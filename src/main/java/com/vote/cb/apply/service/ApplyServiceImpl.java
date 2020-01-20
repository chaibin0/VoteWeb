package com.vote.cb.apply.service;

import com.vote.cb.apply.controller.dto.ApplyRequestDto;
import com.vote.cb.apply.controller.dto.ApplyResponseDto;
import com.vote.cb.apply.domain.Apply;
import com.vote.cb.apply.domain.ApplyRepository;
import com.vote.cb.apply.domain.enums.ApplyStatusType;
import com.vote.cb.exception.ApplyNotFoundException;
import com.vote.cb.exception.CustomException;
import com.vote.cb.exception.ExceptionDetails;
import com.vote.cb.exception.MemberNotFoundException;
import com.vote.cb.exception.UnAuthorizedException;
import com.vote.cb.user.domain.Member;
import com.vote.cb.user.domain.MemberRepository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

@Service
public class ApplyServiceImpl implements ApplyService {

  private ApplyRepository applyRepository;

  private MemberRepository memberRepository;

  public ApplyServiceImpl(ApplyRepository applyRepository, MemberRepository memberRepository) {

    this.applyRepository = applyRepository;
    this.memberRepository = memberRepository;
  }

  @Override
  public Page<Apply> getApplyAllList(Pageable pageable, User user) {

    Member member =
        memberRepository.findById(user.getUsername())
            .orElseThrow(() -> CustomException.MEMBER_NOT_FOUND);

    return applyRepository.findAllByUser(pageable, member);
  }

  @Override
  public Page<Apply> getApplyAllList(Pageable pageable) {

    return applyRepository.findAll(pageable);
  }

  @Override
  public ApplyResponseDto getApply(User user, Long applyId) {

    Apply apply =
        applyRepository.findById(applyId).orElseThrow(() -> CustomException.APPLY_NOT_FOUND);

    if (!apply.isWriter(user)) {
      throw CustomException.UNAUTHORIZED;
    }
    return ApplyResponseDto.of(apply);
  }

  @Override
  public ResponseEntity<?> registerApply(User user, ApplyRequestDto dto) {

    Member member =
        memberRepository.findById(user.getUsername())
            .orElseThrow(() -> CustomException.MEMBER_NOT_FOUND);

    Apply apply = dto.toApply(member);

    return ResponseEntity.created(null).body(applyRepository.save(apply));
  }

  @Override
  public List<Apply> getApplyList(User user) {

    Member member =
        memberRepository.findById(user.getUsername())
            .orElseThrow(() -> CustomException.MEMBER_NOT_FOUND);
    List<Apply> applies = applyRepository.findAllByUser(member);
    return applies;
  }

  @Override
  public ResponseEntity<?> removeApply(User user, Long applyId) {

    Apply apply =
        applyRepository.findById(applyId).orElseThrow(() -> CustomException.APPLY_NOT_FOUND);

    if (!apply.isWriter(user)) {
      throw CustomException.UNAUTHORIZED;
    }

    apply.setDeleted(true);
    apply.setStatus(ApplyStatusType.DELETED);

    applyRepository.save(apply);
    return ResponseEntity.accepted().build();
  }

  @Override
  public ResponseEntity<?> modifyApply(User user, ApplyRequestDto dto) {

    Apply apply =
        applyRepository.findById(dto.getId()).orElseThrow(() -> CustomException.APPLY_NOT_FOUND);

    if (!apply.isWriter(user)) {
      throw CustomException.UNAUTHORIZED;
    }

    if (apply.isVoted()) {
      return ResponseEntity.badRequest()
          .body(new ExceptionDetails(LocalDateTime.now(), HttpStatus.BAD_REQUEST.value(),
              HttpStatus.BAD_REQUEST.getReasonPhrase(), "투표 중이거나 끝난것에 대해서는  추가할 수 없습니다."));
    }

    apply.modify(dto);

    return ResponseEntity.ok().body(applyRepository.save(apply));
  }

  @Override
  public boolean alreadyStart(User user, Long applyId) {

    Apply apply =
        applyRepository.findById(applyId).orElseThrow(() -> CustomException.APPLY_NOT_FOUND);

    if (!apply.isWriter(user)) {
      throw CustomException.UNAUTHORIZED;
    }

    if (apply.isVoted()) {
      return true;
    }

    return false;
  }

  @Override
  public boolean hasVote(User user, Long applyId) {

    Apply apply =
        applyRepository.findById(applyId).orElseThrow(() -> CustomException.APPLY_NOT_FOUND);

    if (!apply.isWriter(user)) {
      throw CustomException.UNAUTHORIZED;
    }

    return apply.isHasVote();
  }


}
