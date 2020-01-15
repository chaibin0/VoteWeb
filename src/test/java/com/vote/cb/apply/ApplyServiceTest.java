package com.vote.cb.apply;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.vote.cb.apply.controller.dto.ApplyRequestDto;
import com.vote.cb.apply.controller.dto.ApplyResponseDto;
import com.vote.cb.apply.domain.Apply;
import com.vote.cb.apply.domain.ApplyRepository;
import com.vote.cb.apply.domain.enums.ApplyStatusType;
import com.vote.cb.apply.service.ApplyService;
import com.vote.cb.apply.service.ApplyServiceImpl;
import com.vote.cb.user.domain.Member;
import com.vote.cb.user.domain.MemberRepository;
import com.vote.cb.user.domain.enums.UserRole;
import com.vote.cb.user.domain.enums.UserStatusType;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

@ExtendWith(MockitoExtension.class)
class ApplyServiceTest {

  @Mock
  ApplyRepository applyRepository;

  @Mock
  MemberRepository memberRepository;

  @InjectMocks
  ApplyService applyService = new ApplyServiceImpl(applyRepository, memberRepository);

  private LocalDateTime start;

  private LocalDateTime end;

  private User user;

  private Member member;

  private Apply apply;

  private Apply apply1;

  private Apply apply2;

  private ApplyRequestDto dto;

  private int PAGE_NUMBER;

  private int PAGE_SIZE;

  @BeforeEach
  void setUp() throws Exception {

    // MockitoAnnotations.initMocks(this);

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

  @DisplayName("getApply() Test")
  @Test
  void testGetApply() {

    apply = Apply.builder()
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
        .build();

    Long applyId = 1L;

    when(applyRepository.findById(Mockito.eq(1L))).thenReturn(Optional.of(apply));

    ApplyResponseDto responseDto = applyService.getApply(user, applyId);
    verify(applyRepository, times(1)).findById(applyId);

    assertAll(
        () -> assertThat(responseDto.getName()).isEqualTo("이름"),
        () -> assertThat(responseDto.getEmail()).isEqualTo("abc@naver.com"),
        () -> assertThat(responseDto.getPhone()).isEqualTo("01000000000"),
        () -> assertThat(responseDto.getTitle()).isEqualTo("첫번째"));
  }

  @DisplayName("getApplyList() Test")
  @Test
  void testGetApplyList() {

    apply1 = Apply.builder()
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
        .build();

    apply2 = Apply.builder()
        .id(2L)
        .name("이름")
        .email("abc@naver.com")
        .phone("01000000000")
        .title("두번째")
        .expectedCount(2)
        .start(start)
        .end(end)
        .createdAt(LocalDateTime.now())
        .createdBy("TEST_SERVER")
        .status(ApplyStatusType.REGISTERED)
        .user(member)
        .build();

    List<Apply> applyList = new ArrayList<>();
    applyList.add(apply1);
    applyList.add(apply2);

    PAGE_NUMBER = 0;
    PAGE_SIZE = 10;

    Pageable pageable = PageRequest.of(PAGE_NUMBER, PAGE_SIZE);
    Page<Apply> pagedApplyList = new PageImpl<Apply>(applyList, pageable, applyList.size());


    when(applyRepository.findAll(Mockito.any(Pageable.class))).thenReturn(pagedApplyList);
    Page<Apply> list = applyService.getApplyAllList(pageable);


    assertAll(
        () -> assertThat(list.getContent().get(0).getTitle()).isEqualTo("첫번째"),
        () -> assertThat(list.getContent().get(1).getTitle()).isEqualTo("두번째"),
        () -> assertThat(list.getNumber()).isEqualTo(PAGE_NUMBER),
        () -> assertThat(list.getSize()).isEqualTo(PAGE_SIZE));
  }

  @DisplayName("RegisterApply() Test")
  @Test
  void testRegisterApply() {

    dto = ApplyRequestDto.builder()
        .name("가나다")
        .email("abc@naver.com")
        .phone("01000000000")
        .title("테스트투표")
        .expectedCount(10)
        .start(LocalDateTime.now().plusDays(10))
        .end(LocalDateTime.now().plusDays(11))
        .build();

    Apply savedApply = Apply.builder()
        .id(1L)
        .name(dto.getName())
        .email(dto.getEmail())
        .phone(dto.getPhone())
        .title(dto.getTitle())
        .expectedCount(dto.getExpectedCount())
        .start(dto.getStart())
        .end(dto.getEnd())
        .createdAt(LocalDateTime.now())
        .createdBy("TEST_SERVER")
        .status(ApplyStatusType.REGISTERED)
        .user(member)
        .build();

    when(memberRepository.findById(Mockito.eq(user.getUsername()))).thenReturn(Optional.of(member));
    when(applyRepository.save(Mockito.any(Apply.class))).thenReturn(savedApply);

    ResponseEntity<?> responseEntity = applyService.registerApply(user, dto);
    verify(applyRepository).save(Mockito.any(Apply.class));

    Apply responsedApply = (Apply) responseEntity.getBody();
    assertAll(
        () -> assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED),
        () -> assertThat(responsedApply.getName()).isEqualTo(dto.getName()),
        () -> assertThat(responsedApply.getEmail()).isEqualTo(dto.getEmail()),
        () -> assertThat(responsedApply.getPhone()).isEqualTo(dto.getPhone()),
        () -> assertThat(responsedApply.getTitle()).isEqualTo(dto.getTitle()));
  }

  @DisplayName("GetApplyAllList() 사용자용")
  @Test
  void testGetApplyAllListPageableUser() {

    apply1 = Apply.builder()
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
        .build();

    apply2 = Apply.builder()
        .id(2L)
        .name("이름")
        .email("abc@naver.com")
        .phone("01000000000")
        .title("두번째")
        .expectedCount(2)
        .start(start)
        .end(end)
        .createdAt(LocalDateTime.now())
        .createdBy("TEST_SERVER")
        .status(ApplyStatusType.REGISTERED)
        .user(member)
        .build();

    PAGE_NUMBER = 0;
    PAGE_SIZE = 10;

    PageRequest pageable = PageRequest.of(PAGE_NUMBER, PAGE_SIZE);
    List<Apply> applyList = new ArrayList<>(Arrays.asList(apply1, apply2));
    Page<Apply> pagedApply = new PageImpl<Apply>(applyList, pageable, 2);

    when(memberRepository.findById(Mockito.eq(user.getUsername()))).thenReturn(Optional.of(member));
    when(applyRepository.findAllByUser(Mockito.any(pageable.getClass()), Mockito.eq(member)))
        .thenReturn(pagedApply);

    Page<Apply> applyPagedList = applyService.getApplyAllList(pageable, user);

    verify(memberRepository, times(1)).findById(user.getUsername());
    verify(applyRepository, times(1)).findAllByUser(pageable, member);

    assertAll(
        () -> assertThat(applyPagedList.getSize()).isEqualTo(PAGE_SIZE),
        () -> assertThat(applyPagedList.getNumber()).isEqualTo(PAGE_NUMBER),
        () -> assertThat(applyPagedList.getContent().get(0).getName()).isEqualTo(apply1.getName()),
        () -> assertThat(applyPagedList.getContent().get(1).getName()).isEqualTo(apply2.getName()));
  }

  @DisplayName("GetApplyAllList() 관리자용")
  @Test
  void testGetApplyAllListPageable() {

    PAGE_NUMBER = 0;
    PAGE_SIZE = 10;

    apply1 = Apply.builder()
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
        .build();

    apply2 = Apply.builder()
        .id(2L)
        .name("이름")
        .email("abc@naver.com")
        .phone("01000000000")
        .title("두번째")
        .expectedCount(2)
        .start(start)
        .end(end)
        .createdAt(LocalDateTime.now())
        .createdBy("TEST_SERVER")
        .status(ApplyStatusType.REGISTERED)
        .user(member)
        .build();

    PageRequest pageable = PageRequest.of(PAGE_NUMBER, PAGE_SIZE);
    List<Apply> applyList = new ArrayList<>(Arrays.asList(apply1, apply2));
    Page<Apply> pagedApply = new PageImpl<Apply>(applyList, pageable, 2);

    when(applyRepository.findAll(Mockito.eq(pageable)))
        .thenReturn(pagedApply);

    Page<Apply> applyPagedList = applyService.getApplyAllList(pageable);
    verify(applyRepository, times(1)).findAll(pageable);

    assertAll(
        () -> assertThat(applyPagedList.getSize()).isEqualTo(PAGE_SIZE),
        () -> assertThat(applyPagedList.getNumber()).isEqualTo(PAGE_NUMBER),
        () -> assertThat(applyPagedList.getContent().get(0).getName()).isEqualTo(apply1.getName()),
        () -> assertThat(applyPagedList.getContent().get(1).getName()).isEqualTo(apply2.getName()));
  }

  @DisplayName("removeApply() test")
  @Test
  void testRemoveApply() {

    apply = Apply.builder()
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
        .build();

    when(applyRepository.findById(Mockito.eq(apply.getId()))).thenReturn(Optional.of(apply));

    ResponseEntity<?> response = applyService.removeApply(user, apply.getId());

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.ACCEPTED);
  }

  @DisplayName("modifyApply() test")
  @Test
  void testModifyApply() {

    apply = Apply.builder()
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
        .build();

    dto = ApplyRequestDto.builder()
        .id(1L)
        .name("가나다")
        .email("abc@naver.com")
        .phone("01000000000")
        .title("테스트투표")
        .expectedCount(10)
        .start(LocalDateTime.now().plusDays(10))
        .end(LocalDateTime.now().plusDays(11))
        .build();

    Apply modifiedApply = Apply.builder()
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
        .build();

    modifiedApply.modify(dto);

    when(applyRepository.findById(dto.getId())).thenReturn(Optional.of(apply));
    when(applyRepository.save(Mockito.any(Apply.class))).thenReturn(modifiedApply);
    ResponseEntity<?> responseEntity = applyService.modifyApply(user, dto);
    Apply responseApply = (Apply) responseEntity.getBody();

    assertAll(
        () -> assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK),
        () -> assertThat(responseApply.getName()).isEqualTo(dto.getName()),
        () -> assertThat(responseApply.getTitle()).isEqualTo(dto.getTitle()));
  }

  @DisplayName("alreadyStart() Test")
  @Test
  void testAlreadyStart() {

    apply = Apply.builder()
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
        .voted(true)
        .build();

    Long applyId = 1L;

    when(applyRepository.findById(applyId)).thenReturn(Optional.of(apply));

    boolean isAlreadyStart = applyService.alreadyStart(user, applyId);

    assertThat(isAlreadyStart).isEqualTo(true);
  }

  @DisplayName("hasVote() test")
  @Test
  void testHasVote() {

    apply = Apply.builder()
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

    Long applyId = 1L;

    when(applyRepository.findById(applyId)).thenReturn(Optional.of(apply));

    boolean isAlreadyStart = applyService.hasVote(user, applyId);

    assertThat(isAlreadyStart).isEqualTo(true);
  }

  @DisplayName("approvalApply() test")
  @Test
  void testApprovalApply() {


  }

  @DisplayName("rejectApply() test")
  @Test
  void testRejectApply() {

  }

}
