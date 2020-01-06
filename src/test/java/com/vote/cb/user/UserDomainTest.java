package com.vote.cb.user;

import static org.assertj.core.api.Assertions.assertThat;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import com.vote.cb.user.domain.Member;
import com.vote.cb.user.domain.enums.UserRole;
import com.vote.cb.user.domain.enums.UserStatusType;


class UserDomainTest {

  @BeforeEach
  void setUp() throws Exception {}

  @DisplayName("User객체 생성 확인 테스트")
  @Test
  void userObjectTest() {

    String id = "test01";
    String password = "test01";
    String name = "test";
    String phone = "01000000000";
    LocalDateTime createdAt = LocalDateTime.now();
    String createdBy = "ADMIN_SERVER";
    UserStatusType status = UserStatusType.NORMAL;
    UserRole role = UserRole.ADMIN;
    Member user = Member.builder()
        .userId(id)
        .password(password)
        .name(name)
        .phone(phone)
        .createdAt(createdAt)
        .createdBy(createdBy)
        .status(status)
        .role(role)
        .build();

    assertThat(user.getUserId()).isEqualTo(id);
    assertThat(user.getPassword()).isEqualTo(password);
    assertThat(user.getName()).isEqualTo(name);
    assertThat(user.getPhone()).isEqualTo(phone);
  }



}
