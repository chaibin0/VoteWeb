package com.vote.cb.apply;

import com.vote.cb.apply.controller.dto.ApplyRequestDto;

import java.time.LocalDateTime;

public interface StaticApply {

  public static LocalDateTime START_DATE_TIME = LocalDateTime.now().plusDays(1);

  public static LocalDateTime END_DATE_TIME = LocalDateTime.now().plusDays(2);

  public static ApplyRequestDto APPLY_DTO = ApplyRequestDto.builder()
      .name("홍길동")
      .email("abc@naver.com")
      .phone("01000000000")
      .title("투표테스트")
      .expectedCount(5)
      .start(START_DATE_TIME)
      .end(END_DATE_TIME)
      .build();


}
