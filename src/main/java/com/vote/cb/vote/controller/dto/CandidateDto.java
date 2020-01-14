package com.vote.cb.vote.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vote.cb.vote.domain.Candidate;
import com.vote.cb.vote.domain.Vote;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

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
  @NotBlank(message = "후보자 이름이 없습니다.")
  String candidateName;

  @JsonProperty(value = "candidateDesc")
  String candidateDesc;

  public Candidate toCandidate(Vote vote) {

    return Candidate.builder()
        .sequenceNumber(candidateSeqNo)
        .name(candidateName.trim())
        .description(candidateDesc.trim())
        .vote(vote)
        .build();
  }
}
