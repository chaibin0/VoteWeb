package com.vote.cb.exception;


public class VoteInfoNotFoundException extends CustomException {

  private static final long serialVersionUID = 1992364707243373076L;

  @Override
  public String getMessage() {

    return "투표정보를 찾을 수 없습니다.";
  }


}
