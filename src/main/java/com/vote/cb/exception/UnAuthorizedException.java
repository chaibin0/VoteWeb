package com.vote.cb.exception;


public class UnAuthorizedException extends RuntimeException {

  private static final long serialVersionUID = -1122451889527063996L;

  @Override
  public String getMessage() {

    return "허가되지 않은 접근입니다.";
  }
  
  
}
