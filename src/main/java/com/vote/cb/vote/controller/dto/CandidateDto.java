package com.vote.cb.vote.controller.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CandidateDto {

  @JsonProperty(value = "candidateSeqNo")
  @NotEmpty(message = "후보자가 존재하지 않습니다.")
  int candidateSeqNo;

  @JsonProperty(value = "candidateName")
  @NotEmpty(message = "후보자 이름이 없습니다.")
  String candidateName;

  @JsonProperty(value = "candidateDesc")
  String candidateDesc;
}
