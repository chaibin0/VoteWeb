package com.vote.cb.vote.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vote.cb.apply.domain.Apply;
import com.vote.cb.vote.domain.VoteInfomation;
import com.vote.cb.vote.domain.enums.VoteInfoStatusType;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import org.springframework.format.annotation.NumberFormat;
import org.springframework.format.annotation.NumberFormat.Style;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VoteInfoDto {

  @JsonProperty(value = "applyId")
  @NotNull(message = "신청서가 존재하지 않습니다.")
  @NumberFormat(style = Style.NUMBER)
  Long applyId;

  @JsonProperty(value = "voteInfoTitle")
  @NotBlank(message = "투표이름이 존재하지 않습니다.")
  String voteInfoTitle;

  @JsonProperty(value = "voteInfoDesc")
  String voteInfoDesc;

  @JsonProperty(value = "vote")
  @NotEmpty(message = "투표가 존재하지 않습니다.")
  @Size(min = 1, message = "투표가 존재하지 않습니다.")
  @Valid
  List<VoteDto> voteDto;

  public VoteInfomation toVoteInfomation(Apply apply) {

    return VoteInfomation.builder()
        .apply(apply)
        .description(voteInfoDesc.trim())
        .name(voteInfoTitle.trim())
        .status(VoteInfoStatusType.NORMAL)
        .build();
  }
}
