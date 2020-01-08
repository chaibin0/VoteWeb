package com.vote.cb.vote.service;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;

public interface ResultService {

  ResponseEntity<?> countVote(User user, long applyId) throws Exception;
  
}
