package com.vote.cb.apply.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VoterDto {

  @JsonProperty(value = "voterId")
  Long voterId;

  @JsonProperty(value = "voterName")
  @NotBlank(message = "유권자 이름을 다시 입력하세요")
  String voterName;

  @JsonProperty(value = "voterPhone")
  @NotBlank(message = "유권자 번호를 다시 입력하세요.")
  @Pattern(regexp = "(\\d{3})(\\d{3,4})(\\d{4})", message = "휴대전화 번호를 제대로 입력하세요")
  String voterPhone;
}
