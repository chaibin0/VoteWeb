package com.vote.cb.admin.service;

import com.vote.cb.user.domain.Member;
import com.vote.cb.user.domain.enums.UserStatusType;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface AdminService {

  ResponseEntity<?> removeUser(String id);

  Page<Member> getUserList(Pageable pageable, String search, UserStatusType type);

  ResponseEntity<?> approvalApply(Long id);

  ResponseEntity<?> rejectApply(Long id);
}
