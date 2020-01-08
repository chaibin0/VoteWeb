package com.vote.cb.admin.service;

import com.vote.cb.user.domain.Member;
import org.springframework.http.ResponseEntity;

public interface AdminService {

  ResponseEntity<?> removeUser(String id);

  ResponseEntity<Member> getUser(String id);

  ResponseEntity<?> approvalApply(Long id);

  ResponseEntity<?> rejectApply(Long id);

}
