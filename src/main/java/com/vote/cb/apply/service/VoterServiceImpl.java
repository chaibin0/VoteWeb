package com.vote.cb.apply.service;

import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import com.vote.cb.apply.controller.dto.VoterDto;
import com.vote.cb.apply.domain.Apply;
import com.vote.cb.apply.domain.ApplyRepository;
import com.vote.cb.apply.domain.Voter;
import com.vote.cb.apply.domain.VoterRepository;
import com.vote.cb.apply.domain.enums.VoterStatusType;
import com.vote.cb.exception.ApplyNotFoundException;
import com.vote.cb.exception.UnAuthorizedException;
import com.vote.cb.exception.VoterNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class VoterServiceImpl implements VoterService {

  private final ApplyRepository applyRepository;

  private final VoterRepository voterRepository;

  @Override
  public Page<Voter> getVoterListByApply(Pageable pageable, User user, long applyId) {

    Apply apply = applyRepository.findById(applyId).orElseThrow(ApplyNotFoundException::new);

    if (!apply.isWriter(user)) {
      throw new UnAuthorizedException();
    }

    Page<Voter> voterList = voterRepository.findAllByApply(pageable, apply);

    return voterList;
  }

  @Override
  public ResponseEntity<?> registerVoter(User user, VoterDto dto, long applyId) {

    Apply apply = applyRepository.findById(applyId).orElseThrow(ApplyNotFoundException::new);

    if (!apply.isWriter(user)) {
      throw new UnAuthorizedException();
    }

    if (apply.isVoted()) {
      return ResponseEntity.badRequest().body("투표 중이거나 끝난것에 대해서는  추가할 수 없습니다.");
    }

    Voter savedVoter = voterRepository.save(Voter.of(apply, dto));
    return ResponseEntity.created(null).body(savedVoter);
  }

  @Override
  public ResponseEntity<?> modifyVoter(User user, VoterDto dto, long applyId) {

    Voter voter =
        voterRepository.findById(dto.getVoterId()).orElseThrow(VoterNotFoundException::new);

    if (voter.getApply().isWriter(user)) {
      throw new UnAuthorizedException();
    }

    if (voter.getApply().isVoted()) {
      return ResponseEntity.badRequest().body("투표 중이거나 끝난것에 대해서는 수정할 수 없습니다.");
    }

    voter.setName(dto.getVoterName())
        .setPhone(dto.getVoterPhone());

    Voter saveVoter = voterRepository.save(voter);
    return ResponseEntity.ok().body(saveVoter);
  }

  @Override
  public ResponseEntity<?> removeVoter(User user, long applyId, long voterId) {

    Voter voter = voterRepository.findById(voterId).orElseThrow(VoterNotFoundException::new);

    if (!voter.getApply().isWriter(user)) {
      throw new UnAuthorizedException();
    }

    if (voter.getApply().isVoted()) {
      return ResponseEntity.badRequest().body("투표 중이거나 끝난것에 대해서는 수정할 수 없습니다.");
    }

    voterRepository.delete(voter);
    
    return ResponseEntity.ok().build();
  }



}