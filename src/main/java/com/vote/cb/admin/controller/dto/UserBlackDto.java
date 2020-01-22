package com.vote.cb.admin.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserBlackDto {

  @NotBlank
  @JsonProperty("id")
  String id;

  @NotNull
  @JsonProperty("black")
  boolean isBlack;
}
