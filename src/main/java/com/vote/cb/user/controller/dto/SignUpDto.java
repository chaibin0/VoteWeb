package com.vote.cb.user.controller.dto;

import com.vote.cb.user.domain.Member;
import com.vote.cb.user.domain.enums.UserRole;
import com.vote.cb.user.domain.enums.UserStatusType;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.springframework.security.crypto.password.PasswordEncoder;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SignUpDto {

  @NotBlank(message = "아이디를 입력해주세요")
  String id;

  @NotBlank(message = "이름을 입력해주세요")
  String name;

  @NotBlank(message = "비밀번호를 입력해주세요")
  String password;

  @NotBlank(message = "휴대전화를 입력해주세요")
  @Pattern(regexp = "(\\d{3})(\\d{3,4})(\\d{4})", message = "휴대전화 번호를 제대로 입력하세요")
  String phone;

  @NotBlank(message = "이메일을 입력해주세요")
  @Email(message = "이메일양식에 맞춰 입력하세요")
  String email;

  public Member toMember(PasswordEncoder passwordEncoder) {

    return Member.builder()
        .userId(id)
        .password(passwordEncoder.encode(password))
        .name(name)
        .email(email)
        .phone(phone)
        .status(UserStatusType.NORMAL)
        .role(UserRole.USER).build();
  }


}
