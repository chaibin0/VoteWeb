package com.vote.cb.apply.service;

import com.vote.cb.apply.controller.dto.ApplyRequestDto;
import com.vote.cb.apply.controller.dto.ApplyResponseDto;
import com.vote.cb.apply.domain.Apply;
import com.vote.cb.apply.domain.ApplyRepository;
import com.vote.cb.exception.ApplyNotFoundException;
import com.vote.cb.exception.MemberNotFoundException;
import com.vote.cb.exception.UnAuthorizedException;
import com.vote.cb.user.domain.Member;
import com.vote.cb.user.domain.MemberRepository;

import java.util.List;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


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
        memberRepository.findById(user.getUsername()).orElseThrow(MemberNotFoundException::new);

    return applyRepository.findAllByUser(pageable, member);
  }

  @Override
  public Page<Apply> getApplyAllList(Pageable pageable) {

    return applyRepository.findAll(pageable);
  }

  @Override
  public ApplyResponseDto getApply(User user, Long applyId) {

    Apply apply = applyRepository.findById(applyId).orElseThrow(ApplyNotFoundException::new);

    if (!apply.isWriter(user)) {
      throw new UnAuthorizedException();
    }
    return ApplyResponseDto.of(apply);
  }

  @Override
  public ResponseEntity<?> registerApply(User user, ApplyRequestDto dto) {

    Member member =
        memberRepository.findById(user.getUsername()).orElseThrow(MemberNotFoundException::new);

    Apply apply = dto.toApply(member);

    return ResponseEntity.created(null).body(applyRepository.save(apply));
  }

  @Override
  public List<Apply> getApplyList(User user) {

    Member member =
        memberRepository.findById(user.getUsername()).orElseThrow(MemberNotFoundException::new);
    List<Apply> applies = applyRepository.findAllByUser(member);
    return applies;
  }

  @Override
  public ResponseEntity<?> removeApply(User user, Long applyId) {

    Apply apply = applyRepository.findById(applyId).orElseThrow(ApplyNotFoundException::new);

    if (!apply.isWriter(user)) {
      throw new UnAuthorizedException();
    }
    apply.setDeleted(true);
    applyRepository.save(apply);
    return ResponseEntity.accepted().build();
  }

  @Override
  public ResponseEntity<?> modifyApply(User user, ApplyRequestDto dto) {

    Apply apply = applyRepository.findById(dto.getId()).orElseThrow(ApplyNotFoundException::new);

    if (!apply.isWriter(user)) {
      throw new UnAuthorizedException();
    }

    if (apply.isVoted()) {
      return ResponseEntity.badRequest().body("투표가 시작되었으면 수정할 수 없습니다.");
    }

    apply.modify(dto);

    return ResponseEntity.ok().body(applyRepository.save(apply));
  }

  @Override
  public boolean alreadyStart(User user, Long applyId) {

    Apply apply = applyRepository.findById(applyId).orElseThrow(ApplyNotFoundException::new);

    if (!apply.isWriter(user)) {
      throw new UnAuthorizedException();
    }

    if (apply.isVoted()) {
      return true;
    }

    return false;
  }

  @Override
  public boolean hasVote(User user, Long applyId) {

    Apply apply = applyRepository.findById(applyId).orElseThrow(ApplyNotFoundException::new);

    if (!apply.isWriter(user)) {
      throw new UnAuthorizedException();
    }

    return apply.isHasVote();
  }


}
