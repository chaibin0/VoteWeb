package com.vote.cb.user.controller.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class SignUpDto {

  @NotEmpty(message = "아이디를 입력해주세요")
  String id;

  @NotEmpty(message = "이름을 입력해주세요")
  String name;

  @NotEmpty(message = "비밀번호를 입력해주세요")
  String password;

  @NotEmpty(message = "휴대전화를 입력해주세요")
  @Pattern(regexp = "(\\d{3})(\\d{3,4})(\\d{4})", message = "휴대전화 번호를 제대로 입력하세요")
  String phone;

  @NotEmpty(message = "이메일을 입력해주세요")
  String email;
}
