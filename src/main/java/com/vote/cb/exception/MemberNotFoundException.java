package com.vote.cb.exception;


public class MemberNotFoundException extends RuntimeException {

  private static final long serialVersionUID = 163374328415726755L;

  @Override
  public String getMessage() {

    return "개인정보를 찾지 못했습니다.";
  }
}
