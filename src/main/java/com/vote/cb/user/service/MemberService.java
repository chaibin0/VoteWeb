package com.vote.cb.user.service;

import com.vote.cb.user.controller.dto.CheckUserIdResponseDto;
import com.vote.cb.user.controller.dto.MemberResponseDto;
import com.vote.cb.user.controller.dto.PasswordModifyDto;
import com.vote.cb.user.controller.dto.SignUpDto;
import com.vote.cb.user.controller.dto.UserModifyDto;

import javax.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;

public interface MemberService {

  ResponseEntity<?> findApply(String userName, String phone, HttpSession session);

  ResponseEntity<?> signUpUser(SignUpDto dto);

  CheckUserIdResponseDto checkUserId(String userId);

  MemberResponseDto getInfo(User user);

  ResponseEntity<?> removeUser(User user);

  ResponseEntity<?> modifyUser(User user, UserModifyDto dto);

  ResponseEntity<?> modifyPassword(User user, PasswordModifyDto dto);
}
