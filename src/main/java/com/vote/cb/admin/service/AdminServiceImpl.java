package com.vote.cb.admin.service;

import com.vote.cb.apply.domain.Apply;
import com.vote.cb.apply.domain.ApplyRepository;
import com.vote.cb.exception.ApplyNotFoundException;
import com.vote.cb.exception.MemberNotFoundException;
import com.vote.cb.user.domain.Member;
import com.vote.cb.user.domain.MemberRepository;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AdminServiceImpl implements AdminService {

  private MemberRepository userRepository;

  private ApplyRepository applyRepository;

  public AdminServiceImpl(MemberRepository userRepository, ApplyRepository applyRepository) {

    this.userRepository = userRepository;
    this.applyRepository = applyRepository;
  }

  @Override
  public ResponseEntity<?> removeUser(String id) {

    Member member = userRepository.findById(id).orElseThrow(MemberNotFoundException::new);
    userRepository.delete(member);

    return ResponseEntity.accepted().build();
  }

  @Override
  public ResponseEntity<Member> getUser(String id) {

    Member member = userRepository.findById(id).orElseThrow(MemberNotFoundException::new);
    return ResponseEntity.ok(member);
  }

  @Override
  @Transactional
  public ResponseEntity<?> approvalApply(Long applyId) {

    Apply apply = applyRepository.findById(applyId).orElseThrow(ApplyNotFoundException::new);
    apply.setApproval(1);
    applyRepository.save(apply);
    return ResponseEntity.accepted().build();
  }

  @Override
  public ResponseEntity<?> rejectApply(Long applyId) {

    Apply apply = applyRepository.findById(applyId).orElseThrow(ApplyNotFoundException::new);
    apply.setApproval(0);
    applyRepository.save(apply);
    return ResponseEntity.accepted().build();

  }
}
