package com.vote.cb.voter;

import static org.assertj.core.api.Assertions.assertThat;

import com.vote.cb.apply.controller.dto.ApplyRequestDto;
import com.vote.cb.apply.controller.dto.VoterDto;
import com.vote.cb.apply.domain.Apply;
import com.vote.cb.apply.domain.ApplyRepository;
import com.vote.cb.apply.domain.Voter;
import com.vote.cb.apply.domain.VoterRepository;
import com.vote.cb.apply.domain.enums.ApplyStatusType;
import com.vote.cb.apply.domain.enums.VoterStatusType;
import com.vote.cb.exception.ApplyNotFoundException;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
class VoterJpaTest {


  @Autowired
  ApplyRepository applyRepository;

  @Autowired
  VoterRepository voterRepository;

  LocalDateTime now;

  LocalDateTime update;

  Apply apply;

  @BeforeEach
  void setUp() throws Exception {

    now = LocalDateTime.of(2019, Month.NOVEMBER, 22, 10, 30, 30, 0);
    update = LocalDateTime.of(2019, Month.NOVEMBER, 22, 10, 30, 30, 0);

    ApplyRequestDto dto = ApplyRequestDto.builder()
        .name("홍길동")
        .email("abc@naver.com")
        .phone("01054136068")
        .title("투표테스트")
        .expectedCount(5)
        .start(now)
        .end(update)
        .build();

    apply = Apply.builder()
        .name(dto.getName())
        .email(dto.getEmail())
        .phone(dto.getPhone())
        .title(dto.getTitle())
        .expectedCount(dto.getExpectedCount())
        .start(dto.getStart())
        .end(dto.getEnd())
        .status(ApplyStatusType.REGISTERED)
        .createdAt(LocalDateTime.now())
        .createdBy("ADMIN_SERVER")
        .build();
    
    applyRepository.save(apply);

  }

  @Test
  @DisplayName("유권자 등록 테스트")
  void createTest() throws Exception {

    long applyId = apply.getId();
    VoterDto dto = VoterDto.builder().voterName("홍길동").voterPhone("01000000000").build();
    Apply findApply = applyRepository.findById(applyId).orElseThrow(ApplyNotFoundException::new);

    Voter voter = Voter.builder()
        .name(dto.getVoterName())
        .phone(dto.getVoterPhone())
        .apply(findApply)
        .ssn(UUID.randomUUID().toString().replace("-", ""))
        .status(VoterStatusType.UNVOTED)
        .createdAt(LocalDateTime.now())
        .createdBy("ADMIN_SERVER")
        .build();
    Voter savedVoter = voterRepository.save(voter);

    assertThat(savedVoter.getName()).isEqualTo("홍길동");
    assertThat(savedVoter.getPhone()).isEqualTo("01000000000");
  }

  @Test
  @DisplayName("유권자 리스트 조회 테스트")
  void readListTest() throws Exception {

    long applyId = apply.getId();

    VoterDto dto1 = VoterDto.builder().voterName("홍길동").voterPhone("01000000000").build();
    VoterDto dto2 = VoterDto.builder().voterName("홍길동2").voterPhone("01000000001").build();

    Apply findApply = applyRepository.findById(applyId).orElseThrow(ApplyNotFoundException::new);

    Voter voter1 = Voter.builder()
        .name(dto1.getVoterName())
        .phone(dto1.getVoterPhone())
        .apply(findApply)
        .ssn(UUID.randomUUID().toString().replace("-", ""))
        .status(VoterStatusType.UNVOTED)
        .createdAt(LocalDateTime.now())
        .createdBy("ADMIN_SERVER")
        .build();

    Voter voter2 = Voter.builder()
        .name(dto2.getVoterName())
        .phone(dto2.getVoterPhone())
        .apply(findApply)
        .ssn(UUID.randomUUID().toString().replace("-", ""))
        .status(VoterStatusType.UNVOTED)
        .createdAt(LocalDateTime.now())
        .createdBy("ADMIN_SERVER")
        .build();

    voterRepository.save(voter1);
    voterRepository.save(voter2);


    List<Voter> voterList = voterRepository.findAllByApply(apply);

    assertThat(2).isEqualTo(voterList.size());
    assertThat(voterList.get(0).getName()).isEqualTo("홍길동");
    assertThat(voterList.get(0).getPhone()).isEqualTo("01000000000");

    assertThat(voterList.get(1).getName()).isEqualTo("홍길동2");
    assertThat(voterList.get(1).getPhone()).isEqualTo("01000000001");

  }



}
