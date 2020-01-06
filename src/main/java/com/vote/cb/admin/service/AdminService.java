package com.vote.cb.admin.service;

import org.springframework.http.ResponseEntity;
import com.vote.cb.user.domain.Member;

public interface AdminService {

  ResponseEntity<?> removeUser(String id);

  ResponseEntity<Member> getUser(String id);

  ResponseEntity<?> approvalApply(Long id);

  ResponseEntity<?> rejectApply(Long id);

}
