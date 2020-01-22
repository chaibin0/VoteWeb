package com.vote.cb.admin.service;

import com.vote.cb.admin.controller.dto.UserBlackDto;
import com.vote.cb.user.domain.Member;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface AdminService {

  ResponseEntity<?> removeUser(String id);

  Page<Member> getUserList(Pageable pageable, String search);

  ResponseEntity<?> approvalApply(Long id);

  ResponseEntity<?> rejectApply(Long id);

  ResponseEntity<?> blackUser(@Valid UserBlackDto dto);

  Page<Member> getUserBlackList(Pageable pageable, String search);
}
