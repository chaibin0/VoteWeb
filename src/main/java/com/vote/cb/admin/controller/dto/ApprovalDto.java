package com.vote.cb.admin.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ApprovalDto {

  @JsonProperty("id")
  @NotNull(message = "id값을 입력하세요")
  Long id;
}
