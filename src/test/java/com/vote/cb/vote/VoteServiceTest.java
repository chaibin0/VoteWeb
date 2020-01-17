package com.vote.cb.vote;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.when;

import com.vote.cb.apply.domain.Apply;
import com.vote.cb.apply.domain.ApplyRepository;
import com.vote.cb.apply.domain.Voter;
import com.vote.cb.apply.domain.VoterRepository;
import com.vote.cb.apply.domain.enums.ApplyStatusType;
import com.vote.cb.apply.domain.enums.VoterStatusType;
import com.vote.cb.user.domain.Member;
import com.vote.cb.user.domain.enums.UserRoleType;
import com.vote.cb.user.domain.enums.UserStatusType;
import com.vote.cb.vote.controller.dto.VoteInfoDto;
import com.vote.cb.vote.controller.dto.VoteSignDto;
import com.vote.cb.vote.domain.CandidateRepository;
import com.vote.cb.vote.domain.ResultRepository;
import com.vote.cb.vote.domain.VoteInfoRepository;
import com.vote.cb.vote.domain.VoteInfomation;
import com.vote.cb.vote.domain.VoteRepository;
import com.vote.cb.vote.domain.enums.VoteInfoStatusType;
import com.vote.cb.vote.service.VoteService;
import com.vote.cb.vote.service.VoteServiceImpl;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
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
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

@ExtendWith(MockitoExtension.class)
class VoteServiceTest {

  @Mock
  private VoteRepository voteRepository;

  @Mock
  private ApplyRepository applyRepository;

  @Mock
  private VoteInfoRepository voteInfoRepository;

  @Mock
  private CandidateRepository candidateRepository;

  @Mock
  private VoterRepository voterRepository;

  @Mock
  private ResultRepository resultRepository;

  @Mock
  MockHttpSession httpSession;

  @InjectMocks
  VoteService voteService = new VoteServiceImpl(voteRepository, applyRepository, voteInfoRepository,
      candidateRepository, voterRepository, resultRepository);

  private Apply apply;

  private Member member;

  private User user;

  private LocalDateTime start;

  private LocalDateTime end;

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
        .build();

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
  }

  @AfterEach
  void tearDown() throws Exception {}

  @Test
  void testGetVoteList() throws Exception {

    String name = "테스트이름";
    String phone = "01000000000";
    String uid = "uid";

    VoteInfomation voteInfo = VoteInfomation.builder()
        .apply(apply)
        .description("설명")
        .name("투표이름")
        .status(VoteInfoStatusType.NORMAL)
        .createdAt(LocalDateTime.now())
        .createdBy("ADMIN_SERVER")
        .build();

    Voter voter = Voter.builder()
        .id(1L)
        .name("테스트이름")
        .phone("01000000000")
        .ssn("uid")
        .apply(apply)
        .status(VoterStatusType.UNVOTED)
        .build();

    when(voterRepository.findByNameAndPhoneAndSsn(Mockito.eq(name), Mockito.eq(phone),
        Mockito.eq(uid))).thenReturn(Optional.of(voter));

    when(voteInfoRepository.findByApply(Mockito.any(voter.getApply().getClass())))
        .thenReturn(Optional.of(voteInfo));

    VoteInfomation voteInfoByService = voteService.getVoteList(name, phone, uid);

    assertAll(
        () -> assertThat(voteInfoByService.getName()).isEqualTo(voteInfo.getName()),
        () -> assertThat(voteInfoByService.getApply().getName()).isEqualTo(apply.getName()));
  }

  @Test
  void testSaveVoteInfo() {

    VoteInfomation voteInfo = VoteInfomation.builder()
        .id(1L)
        .apply(apply)
        .description("설명")
        .name("투표이름")
        .status(VoteInfoStatusType.NORMAL)
        .createdAt(LocalDateTime.now())
        .createdBy("ADMIN_SERVER")
        .build();

    VoteInfoDto dto = VoteInfoDto.builder()
        .voteInfoTitle("투표이름")
        .voteInfoDesc("설명")
        .voteDto(new ArrayList<>())
        .build();

    Apply savedApply = Apply.builder()
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
        .hasVote(true)
        .user(member)
        .build();


    when(applyRepository.findById(Mockito.eq(dto.getApplyId()))).thenReturn(Optional.of(apply));
    when(voteInfoRepository.save(Mockito.any(VoteInfomation.class))).thenReturn(voteInfo);
    when(applyRepository.save(Mockito.any(Apply.class))).thenReturn(savedApply);

    ResponseEntity<VoteInfomation> responseEntity = voteService.saveVoteInfo(user, dto);
    VoteInfomation entity = responseEntity.getBody();
    assertAll(
        () -> assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED),
        () -> assertThat(entity.getName()).isEqualTo(voteInfo.getName()),
        () -> assertThat(entity.getApply().isHasVote()).isEqualTo(savedApply.isHasVote()));
  }

  @Test
  void testAuthVoterInfo() {

    VoteSignDto dto = VoteSignDto.builder()
        .name("이름")
        .phone("01000000000")
        .uid("uid")
        .build();

    Voter voter = Voter.builder()
        .id(1L)
        .name("이름")
        .phone("01000000000")
        .ssn("uid")
        .apply(apply)
        .status(VoterStatusType.UNVOTED)
        .build();

    when(voterRepository.findByNameAndPhoneAndSsn(Mockito.eq(dto.getName()),
        Mockito.eq(dto.getPhone()),
        Mockito.eq(dto.getUid()))).thenReturn(Optional.of(voter));

    ResponseEntity<?> responseEntity = voteService.authVoterInfo(dto, httpSession);

    assertAll(
        () -> assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK),
        () -> assertThat(httpSession.getAttribute("name")).isEqualTo(voter.getName()),
        () -> assertThat(httpSession.getAttribute("phone")).isEqualTo(voter.getName()),
        () -> assertThat(httpSession.getAttribute("uid")).isEqualTo(voter.getSsn()));
  }

}
