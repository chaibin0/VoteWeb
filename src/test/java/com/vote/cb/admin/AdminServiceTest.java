package com.vote.cb.admin;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.vote.cb.admin.service.AdminService;
import com.vote.cb.admin.service.AdminServiceImpl;
import com.vote.cb.apply.domain.Apply;
import com.vote.cb.apply.domain.ApplyRepository;
import com.vote.cb.apply.domain.enums.ApplyStatusType;
import com.vote.cb.user.domain.Member;
import com.vote.cb.user.domain.MemberRepository;
import com.vote.cb.user.domain.enums.UserRole;
import com.vote.cb.user.domain.enums.UserStatusType;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

@ExtendWith(MockitoExtension.class)
class AdminServiceTest {

  private LocalDateTime start;

  private LocalDateTime end;

  private Member member;

  private User user;

  @Mock
  ApplyRepository applyRepository;

  @Mock
  MemberRepository userRepository;

  @InjectMocks
  AdminService adminService = new AdminServiceImpl(userRepository, applyRepository);

  @AfterAll
  static void tearDownAfterClass() throws Exception {}

  @BeforeEach
  void setUp() throws Exception {

    start = LocalDateTime.of(2020, Month.JANUARY, 13, 12, 30);
    end = LocalDateTime.of(2020, Month.JANUARY, 14, 12, 30);
    Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
    grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_USER"));

    user = new User("test", "test", grantedAuthorities);
    member = Member.builder()
        .userId("test")
        .password("test")
        .name("이름")
        .phone("01000000000")
        .createdAt(LocalDateTime.now())
        .createdBy("TEST_SERVER")
        .status(UserStatusType.NORMAL)
        .role(UserRole.USER)
        .build();

  }

  @AfterEach
  void tearDown() throws Exception {}

  @Test
  void testApprovalApply() {

    Apply apply = Apply.builder()
        .id(1L)
        .name("이름")
        .email("abc@naver.com")
        .phone("01000000000")
        .title("첫번째")
        .expectedCount(1)
        .start(start)
        .end(end)
        .createdAt(LocalDateTime.now())
        .createdBy("TEST_SERVER")
        .status(ApplyStatusType.REGISTERED)
        .user(member)
        .hasVote(true)
        .voted(false)
        .build();

    Apply savedapply = Apply.builder()
        .id(1L)
        .name("이름")
        .email("abc@naver.com")
        .phone("01000000000")
        .title("첫번째")
        .expectedCount(1)
        .start(start)
        .end(end)
        .createdAt(LocalDateTime.now())
        .createdBy("TEST_SERVER")
        .status(ApplyStatusType.REGISTERED)
        .user(member)
        .hasVote(true)
        .voted(false)
        .approval(1)
        .build();

    Long applyId = 1L;

    when(applyRepository.findById(applyId)).thenReturn(Optional.of(apply));
    when(applyRepository.save(Mockito.any(Apply.class))).thenReturn(savedapply);

    ResponseEntity<?> responseEntity = adminService.approvalApply(applyId);
    verify(applyRepository, times(1)).save(Mockito.any(Apply.class));
    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.ACCEPTED);
  }

  @Test
  void testRejectApply() {

    Apply apply = Apply.builder()
        .id(1L)
        .name("이름")
        .email("abc@naver.com")
        .phone("01000000000")
        .title("첫번째")
        .expectedCount(1)
        .start(start)
        .end(end)
        .createdAt(LocalDateTime.now())
        .createdBy("TEST_SERVER")
        .status(ApplyStatusType.REGISTERED)
        .user(member)
        .hasVote(true)
        .voted(false)
        .build();

    Apply savedapply = Apply.builder()
        .id(1L)
        .name("이름")
        .email("abc@naver.com")
        .phone("01000000000")
        .title("첫번째")
        .expectedCount(1)
        .start(start)
        .end(end)
        .createdAt(LocalDateTime.now())
        .createdBy("TEST_SERVER")
        .status(ApplyStatusType.REGISTERED)
        .user(member)
        .hasVote(true)
        .voted(false)
        .approval(0)
        .build();

    Long applyId = 1L;

    when(applyRepository.findById(applyId)).thenReturn(Optional.of(apply));
    when(applyRepository.save(Mockito.any(Apply.class))).thenReturn(savedapply);

    ResponseEntity<?> responseEntity = adminService.approvalApply(applyId);
    verify(applyRepository, times(1)).save(Mockito.any(Apply.class));
    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.ACCEPTED);
  }

}
