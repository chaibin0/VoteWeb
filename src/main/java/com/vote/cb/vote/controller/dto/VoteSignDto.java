package com.vote.cb.vote.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VoteSignDto {

  @JsonProperty(value = "name")
  @NotBlank(message = "이름을 입력해주세요")
  String name;

  @JsonProperty(value = "phone")
  @NotBlank(message = "전화번호를 입력해주세요")
  String phone;
}
