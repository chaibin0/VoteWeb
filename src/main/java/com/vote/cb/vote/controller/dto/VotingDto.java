package com.vote.cb.vote.controller.dto;

import javax.validation.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;

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
	@NotEmpty(message = "정상적이지 않는 값")
	long voteInfoId;

	@JsonProperty(value = "voteId")
	@NotEmpty(message = "정상적이지 않는 값")
	long voteId;

	@JsonProperty(value = "candidateSequenceNumber")
	@NotEmpty(message = "정상적이지 않는 값")
	long candidateSequenceNumber;

	@JsonProperty(value = "candidateId")
	@NotEmpty(message = "정상적이지 않는 값")
	long candidateId;

	@JsonProperty(value = "value")
	@NotEmpty(message = "정상적이지 않는 값")
	int value;
}
