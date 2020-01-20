package com.vote.cb.user.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PasswordModifyDto {

  @JsonProperty("password")
  @NotBlank(message = "비밀번호가 없음")
  String password;

  @JsonProperty("newPassword")
  @NotBlank(message = "잘못된 비밀번호")
  String newPassword;

  @JsonProperty("newPasswordCheck")
  @NotBlank(message = "잘못된 비밀번호")
  String newPasswordCheck;
}
