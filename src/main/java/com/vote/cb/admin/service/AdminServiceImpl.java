package com.vote.cb.admin.service;

import com.vote.cb.admin.controller.dto.UserBlackDto;
import com.vote.cb.apply.domain.Apply;
import com.vote.cb.apply.domain.ApplyRepository;
import com.vote.cb.exception.CustomException;
import com.vote.cb.user.domain.Black;
import com.vote.cb.user.domain.BlackRepository;
import com.vote.cb.user.domain.Member;
import com.vote.cb.user.domain.MemberRepository;
import com.vote.cb.user.domain.enums.UserRoleType;
import com.vote.cb.user.domain.enums.UserStatusType;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AdminServiceImpl implements AdminService {

  private MemberRepository userRepository;

  private ApplyRepository applyRepository;

  private BlackRepository blackRepository;

  public AdminServiceImpl(MemberRepository userRepository, ApplyRepository applyRepository,
      BlackRepository blackRepository) {

    this.userRepository = userRepository;
    this.applyRepository = applyRepository;
    this.blackRepository = blackRepository;
  }

  @Override
  public ResponseEntity<?> removeUser(String id) {

    Member member = userRepository.findById(id).orElseThrow(() -> CustomException.MEMBER_NOT_FOUND);
    userRepository.delete(member);

    return ResponseEntity.accepted().build();
  }

  @Override
  public Page<Member> getUserList(Pageable pageable, String search) {

    Page<Member> memberList =
        userRepository.findAllByUserIdContainingOrderByCreatedAt(pageable, search);

    List<Member> filteredMemberList = memberList.stream()
        .filter((member) -> member.getRole()
            .stream()
            .anyMatch((role) -> !(role.getRole().equals(UserRoleType.ADMIN))))
        .collect(Collectors.toList());

    return new PageImpl<Member>(filteredMemberList, pageable, filteredMemberList.size());
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

  // 테이블 분리할 예정
  @Override
  @Transactional
  public ResponseEntity<?> blackUser(@Valid UserBlackDto dto) {

    Member member =
        userRepository.findById(dto.getId()).orElseThrow(() -> CustomException.MEMBER_NOT_FOUND);

    if (!dto.isBlack()) {
      Black black =
          blackRepository.findByUser(member).orElseThrow(() -> CustomException.MEMBER_NOT_FOUND);
      blackRepository.delete(black);
      member.setStatus(UserStatusType.NORMAL);
      userRepository.save(member);
      return ResponseEntity.ok(null);
    }

    Black black = Black.builder()
        .user(member)
        .phone(member.getPhone())
        .end(LocalDateTime.now())     //임시 
        .build();
    blackRepository.save(black);

    member.setStatus(UserStatusType.BLACK);
    userRepository.save(member);

    return ResponseEntity.ok(null);
  }

  @Override
  public Page<Member> getUserBlackList(Pageable pageable, String search) {

    return userRepository.findAllByUserIdContainingAndStatusOrderByCreatedAt(pageable, search,
        UserStatusType.BLACK);
  }
}
