package com.vote.cb.user.controller;

import com.vote.cb.user.controller.dto.CheckUserIdResponseDto;
import com.vote.cb.user.controller.dto.MemberResponseDto;
import com.vote.cb.user.controller.dto.PasswordModifyDto;
import com.vote.cb.user.controller.dto.SignUpDto;
import com.vote.cb.user.controller.dto.UserModifyDto;
import com.vote.cb.user.service.MemberService;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user")
public class MemberApiController {

  @Autowired
  private MemberService userService;

  @PatchMapping("/info")
  public ResponseEntity<?> editUser(@AuthenticationPrincipal User user,
      @RequestBody @Valid UserModifyDto dto) {

    return userService.modifyUser(user, dto);
  }

  @PatchMapping("/password")
  public ResponseEntity<?> editPassword(@AuthenticationPrincipal User user,
      @RequestBody PasswordModifyDto dto) {

    return userService.modifyPassword(user, dto);
  }

  @PostMapping("/signup")
  public ResponseEntity<?> signUpUser(@RequestBody @Valid SignUpDto dto) {

    return userService.signUpUser(dto);
  }

  @GetMapping("/check-id")
  public CheckUserIdResponseDto checkId(@RequestParam String userId) {

    return userService.checkUserId(userId);
  }

  @GetMapping("/info")
  public MemberResponseDto getInfo(@AuthenticationPrincipal User user) {

    return userService.getInfo(user);
  }

  @DeleteMapping("")
  public ResponseEntity<?> removeUser(@AuthenticationPrincipal User user) {

    return userService.removeUser(user);
  }

}
