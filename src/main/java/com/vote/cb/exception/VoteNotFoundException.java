package com.vote.cb.exception;


public class VoteNotFoundException extends CustomException {

  private static final long serialVersionUID = 9206041548712877694L;

  @Override
  public String getMessage() {

    return "투표정보를 찾을 수 없습니다.";
  }

}
