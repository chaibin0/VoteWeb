package com.vote.cb.user.service;

import com.vote.cb.apply.domain.Apply;
import com.vote.cb.apply.domain.ApplyRepository;
import com.vote.cb.apply.domain.UserRoleRepository;
import com.vote.cb.exception.MemberNotFoundException;
import com.vote.cb.user.controller.dto.CheckUserIdResponseDto;
import com.vote.cb.user.controller.dto.MemberResponseDto;
import com.vote.cb.user.controller.dto.SignUpDto;
import com.vote.cb.user.domain.Member;
import com.vote.cb.user.domain.MemberRepository;
import com.vote.cb.user.domain.UserRole;
import com.vote.cb.user.domain.enums.UserRoleType;
import com.vote.cb.user.domain.enums.UserStatusType;

import java.util.List;

import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class MemberServiceImpl implements MemberService {

  private ApplyRepository applyRepository;

  private MemberRepository userRepository;

  private PasswordEncoder passwordEncoder;

  private UserRoleRepository userRoleRepository;

  public MemberServiceImpl(ApplyRepository applyRepository, MemberRepository userRepository,
      PasswordEncoder passwordEncoder, UserRoleRepository userRoleRepository) {

    this.applyRepository = applyRepository;
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
    this.userRoleRepository = userRoleRepository;
  }

  @Override
  public ResponseEntity<?> findApply(String userName, String phone, HttpSession session) {

    List<Apply> apply = applyRepository.findAllByNameAndPhone(userName, phone);

    if (apply.isEmpty()) {
      return ResponseEntity.badRequest().body("ID나 비밀번호가 존재하지 않습니다."); // 임시
    }

    // 임시 세션 저장
    session.setAttribute("username", userName);
    session.setAttribute("phone", phone);
    return ResponseEntity.ok(null);
  }

  @Override
  @Transactional
  public ResponseEntity<?> signUpUser(SignUpDto dto) {

    if (userRepository.existsById(dto.getId())) {
      return ResponseEntity.badRequest().body("아이디 중복");
    }

    Member newUser = userRepository.save(dto.toMember(passwordEncoder));

    UserRole userRole = UserRole.builder()
        .user(newUser)
        .role(UserRoleType.USER)
        .build();

    userRoleRepository.save(userRole);

    return ResponseEntity.created(null).body(newUser);
  }

  @Override
  public CheckUserIdResponseDto checkUserId(String userId) {

    if (userRepository.existsById(userId)) {
      return new CheckUserIdResponseDto(true);
    }
    return new CheckUserIdResponseDto(false);
  }

  @Override
  public MemberResponseDto getInfo(User user) {

    Member member =
        userRepository.findById(user.getUsername()).orElseThrow(MemberNotFoundException::new);

    return MemberResponseDto.of(member);
  }

  @Override
  public ResponseEntity<?> removeUser(User user) {

    userRepository.deleteById(user.getUsername());
    return ResponseEntity.accepted().build();
  }
}
