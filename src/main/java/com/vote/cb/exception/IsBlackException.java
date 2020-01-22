package com.vote.cb.exception;


public class IsBlackException extends CustomException {

  private static final long serialVersionUID = 1953289211321809331L;

  @Override
  public String getMessage() {

    return "당신은 Block당했습니다.";
  }
}
