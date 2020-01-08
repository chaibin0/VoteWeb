package com.vote.cb.user.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vote.cb.user.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class MemberResponseDto {

  @JsonProperty(value = "name")
  String name;

  @JsonProperty(value = "phone")
  String phone;

  @JsonProperty(value = "email")
  String email;

  public static MemberResponseDto of(Member member) {

    return MemberResponseDto.builder()
        .name(member.getName())
        .phone(member.getPhone())
        .email(member.getEmail())
        .build();
  }
}
