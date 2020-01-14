package com.vote.cb.apply.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vote.cb.apply.domain.Apply;
import com.vote.cb.apply.domain.Voter;
import com.vote.cb.apply.domain.enums.VoterStatusType;

import java.util.UUID;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VoterDto {

  @JsonProperty(value = "voterId")
  Long voterId;

  @JsonProperty(value = "voterName")
  @NotBlank(message = "유권자 이름을 다시 입력하세요")
  String voterName;

  @JsonProperty(value = "voterPhone")
  @NotBlank(message = "유권자 번호를 다시 입력하세요.")
  @Pattern(regexp = "(\\d{3})(\\d{3,4})(\\d{4})", message = "휴대전화 번호를 제대로 입력하세요")
  String voterPhone;

  public Voter toVoter(Apply apply) {

    return Voter.builder()
        .name(voterName.trim())
        .phone(voterPhone)
        .apply(apply)
        .ssn(UUID.randomUUID().toString().replace("-", ""))
        .status(VoterStatusType.UNVOTED)
        .build();
  }

  public Voter modifyByDto(Voter voter) {

    voter.setName(voterName.trim())
        .setPhone(voterPhone);
    return voter;
  }
}
