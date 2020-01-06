package com.vote.cb.exception;


public class CandidateNotFoundException extends RuntimeException {

  private static final long serialVersionUID = -5870560278767800719L;

  @Override
  public String getMessage() {

    return "후보자 정보를 제대로 찾지 못했습니다.";
  }

  
}
