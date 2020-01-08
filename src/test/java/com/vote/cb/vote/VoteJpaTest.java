package com.vote.cb.vote;

import static org.assertj.core.api.Assertions.assertThat;

import com.vote.cb.apply.controller.dto.ApplyRequestDto;
import com.vote.cb.apply.domain.Apply;
import com.vote.cb.apply.domain.ApplyRepository;
import com.vote.cb.apply.domain.enums.ApplyStatusType;
import com.vote.cb.exception.VoteInfoNotFoundException;
import com.vote.cb.vote.controller.dto.CandidateDto;
import com.vote.cb.vote.controller.dto.VoteDto;
import com.vote.cb.vote.controller.dto.VoteInfoDto;
import com.vote.cb.vote.domain.Candidate;
import com.vote.cb.vote.domain.CandidateRepository;
import com.vote.cb.vote.domain.Vote;
import com.vote.cb.vote.domain.VoteInfoRepository;
import com.vote.cb.vote.domain.VoteInfomation;
import com.vote.cb.vote.domain.VoteRepository;
import com.vote.cb.vote.domain.enums.VoteInfoStatusType;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;


@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
class VoteJpaTest {

  @Autowired
  VoteRepository voteRepository;

  @Autowired
  CandidateRepository candidateRepository;

  @Autowired
  VoteInfoRepository voteInfoRepository;

  @Autowired
  ApplyRepository applyRepository;

  LocalDateTime start;

  LocalDateTime end;

  LocalDateTime createdAt;

  String createdBy;

  ApplyRequestDto dto;

  Long id;

  @BeforeEach
  void setUp() throws Exception {

    createdAt = LocalDateTime.now();
    createdBy = "ADMIN_SERVER";
    start = LocalDateTime.of(2019, 12, 10, 10, 13);
    end = LocalDateTime.of(2019, 12, 12, 10, 13);

    // create apply
    dto = ApplyRequestDto.builder()
        .name("홍길동")
        .email("abc@naver.com")
        .phone("01054136068")
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
    this.id = newApply.getId();
  }

  @DisplayName("투표 등록 테스트")
  @Test
  @Transactional
  void saveVoteTest() throws Exception {

    List<VoteDto> voteDto = new ArrayList<>();
    List<CandidateDto> candidateDto1 = new ArrayList<>();

    CandidateDto candDto1 = CandidateDto.builder()
        .candidateSeqNo(1)
        .candidateName("후보자1")
        .candidateDesc("후보자 설명")
        .build();

    candidateDto1.add(candDto1);

    VoteDto voteDto1 = VoteDto.builder()
        .voteSeqNum(1)
        .voteSelNum(1)
        .voteName("첫번째 투표")
        .voteElecNum(1)
        .candidate(candidateDto1)
        .build();

    voteDto.add(voteDto1);

    VoteInfoDto dto = VoteInfoDto.builder()
        .applyId(this.id)
        .voteInfoTitle("투표 제목")
        .voteInfoDesc("투표 설명")
        .voteInfoCount(10)
        .voteDto(voteDto)
        .build();


    Apply apply = applyRepository.findById(dto.getApplyId()).orElseThrow(Exception::new);
    VoteInfomation voteInfo = VoteInfomation.builder()
        .apply(apply)
        .count(dto.getVoteInfoCount())
        .description(dto.getVoteInfoDesc())
        .name(dto.getVoteInfoTitle())
        .status(VoteInfoStatusType.NORMAL)
        .createdAt(LocalDateTime.now())
        .createdBy("ADMIN_SERVER")
        .build();

    VoteInfomation saveVoteInfo = voteInfoRepository.save(voteInfo);

    List<Vote> voteList = saveVote(saveVoteInfo, dto.getVoteDto());
    saveVoteInfo.setVoteList(voteList);
    apply.setHasVote(true);
    Apply savedApply = applyRepository.save(apply);

    assertThat(savedApply).isEqualTo(saveVoteInfo.getApply());
    assertThat(saveVoteInfo.getName()).isEqualTo("투표 제목");
    assertThat(saveVoteInfo.getVoteList().get(0).getName()).isEqualTo("첫번째 투표");
    assertThat(saveVoteInfo.getVoteList().get(0).getCandidateList().get(0).getName())
        .isEqualTo("후보자1");

  }

  private List<Vote> saveVote(VoteInfomation voteInfo, List<VoteDto> voteDtoList) {

    List<Vote> voteList = new ArrayList<>();

    for (VoteDto dto : voteDtoList) {
      Vote vote = Vote.builder()
          .sequenceNumber(dto.getVoteSeqNum())
          .selectedNumber(dto.getVoteSelNum())
          .name(dto.getVoteName())
          .electedCount(dto.getVoteElecNum())
          .voteInfo(voteInfo)
          .build();

      Vote saveVote = voteRepository.save(vote);

      List<Candidate> candidateList = saveCandidate(saveVote, dto.getCandidate());
      saveVote.setCandidateList(candidateList);
      voteList.add(saveVote);
    }

    return voteList;
  }

  private List<Candidate> saveCandidate(Vote vote, List<CandidateDto> candidateDtoList) {

    List<Candidate> candidateList = new ArrayList<>();
    for (CandidateDto dto : candidateDtoList) {
      Candidate candidate = Candidate.builder()
          .sequenceNumber(dto.getCandidateSeqNo())
          .name(dto.getCandidateName())
          .description(dto.getCandidateDesc())
          .vote(vote)
          .build();

      Candidate saveCandidate = candidateRepository.save(candidate);
      candidateList.add(saveCandidate);
    }

    return candidateList;
  }

}
