package com.vote.cb.admin.service;

import com.vote.cb.apply.domain.Apply;
import com.vote.cb.apply.domain.ApplyRepository;
import com.vote.cb.exception.CustomException;
import com.vote.cb.user.domain.Member;
import com.vote.cb.user.domain.MemberRepository;
import com.vote.cb.user.domain.enums.UserStatusType;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    Member member = userRepository.findById(id).orElseThrow(() -> CustomException.MEMBER_NOT_FOUND);
    userRepository.delete(member);

    return ResponseEntity.accepted().build();
  }

  @Override
  public Page<Member> getUserList(Pageable pageable, String search, UserStatusType type) {

    if (search.isEmpty() && type != null) {
      return userRepository.findAllByUserIdLikeAndStatusOrderByCreatedAt(pageable, search, type);
    }

    if (type != null) {
      return userRepository.findAllByStatusOrderByCreatedAt(pageable, type);
    }

    if (search.isEmpty()) {
      return userRepository.findAllByUserIdLikeOrderByCreatedAt(pageable, search);
    }

    return userRepository.findAll(pageable);
  }

  @Override
  @Transactional
  public ResponseEntity<?> approvalApply(Long applyId) {

    Apply apply =
        applyRepository.findById(applyId).orElseThrow(() -> CustomException.APPLY_NOT_FOUND);
    apply.setApproval(1);
    applyRepository.save(apply);
    return ResponseEntity.accepted().build();
  }

  @Override
  public ResponseEntity<?> rejectApply(Long applyId) {

    Apply apply =
        applyRepository.findById(applyId).orElseThrow(() -> CustomException.APPLY_NOT_FOUND);
    apply.setApproval(0);
    applyRepository.save(apply);
    return ResponseEntity.accepted().build();

  }
}
