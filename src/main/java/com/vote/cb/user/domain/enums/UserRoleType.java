package com.vote.cb.user.domain.enums;


public enum UserRoleType {

  USER("ROLE_USER"),
  ADMIN("ROLE_ADMIN");

  private String roleName;

  UserRoleType(String roleName) {

    this.roleName = roleName;
  }

  public String getRoleName() {

    return roleName;
  }


}
