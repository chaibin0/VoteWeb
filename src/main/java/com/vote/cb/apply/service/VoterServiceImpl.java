package com.vote.cb.apply.service;

import com.vote.cb.apply.controller.dto.VoterDto;
import com.vote.cb.apply.domain.Apply;
import com.vote.cb.apply.domain.ApplyRepository;
import com.vote.cb.apply.domain.Voter;
import com.vote.cb.apply.domain.VoterRepository;
import com.vote.cb.exception.ApplyNotFoundException;
import com.vote.cb.exception.CustomException;
import com.vote.cb.exception.ExceptionDetails;
import com.vote.cb.exception.UnAuthorizedException;
import com.vote.cb.exception.VoterNotFoundException;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

@Service
public class VoterServiceImpl implements VoterService {

  private ApplyRepository applyRepository;

  private VoterRepository voterRepository;

  public VoterServiceImpl(ApplyRepository applyRepository, VoterRepository voterRepository) {

    this.applyRepository = applyRepository;
    this.voterRepository = voterRepository;
  }

  @Override
  public Page<Voter> getVoterListByApply(Pageable pageable, User user, long applyId) {

    Apply apply =
        applyRepository.findById(applyId).orElseThrow(() -> CustomException.APPLY_NOT_FOUND);

    if (!apply.isWriter(user)) {
      throw CustomException.UNAUTHORIZED;
    }

    Page<Voter> voterList = voterRepository.findAllByApply(pageable, apply);

    return voterList;
  }

  @Override
  public ResponseEntity<?> registerVoter(User user, VoterDto dto, long applyId) {

    Apply apply =
        applyRepository.findById(applyId).orElseThrow(() -> CustomException.APPLY_NOT_FOUND);

    if (!apply.isWriter(user)) {
      throw CustomException.UNAUTHORIZED;
    }

    if (apply.isVoted()) {
      return ResponseEntity.badRequest()
          .body(new ExceptionDetails(LocalDateTime.now(), HttpStatus.BAD_REQUEST.value(),
              HttpStatus.BAD_REQUEST.getReasonPhrase(), "투표 중이거나 끝난것에 대해서는  추가할 수 없습니다."));
    }

    voterRepository.save(dto.toVoter(apply));

    return ResponseEntity.created(null).build();
  }

  @Override
  public ResponseEntity<?> modifyVoter(User user, VoterDto dto, long applyId) {

    Voter voter =
        voterRepository.findById(dto.getVoterId())
            .orElseThrow(() -> CustomException.VOTER_NOT_FOUND);

    if (voter.getApply().isWriter(user)) {
      throw CustomException.UNAUTHORIZED;
    }

    if (voter.getApply().isVoted()) {
      return ResponseEntity.badRequest()
          .body(new ExceptionDetails(LocalDateTime.now(), HttpStatus.BAD_REQUEST.value(),
              HttpStatus.BAD_REQUEST.getReasonPhrase(), "투표 중이거나 끝난것에 대해서는  추가할 수 없습니다."));
    }

    voterRepository.save(dto.modifyByDto(voter));

    return ResponseEntity.ok().build();
  }

  @Override
  public ResponseEntity<?> removeVoter(User user, long applyId, long voterId) {

    Voter voter =
        voterRepository.findById(voterId).orElseThrow(() -> CustomException.VOTER_NOT_FOUND);

    if (!voter.getApply().isWriter(user)) {
      throw CustomException.UNAUTHORIZED;
    }

    if (voter.getApply().isVoted()) {
      return ResponseEntity.badRequest()
          .body(new ExceptionDetails(LocalDateTime.now(), HttpStatus.BAD_REQUEST.value(),
              HttpStatus.BAD_REQUEST.getReasonPhrase(), "투표 중이거나 끝난것에 대해서는  추가할 수 없습니다."));
    }

    voterRepository.delete(voter);

    return ResponseEntity.ok().build();
  }



}
