package com.vote.cb.user.service;

import javax.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import com.vote.cb.user.controller.dto.CheckUserIdRequestDto;
import com.vote.cb.user.controller.dto.CheckUserIdResponseDto;
import com.vote.cb.user.controller.dto.MemberResponseDto;
import com.vote.cb.user.controller.dto.SignUpDto;

public interface MemberService {

  ResponseEntity<?> findApply(String userName, String phone, HttpSession session);

  ResponseEntity<?> signUpUser(SignUpDto dto);

  CheckUserIdResponseDto checkUserId(String userId);

  MemberResponseDto getInfo(User user);

  ResponseEntity<?> removeUser(User user);
}
