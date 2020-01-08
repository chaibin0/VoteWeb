package com.vote.cb.apply.service;

import com.vote.cb.apply.controller.dto.VoterDto;
import com.vote.cb.apply.domain.Voter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;

public interface VoterService {

  public Page<Voter> getVoterListByApply(Pageable pageable, User user, long applyId);

  public ResponseEntity<?> registerVoter(User user, VoterDto dto, long applyId);

  public ResponseEntity<?> removeVoter(User user, long applyId, long voterId);

  public ResponseEntity<?> modifyVoter(User user, VoterDto dto, long applyId);
}
