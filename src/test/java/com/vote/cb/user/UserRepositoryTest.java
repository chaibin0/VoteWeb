package com.vote.cb.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.transaction.annotation.Transactional;
import com.vote.cb.apply.domain.Apply;
import com.vote.cb.apply.domain.ApplyRepository;
import com.vote.cb.apply.domain.enums.ApplyStatusType;
import com.vote.cb.exception.MemberNotFoundException;
import com.vote.cb.user.controller.dto.SignUpDto;
import com.vote.cb.user.domain.Member;
import com.vote.cb.user.domain.MemberRepository;
import com.vote.cb.user.domain.enums.UserRole;
import com.vote.cb.user.domain.enums.UserStatusType;
import com.vote.cb.vote.domain.Candidate;
import com.vote.cb.vote.domain.CandidateRepository;
import com.vote.cb.vote.domain.Vote;
import com.vote.cb.vote.domain.VoteInfoRepository;
import com.vote.cb.vote.domain.VoteInfomation;
import com.vote.cb.vote.domain.VoteRepository;
import com.vote.cb.vote.domain.enums.VoteInfoStatusType;


@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
class UserRepositoryTest {

  SignUpDto signUpDto;

  @Autowired
  MemberRepository userRepository;

  @Autowired
  VoteInfoRepository voteInfoRepository;

  @Autowired
  VoteRepository voteRepository;

  @Autowired
  CandidateRepository candidateRepository;

  @Autowired
  ApplyRepository applyRepository;

  @BeforeEach
  void setUp() throws Exception {

    signUpDto = new SignUpDto();
    signUpDto.setId("test01");
    signUpDto.setPassword("test01");
    signUpDto.setPhone("01000000000");
    signUpDto.setName("테스트");
  }

  @DisplayName("유저 생성 확인 테스트")
  @Test
  @Transactional
  void createUserTest() {

    String id = "test01";
    String password = "test01";
    String name = "test";
    String phone = "01000000000";
    LocalDateTime createdAt = LocalDateTime.now();
    String createdBy = "ADMIN_SERVER";
    UserStatusType status = UserStatusType.NORMAL;
    UserRole role = UserRole.USER;

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
    Member newUser = userRepository.save(user);

    assertThat(newUser.getUserId()).isEqualTo(id);
    assertThat(newUser.getPassword()).isEqualTo(password);
    assertThat(newUser.getName()).isEqualTo(name);
    assertThat(newUser.getPhone()).isEqualTo(phone);
    assertThat(newUser.getRole()).isEqualTo(role);
    assertThat(newUser.getStatus()).isEqualTo(status);
  }

  @DisplayName("Dto를 이용한 User 유저 생성 테스트")
  @Test
  @Transactional
  void createUserByDtoTest() {

    Member user = Member.builder()
        .userId(signUpDto.getId())
        .password(signUpDto.getPassword())
        .name(signUpDto.getName())
        .phone(signUpDto.getPhone())
        .createdAt(LocalDateTime.now())
        .createdBy("ADMIN_SERVER")
        .status(UserStatusType.NORMAL)
        .role(UserRole.USER)
        .build();

    Member newUser = userRepository.save(user);

    assertThat(newUser.getUserId()).isEqualTo(user.getUserId());
    assertThat(newUser.getPassword()).isEqualTo(user.getPassword());
    assertThat(newUser.getName()).isEqualTo(user.getName());
    assertThat(newUser.getPhone()).isEqualTo(user.getPhone());
    assertThat(newUser.getRole()).isEqualTo(user.getRole());
    assertThat(newUser.getStatus()).isEqualTo(user.getStatus());
  }

  @DisplayName("유저 삭제 테스트")
  @Test
  void deleteUserTest() {

    Member user = Member.builder()
        .userId(signUpDto.getId())
        .password(signUpDto.getPassword())
        .name(signUpDto.getName())
        .phone(signUpDto.getPhone())
        .createdAt(LocalDateTime.now())
        .createdBy("ADMIN_SERVER")
        .status(UserStatusType.NORMAL)
        .role(UserRole.USER)
        .build();

    userRepository.save(user);

    userRepository.deleteById(signUpDto.getId());

    assertThatThrownBy(() -> {
      userRepository.findById(user.getUserId()).orElseThrow(MemberNotFoundException::new);
    }).isInstanceOf(MemberNotFoundException.class);

  }
}
