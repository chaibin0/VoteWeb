package com.vote.cb.admin.service;

import com.vote.cb.exception.MemberNotFoundException;
import com.vote.cb.user.domain.Member;
import com.vote.cb.user.domain.MemberRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class AdminServiceImpl implements AdminService {

  private MemberRepository userRepository;

  public AdminServiceImpl(MemberRepository userRepository) {

    this.userRepository = userRepository;
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


}
