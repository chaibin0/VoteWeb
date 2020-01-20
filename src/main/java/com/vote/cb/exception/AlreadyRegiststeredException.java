package com.vote.cb.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AlreadyRegiststeredException extends CustomException {

  private static final long serialVersionUID = -5989570373875648530L;


  public AlreadyRegiststeredException() {

  }

  @Override
  public String getMessage() {

    return "이미 등록되어있습니다.";
  }

}
