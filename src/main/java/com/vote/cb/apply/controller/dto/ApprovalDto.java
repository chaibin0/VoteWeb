package com.vote.cb.apply.controller.dto;

import javax.validation.constraints.NotBlank;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ApprovalDto {

  @JsonProperty("id")
  @NotBlank(message = "id값을 입력하세요")
  Long id;
}
