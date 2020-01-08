package com.vote.cb.vote.service;

import com.vote.cb.apply.domain.Apply;
import com.vote.cb.apply.domain.ApplyRepository;
import com.vote.cb.apply.domain.Voter;
import com.vote.cb.apply.domain.VoterRepository;
import com.vote.cb.apply.domain.enums.VoterStatusType;
import com.vote.cb.exception.ApplyNotFoundException;
import com.vote.cb.exception.CandidateNotFoundException;
import com.vote.cb.exception.UnAuthorizedException;
import com.vote.cb.exception.VoteInfoNotFoundException;
import com.vote.cb.exception.VoteNotFoundException;
import com.vote.cb.exception.VoterNotFoundException;
import com.vote.cb.vote.controller.dto.CandidateDto;
import com.vote.cb.vote.controller.dto.VoteDto;
import com.vote.cb.vote.controller.dto.VoteInfoDto;
import com.vote.cb.vote.controller.dto.VoteResponseDto;
import com.vote.cb.vote.controller.dto.VoteSignDto;
import com.vote.cb.vote.controller.dto.VotingDto;
import com.vote.cb.vote.domain.Candidate;
import com.vote.cb.vote.domain.CandidateRepository;
import com.vote.cb.vote.domain.Result;
import com.vote.cb.vote.domain.ResultRepository;
import com.vote.cb.vote.domain.Vote;
import com.vote.cb.vote.domain.VoteInfoRepository;
import com.vote.cb.vote.domain.VoteInfomation;
import com.vote.cb.vote.domain.VoteRepository;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class VoteServiceImpl implements VoteService {

  private final VoteRepository voteRepository;

  private final ApplyRepository applyRepository;

  private final VoteInfoRepository voteInfoRepository;

  private final CandidateRepository candidateRepository;

  private final VoterRepository voterRepository;

  private final ResultRepository resultRepository;

  @Override
  @Transactional
  public ResponseEntity<VoteInfomation> saveVoteInfo(User user, VoteInfoDto voteInfoDto) {

    // 신청서와 현재 session 정보가 같아야만 등록이 가능하게 해야함
    Apply apply =
        applyRepository.findById(voteInfoDto.getApplyId()).orElseThrow(ApplyNotFoundException::new);

    if (!apply.isWriter(user)) {
      throw new UnAuthorizedException();
    }

    VoteInfomation voteInfo = VoteInfomation.of(apply, voteInfoDto);
    VoteInfomation saveVoteInfo = voteInfoRepository.save(voteInfo);

    List<Vote> voteList = saveVote(saveVoteInfo, voteInfoDto.getVoteDto());
    saveVoteInfo.setVoteList(voteList);

    apply.setHasVote(true);
    applyRepository.save(apply);

    return ResponseEntity.created(null).body(saveVoteInfo);
  }


  // 나중에 json데이터에 상위 객체의 ID도 저장하고 ID와 비교해서 한번더 validation 할지 생각 할것
  @Transactional
  private List<Vote> saveVote(VoteInfomation voteInfo, List<VoteDto> voteDtoList) {

    List<Vote> voteList = new ArrayList<>();

    for (VoteDto voteDto : voteDtoList) {
      Vote vote = Vote.of(voteInfo, voteDto);
      Vote saveVote = voteRepository.save(vote);

      List<Candidate> candidateList = saveCandidate(saveVote, voteDto.getCandidate());
      saveVote.setCandidateList(candidateList);
      voteList.add(saveVote);
    }

    return voteList;
  }

  @Transactional
  private List<Candidate> saveCandidate(Vote vote, List<CandidateDto> candidateDtoList) {

    List<Candidate> candidateList = new ArrayList<>();
    for (CandidateDto candidateDto : candidateDtoList) {
      Candidate candidate = Candidate.of(vote, candidateDto);
      Candidate saveCandidate = candidateRepository.save(candidate);
      candidateList.add(saveCandidate);
    }

    return candidateList;
  }


  @Override
  public ResponseEntity<?> authVoterInfo(String uid, VoteSignDto dto, HttpSession session) {

    Voter voter = voterRepository.findByNameAndPhoneAndSsn(dto.getName(), dto.getPhone(), uid)
        .orElseThrow(VoterNotFoundException::new);

    if (voter == null) {
      return ResponseEntity.badRequest().body("투표정보가 정확하지 않습니다.");
    }

    if (voter.getApply().getEnd().isBefore(LocalDateTime.now())) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("투표가 끝났습니다.");
    }
    if (voter.getApply().getStart().isAfter(LocalDateTime.now())) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("투표시작 전입니다.");
    }
    if (voter.getStatus().equals(VoterStatusType.VOTED)) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("이미 투표하셨습니다.");
    }

    session.setAttribute("name", voter.getName());
    session.setAttribute("phone", voter.getPhone());
    return ResponseEntity.ok().build();
  }

  @Override
  public boolean invalidUid(String uid) {

    return !voterRepository.existsBySsn(uid);
  }

  @Override
  public VoteInfomation getVoteList(String name, String phone, String uid) throws Exception {

    Voter voter = voterRepository.findByNameAndPhoneAndSsn(name, phone, uid)
        .orElseThrow(VoterNotFoundException::new);
    VoteInfomation voteInfo = voteInfoRepository.findByApply(voter.getApply())
        .orElseThrow(VoteInfoNotFoundException::new);
    return voteInfo;
  }



  @Override
  @Transactional
  public ResponseEntity<?> vote(String name, String phone, String uid,
      List<VotingDto> votingDtoList) {

    Voter voter = voterRepository.findByNameAndPhoneAndSsn(name, phone, uid)
        .orElseThrow(VoterNotFoundException::new);
    VoteInfomation voteInfo = voteInfoRepository.findByApply(voter.getApply())
        .orElseThrow(VoteInfoNotFoundException::new);


    for (VotingDto votingDto : votingDtoList) {

      if (voteInfo.getId() != votingDto.getVoteInfoId()) {
        return ResponseEntity.badRequest().body("잘못된 요청입니다");
      }

      Vote vote =
          voteRepository.findById(votingDto.getVoteId()).orElseThrow(VoteNotFoundException::new);

      if (vote.getVoteInfo().getId() != voteInfo.getId()) {
        return ResponseEntity.badRequest().body("잘못된 요청입니다");
      }

      Candidate candidate = candidateRepository.findById(votingDto.getCandidateId())
          .orElseThrow(CandidateNotFoundException::new);

      if (candidate.getVote().getId() != vote.getId()) {
        return ResponseEntity.badRequest().body("잘못된 요청입니다");
      }
      resultRepository.save(Result.of(voter, candidate, votingDto.getValue()));
    }

    voter.setStatus(VoterStatusType.VOTED);
    voterRepository.save(voter);

    voteInfo.addCurrent();
    voteInfoRepository.save(voteInfo);
    return ResponseEntity.created(URI.create("/vote/success")).build();
  }



  @Override
  @Transactional
  public VoteResponseDto getVoteListByApplyId(User user, Long applyId) {

    Apply apply = applyRepository.findById(applyId).orElseThrow(ApplyNotFoundException::new);

    if (!apply.isWriter(user)) {
      throw new UnAuthorizedException();
    }

    VoteInfomation voteInfo =
        voteInfoRepository.findByApply(apply).orElseThrow(VoteInfoNotFoundException::new);
    return VoteResponseDto.of(applyId, voteInfo);
  }

  // 수정할것
  @Override
  public ResponseEntity<?> modifyVoteInfo(User user, @Valid VoteInfoDto dto) {

    Apply apply =
        applyRepository.findById(dto.getApplyId()).orElseThrow(ApplyNotFoundException::new);

    if (!apply.isWriter(user)) {
      throw new UnAuthorizedException();
    }

    VoteInfomation voteInfo =
        voteInfoRepository.findByApply(apply).orElseThrow(VoteInfoNotFoundException::new);

    voteInfo.setCount(dto.getVoteInfoCount())
        .setDescription(dto.getVoteInfoDesc())
        .setName(dto.getVoteInfoTitle());

    voteRepository.deleteAll(voteInfo.getVoteList());
    saveVote(voteInfo, dto.getVoteDto());

    voteInfoRepository.save(voteInfo);

    return ResponseEntity.accepted().build();
  }


  @Override
  public boolean checkVote(String name, String phone, String uid) {

    Voter voter = voterRepository.findByNameAndPhoneAndSsn(name, phone, uid)
        .orElseThrow(VoterNotFoundException::new);
    return voter.getStatus().equals(VoterStatusType.VOTED) ? true : false;
  }

  @Override
  public boolean isApproval(Long applyId) {

    Apply apply = applyRepository.findById(applyId).orElseThrow(ApplyNotFoundException::new);
    return apply.getApproval() != 1 ? false : true;
  }
}
