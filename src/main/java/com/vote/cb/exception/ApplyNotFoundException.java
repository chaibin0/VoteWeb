package com.vote.cb.exception;


public class ApplyNotFoundException extends RuntimeException {

  private static final long serialVersionUID = 1927089834740782209L;

  @Override
  public String getMessage() {

    return "이용신청서가 존재하지 않습니다.";
  }


}
