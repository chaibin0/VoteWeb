package com.vote.cb.vote.service;

import com.vote.cb.apply.domain.Apply;
import com.vote.cb.apply.domain.ApplyRepository;
import com.vote.cb.apply.domain.Voter;
import com.vote.cb.apply.domain.VoterRepository;
import com.vote.cb.apply.domain.enums.VoterStatusType;
import com.vote.cb.exception.AlreadyRegiststeredException;
import com.vote.cb.exception.ApplyNotFoundException;
import com.vote.cb.exception.CandidateNotFoundException;
import com.vote.cb.exception.CustomException;
import com.vote.cb.exception.ExceptionDetails;
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

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class VoteServiceImpl implements VoteService {

  private VoteRepository voteRepository;

  private ApplyRepository applyRepository;

  private VoteInfoRepository voteInfoRepository;

  private CandidateRepository candidateRepository;

  private VoterRepository voterRepository;

  private ResultRepository resultRepository;

  public VoteServiceImpl(VoteRepository voteRepository, ApplyRepository applyRepository,
      VoteInfoRepository voteInfoRepository, CandidateRepository candidateRepository,
      VoterRepository voterRepository, ResultRepository resultRepository) {

    this.voteRepository = voteRepository;
    this.applyRepository = applyRepository;
    this.voteInfoRepository = voteInfoRepository;
    this.candidateRepository = candidateRepository;
    this.voterRepository = voterRepository;
    this.resultRepository = resultRepository;
  }


  @Override
  @Transactional
  public ResponseEntity<VoteInfomation> saveVoteInfo(User user, VoteInfoDto voteInfoDto) {

    // 신청서와 현재 session 정보가 같아야만 등록이 가능하게 해야함
    Apply apply =
        applyRepository.findById(voteInfoDto.getApplyId())
            .orElseThrow(() -> CustomException.APPLY_NOT_FOUND);

    if (!apply.isWriter(user)) {
      throw CustomException.UNAUTHORIZED;
    }

    if (apply.isHasVote()) {
      throw CustomException.ALREADY_REGISTERED;
    }

    VoteInfomation saveVoteInfo = voteInfoRepository.save(voteInfoDto.toVoteInfomation(apply));

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
      Vote saveVote = voteRepository.save(voteDto.toVote(voteInfo));
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
      Candidate saveCandidate = candidateRepository.save(candidateDto.toCandidate(vote));
      candidateList.add(saveCandidate);
    }

    return candidateList;
  }


  @Override
  public ResponseEntity<?> authVoterInfo(VoteSignDto dto, HttpSession session) {

    Voter voter =
        voterRepository.findByNameAndPhoneAndSsn(dto.getName(), dto.getPhone(), dto.getUid())
            .orElseThrow(() -> CustomException.VOTER_NOT_FOUND);

    if (voter == null) {
      return ResponseEntity.badRequest()
          .body(new ExceptionDetails(LocalDateTime.now(), HttpStatus.BAD_REQUEST.value(),
              HttpStatus.BAD_REQUEST.getReasonPhrase(), "투표정보가 정확하지 않습니다."));
    }


    if (voter.getApply().getEnd().isBefore(LocalDateTime.now())) {
      return ResponseEntity.badRequest()
          .body(new ExceptionDetails(LocalDateTime.now(), HttpStatus.BAD_REQUEST.value(),
              HttpStatus.BAD_REQUEST.getReasonPhrase(), "투표가 끝났습니다."));
    }

    if (voter.getApply().getStart().isAfter(LocalDateTime.now())) {
      return ResponseEntity.badRequest()
          .body(new ExceptionDetails(LocalDateTime.now(), HttpStatus.BAD_REQUEST.value(),
              HttpStatus.BAD_REQUEST.getReasonPhrase(), "투표시작 전입니다."));
    }
    if (voter.getStatus().equals(VoterStatusType.VOTED)) {
      return ResponseEntity.badRequest()
          .body(new ExceptionDetails(LocalDateTime.now(), HttpStatus.BAD_REQUEST.value(),
              HttpStatus.BAD_REQUEST.getReasonPhrase(), "이미 투표하셨습니다."));
    }

    session.setAttribute("name", voter.getName());
    session.setAttribute("phone", voter.getPhone());
    session.setAttribute("uid", voter.getSsn());
    return ResponseEntity.ok().build();
  }

  @Override
  public boolean invalidUid(String uid) {

    return !voterRepository.existsBySsn(uid);
  }

  @Override
  public VoteInfomation getVoteList(String name, String phone, String uid) {

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
        return ResponseEntity.badRequest()
            .body(new ExceptionDetails(LocalDateTime.now(), HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(), "잘못된 요청입니다."));
      }

      Vote vote =
          voteRepository.findById(votingDto.getVoteId()).orElseThrow(VoteNotFoundException::new);

      if (vote.getVoteInfo().getId() != voteInfo.getId()) {
        return ResponseEntity.badRequest()
            .body(new ExceptionDetails(LocalDateTime.now(), HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(), "잘못된 요청입니다."));
      }

      Candidate candidate = candidateRepository.findById(votingDto.getCandidateId())
          .orElseThrow(CandidateNotFoundException::new);

      if (candidate.getVote().getId() != vote.getId()) {
        return ResponseEntity.badRequest()
            .body(new ExceptionDetails(LocalDateTime.now(), HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(), "잘못된 요청입니다."));
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

    Apply apply =
        applyRepository.findById(applyId).orElseThrow(() -> CustomException.APPLY_NOT_FOUND);

    if (!apply.isWriter(user)) {
      throw CustomException.UNAUTHORIZED;
    }

    VoteInfomation voteInfo =
        voteInfoRepository.findByApply(apply).orElseThrow(() -> CustomException.VOTEINFO_NOT_FOUND);
    return VoteResponseDto.of(applyId, voteInfo);
  }

  @Override
  @Transactional
  public ResponseEntity<?> modifyVoteInfo(User user, @Valid VoteInfoDto dto) {

    Apply apply =
        applyRepository.findById(dto.getApplyId())
            .orElseThrow(() -> CustomException.APPLY_NOT_FOUND);

    if (!apply.isWriter(user)) {
      throw CustomException.UNAUTHORIZED;
    }

    VoteInfomation voteInfo =
        voteInfoRepository.findByApply(apply).orElseThrow(() -> CustomException.VOTEINFO_NOT_FOUND);

    voteInfo.setDescription(dto.getVoteInfoDesc())
        .setName(dto.getVoteInfoTitle());

    voteRepository.deleteInBatch(voteInfo.getVoteList());
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
