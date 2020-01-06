package com.vote.cb.vote.controller.dto;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.vote.cb.vote.domain.Vote;
import com.vote.cb.vote.domain.VoteInfomation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class VoteResponseDto {

  @JsonProperty("applyId")
  Long applyId;

  @JsonProperty("id")
  Long id;

  @JsonProperty("name")
  String name;

  @JsonProperty("description")
  String description;

  @JsonProperty("count")
  int count;

  @JsonProperty("current")
  int current;

  @JsonManagedReference
  List<Vote> voteList;

  public static VoteResponseDto of(Long applyId, VoteInfomation voteInfo) {

    return VoteResponseDto.builder()
        .applyId(applyId)
        .id(voteInfo.getId())
        .name(voteInfo.getName())
        .description(voteInfo.getDescription())
        .count(voteInfo.getCount())
        .current(voteInfo.getCurrent())
        .voteList(voteInfo.getVoteList())
        .build();
  }
}
