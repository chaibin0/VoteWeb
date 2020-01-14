package com.vote.cb.vote.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vote.cb.vote.domain.Vote;
import com.vote.cb.vote.domain.VoteInfomation;

import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VoteDto {

  @JsonProperty(value = "voteSeqNum")
  @NotEmpty(message = "투표가 존재하지 않습니다.")
  int voteSeqNum;

  @JsonProperty(value = "voteSelNum")
  @NotEmpty(message = "투표가 존재하지 않습니다.")
  int voteSelNum;

  @JsonProperty(value = "voteName")
  @NotBlank(message = "투표 이름을 입력하세요")
  String voteName;

  @JsonProperty(value = "voteElecNum")
  @NotEmpty(message = "투표 당선 수를 입력하세요")
  int voteElecNum;

  @JsonProperty(value = "candidate")
  @NotEmpty(message = "후보자가 존재하지 않습니다.")
  @Size(min = 2, message = "최소 2명 이상의 후보자가 필요합니다.")
  List<CandidateDto> candidate;

  public Vote toVote(VoteInfomation voteInfo) {

    return Vote.builder()
        .sequenceNumber(voteSeqNum)
        .selectedNumber(voteSelNum)
        .name(voteName.trim())
        .electedCount(voteElecNum)
        .voteInfo(voteInfo)
        .build();
  }
}
