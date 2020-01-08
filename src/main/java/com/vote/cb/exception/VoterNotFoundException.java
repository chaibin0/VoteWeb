package com.vote.cb.exception;


public class VoterNotFoundException extends RuntimeException {

  private static final long serialVersionUID = -8001110617321046806L;

  @Override
  public String getMessage() {

    return "유권자 정보를 찾지 못했습니다.";
  }


}
