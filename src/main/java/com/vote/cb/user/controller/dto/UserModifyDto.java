package com.vote.cb.user.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
public class UserModifyDto {

  @JsonProperty("name")
  @NotBlank
  String name;

  @JsonProperty("email")
  @NotBlank
  @Email
  String email;

  @NotBlank(message = "휴대전화를 입력해주세요")
  @Pattern(regexp = "(\\d{3})(\\d{3,4})(\\d{4})", message = "휴대전화 번호를 제대로 입력하세요")
  String phone;

}
