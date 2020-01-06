package com.vote.cb.user.controller.dto;

import javax.validation.constraints.NotBlank;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ApplyValidationDto {

  @JsonProperty("name")
  @NotBlank(message = "이름을 입력하세요")
  String name;

  @JsonProperty("phone")
  @NotBlank(message = "휴대전화를 입력하세요")
  String phone;
}
