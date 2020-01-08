package com.vote.cb.apply.controller.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.vote.cb.apply.domain.Apply;
import java.time.LocalDateTime;
import javax.validation.constraints.Email;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApplyResponseDto {

  @JsonProperty("id")
  private Long id;

  @JsonProperty("name")
  @NotBlank(message = "이름을 입력해주세요")
  private String name;

  @JsonProperty("email")
  @NotBlank(message = "이메일을 입력해주세요")
  @Email(message = "이메일 양식을 지켜주세요")
  private String email;

  @JsonProperty("phone")
  @NotBlank(message = "휴대전화를 입력해주세요")
  @Pattern(regexp = "(\\d{3})(\\d{3,4})(\\d{4})", message = "휴대전화 번호를 제대로 입력하세요")
  private String phone;

  @JsonProperty("voteTitle")
  @NotBlank(message = "제목을 입력하세요")
  private String title;

  @JsonProperty("expectedCount")
  private int expectedCount;

  @JsonProperty("status")
  public String status;

  @JsonProperty("startVote")
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss",
      timezone = "Asia/Seoul")
  @Future(message = "오늘이전의 값은 사용할 수 없습니다.")
  private LocalDateTime start;

  @JsonProperty("endVote")
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss",
      timezone = "Asia/Seoul")
  @Future(message = "오늘이전의 값은 사용할 수 없습니다.")
  private LocalDateTime end;

  @JsonProperty("voted")
  private boolean voted;

  public static ApplyResponseDto of(Apply apply) {

    return ApplyResponseDto.builder()
        .id(apply.getId())
        .name(apply.getName())
        .email(apply.getEmail())
        .phone(apply.getPhone())
        .title(apply.getTitle())
        .expectedCount(apply.getExpectedCount())
        .start(apply.getStart())
        .end(apply.getEnd())
        .status(apply.getStatus().toString())
        .voted(apply.isVoted())
        .build();
  }
}
