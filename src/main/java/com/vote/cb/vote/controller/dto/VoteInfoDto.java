package com.vote.cb.vote.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

import javax.validation.constraints.Min;
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

  // 나중에 투표 제한 할것
  @JsonProperty(value = "voteInfoCount")
  @NotNull(message = "투표 데이터가 정상적이지 않습니다.")
  @NumberFormat(style = Style.NUMBER)
  @Min(1)
  Integer voteInfoCount;

  @JsonProperty(value = "vote")
  @NotEmpty(message = "투표가 존재하지 않습니다.")
  @Size(min = 1, message = "투표가 존재하지 않습니다.")
  List<VoteDto> voteDto;
}
