package com.vote.cb.apply.service;

import com.vote.cb.apply.controller.dto.ApplyRequestDto;
import com.vote.cb.apply.controller.dto.ApplyResponseDto;
import com.vote.cb.apply.domain.Apply;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;

public interface ApplyService {

  ApplyResponseDto getApply(User user, Long applyId);

  List<Apply> getApplyList(User user);

  ResponseEntity<?> registerApply(User user, ApplyRequestDto dto);

  Page<Apply> getApplyAllList(Pageable pageable, User user);

  Page<Apply> getApplyAllList(Pageable pageable);

  ResponseEntity<?> removeApply(User user, Long applyId);

  ResponseEntity<?> modifyApply(User user, ApplyRequestDto dto);

  boolean alreadyStart(User user, Long applyId);

  boolean hasVote(User user, Long applyId);

}
