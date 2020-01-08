package com.vote.cb.apply.domain.enums;


public enum ApplyStatusType {
  REGISTERED("등록"),
  REJECTED("거절"),
  APRROVAL("승인"),
  VOTING("투표중"),
  FINISHED("종료"),
  COUNTED("개표");

  private String status;

  private ApplyStatusType(String status) {

    this.status = status;
  }

  public String getStatus(ApplyStatusType statusType) {

    return statusType.status;
  }
}
