package com.vote.cb.apply;

import static org.assertj.core.api.Assertions.assertThat;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import com.vote.cb.apply.controller.dto.ApplyRequestDto;
import com.vote.cb.apply.domain.Apply;
import com.vote.cb.apply.domain.ApplyRepository;
import com.vote.cb.apply.domain.enums.ApplyStatusType;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
class ApplyJpaTest {

  @Autowired
  private ApplyRepository applyRepository;

  LocalDateTime start;

  LocalDateTime end;

  LocalDateTime createdAt;

  String createdBy;

  @BeforeEach
  void setUp() throws Exception {

    createdAt = LocalDateTime.now();
    createdBy = "ADMIN_SERVER";
    start = LocalDateTime.of(2019, 12, 10, 10, 13);
    end = LocalDateTime.of(2019, 12, 12, 10, 13);
  }

  @DisplayName("신청서 등록 확인 테스트")
  @Test
  void registerApplyTest() {

    // given
    ApplyRequestDto dto = ApplyRequestDto.builder()
        .name("홍길동")
        .email("abc@naver.com")
        .phone("01000000000")
        .title("투표테스트")
        .expectedCount(5)
        .start(start)
        .end(end)
        .build();

    // when
    Apply apply = Apply.builder()
        .name(dto.getName())
        .email(dto.getEmail())
        .phone(dto.getPhone())
        .title(dto.getTitle())
        .expectedCount(dto.getExpectedCount())
        .start(dto.getStart())
        .end(dto.getEnd())
        .createdAt(createdAt)
        .createdBy(createdBy)
        .status(ApplyStatusType.REGISTERED)
        .build();

    // then
    Apply saveApply = applyRepository.save(apply);
    Apply findApply = applyRepository.findById(saveApply.getId()).orElse(null);
    assertThat(findApply.getId()).isNotNull();
    assertThat(findApply.getName()).isEqualTo("홍길동");
    assertThat(findApply.getEmail()).isEqualTo("abc@naver.com");
    assertThat(findApply.getPhone()).isEqualTo("01000000000");
    assertThat(findApply.getTitle()).isEqualTo("투표테스트");
    assertThat(findApply.getExpectedCount()).isEqualTo(5);
  }

  @DisplayName("신청서 찾기 테스트")
  @Test
  void findApplyByApplyDtoTest() {

    // given
    ApplyRequestDto dto = ApplyRequestDto.builder()
        .name("홍길동")
        .email("abc@naver.com")
        .phone("01000000000")
        .title("투표테스트")
        .expectedCount(5)
        .start(start)
        .end(end)
        .build();

    Apply apply = Apply.builder()
        .name(dto.getName())
        .email(dto.getEmail())
        .phone(dto.getPhone())
        .title(dto.getTitle())
        .expectedCount(dto.getExpectedCount())
        .start(dto.getStart())
        .end(dto.getEnd())
        .createdAt(createdAt)
        .createdBy(createdBy)
        .status(ApplyStatusType.REGISTERED)
        .build();

    Apply newApply = applyRepository.save(apply);

    // then
    List<Apply> applyList = applyRepository.findAllByName("홍길동");
    assertThat(applyList.isEmpty()).isFalse();
  }

  @DisplayName("신청서 삭제 테스트")
  @Test
  void deleteApplyTest() throws Exception {

    // given
    ApplyRequestDto dto = ApplyRequestDto.builder()
        .name("홍길동")
        .email("abc@naver.com")
        .phone("01054136068")
        .title("투표테스트")
        .expectedCount(5)
        .start(start)
        .end(end)
        .build();
    Apply apply = Apply.builder()
        .name(dto.getName())
        .email(dto.getEmail())
        .phone(dto.getPhone())
        .title(dto.getTitle())
        .expectedCount(dto.getExpectedCount())
        .start(dto.getStart())
        .end(dto.getEnd())
        .createdAt(createdAt)
        .createdBy(createdBy)
        .status(ApplyStatusType.REGISTERED)
        .build();

    Apply newApply = applyRepository.save(apply);

    Long id = newApply.getId();
    Apply deleteApply = applyRepository.findById(id).orElseThrow(Exception::new);
    applyRepository.delete(deleteApply);
    Optional<Apply> deletedApply = applyRepository.findById(id);
    assertThat(deletedApply.isPresent()).isFalse();
  }

  @DisplayName("신청서 수정 테스트")
  @Test
  void modifyApplyTest() throws Exception {

    // create apply
    ApplyRequestDto dto = ApplyRequestDto.builder()
        .name("홍길동")
        .email("abc@naver.com")
        .phone("01000000000")
        .title("투표테스트")
        .expectedCount(5)
        .start(start).end(end)
        .build();
    Apply apply = Apply.builder()
        .name(dto.getName())
        .email(dto.getEmail())
        .phone(dto.getPhone())
        .title(dto.getTitle())
        .expectedCount(dto.getExpectedCount())
        .start(dto.getStart())
        .end(dto.getEnd())
        .createdAt(createdAt)
        .createdBy(createdBy)
        .status(ApplyStatusType.REGISTERED)
        .build();

    Apply newApply = applyRepository.save(apply);

    // modify apply
    LocalDateTime start = LocalDateTime.of(2019, 12, 11, 12, 50);
    LocalDateTime end = LocalDateTime.of(2019, 12, 30, 12, 50);
    ApplyRequestDto modifyDto = ApplyRequestDto.builder()
        .id(newApply.getId())
        .name("홍수정")
        .email("modify@naver.com")
        .phone("01000000000")
        .title("수정")
        .expectedCount(20)
        .start(start)
        .end(end)
        .build();

    Apply findedApply = applyRepository.findById(modifyDto.getId()).orElseThrow(Exception::new);
    findedApply.setName(modifyDto.getName())
        .setEmail(modifyDto.getEmail())
        .setPhone(modifyDto.getPhone())
        .setTitle(modifyDto.getTitle())
        .setExpectedCount(modifyDto.getExpectedCount())
        .setStart(modifyDto.getStart())
        .setEnd(modifyDto.getEnd());

    Apply modifiedApply = applyRepository.save(findedApply);

    // then
    assertThat(modifiedApply.getId()).isEqualTo(newApply.getId());
    assertThat(modifiedApply.getName()).isEqualTo("홍수정");
    assertThat(modifiedApply.getEmail()).isEqualTo("modify@naver.com");
    assertThat(modifiedApply.getTitle()).isEqualTo("수정");
    assertThat(modifiedApply.getStart()).isEqualTo(start);
    assertThat(modifiedApply.getEnd()).isEqualTo(end);
  }

  @DisplayName("투표시작 테스트")
  @Test
  public void voteStartTest() {

    List<Apply> applyList =
        applyRepository.findAllByVotedEqualsAndStartLessThanEqual(false, LocalDateTime.now());

  }

  public static void print(Apply apply) {

    System.out.println(apply.getName() + "\t" + apply.getStart() + "\t" + apply.isVoted());
  }

}
