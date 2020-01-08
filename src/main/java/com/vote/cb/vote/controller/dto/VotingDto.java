package com.vote.cb.vote.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VotingDto {

  @JsonProperty(value = "voteInfoId")
  @NotNull(message = "정상적이지 않는 값")
  long voteInfoId;

  @JsonProperty(value = "voteId")
  @NotNull(message = "정상적이지 않는 값")
  long voteId;

  @JsonProperty(value = "candidateSequenceNumber")
  @NotNull(message = "정상적이지 않는 값")
  long candidateSequenceNumber;


  @JsonProperty(value = "candidateId")
  @NotNull(message = "정상적이지 않는 값")
  long candidateId;

  @JsonProperty(value = "value")
  @NotNull(message = "정상적이지 않는 값")
  int value;
}
