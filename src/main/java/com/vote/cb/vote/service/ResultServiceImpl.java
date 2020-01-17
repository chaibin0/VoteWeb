package com.vote.cb.vote.service;

import com.vote.cb.apply.domain.Apply;
import com.vote.cb.apply.domain.ApplyRepository;
import com.vote.cb.apply.domain.Voter;
import com.vote.cb.apply.domain.VoterRepository;
import com.vote.cb.apply.domain.enums.ApplyStatusType;
import com.vote.cb.apply.domain.enums.VoterStatusType;
import com.vote.cb.exception.ApplyNotFoundException;
import com.vote.cb.exception.CandidateNotFoundException;
import com.vote.cb.exception.ExceptionDetails;
import com.vote.cb.exception.UnAuthorizedException;
import com.vote.cb.exception.VoteInfoNotFoundException;
import com.vote.cb.vote.domain.Candidate;
import com.vote.cb.vote.domain.CandidateRepository;
import com.vote.cb.vote.domain.Result;
import com.vote.cb.vote.domain.ResultRepository;
import com.vote.cb.vote.domain.VoteInfoRepository;
import com.vote.cb.vote.domain.VoteInfomation;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ResultServiceImpl implements ResultService {

  private ResultRepository resultRepository;

  private ApplyRepository applyRepository;

  private VoteInfoRepository voteInfoRepository;

  private CandidateRepository candidateRepository;

  private VoterRepository voterRepository;

  public ResultServiceImpl(ResultRepository resultRepository, ApplyRepository applyRepository,
      VoteInfoRepository voteInfoRepository, CandidateRepository candidateRepository,
      VoterRepository voterRepository) {

    this.resultRepository = resultRepository;
    this.applyRepository = applyRepository;
    this.voteInfoRepository = voteInfoRepository;
    this.candidateRepository = candidateRepository;
    this.voterRepository = voterRepository;
  }

  @Override
  @Transactional
  public ResponseEntity<?> countVote(User user, long applyId) {

    Map<Long, ResultObject> results = new HashMap<>();

    Apply apply = applyRepository.findById(applyId).orElseThrow(ApplyNotFoundException::new);

    if (!apply.isWriter(user)) {
      throw new UnAuthorizedException();
    }

    if (apply.getEnd().isAfter(LocalDateTime.now())) {
      return ResponseEntity.badRequest().body(new ExceptionDetails(LocalDateTime.now(), "400",
          "bad request", "개표할 수 있는 기간이 아닙니다."));
    }

    VoteInfomation voteInfo =
        voteInfoRepository.findByApply(apply).orElseThrow(VoteInfoNotFoundException::new);

    if (voteInfo.getCurrent() <= 0) {
      return ResponseEntity.badRequest().body(new ExceptionDetails(LocalDateTime.now(), "400",
          "bad request", "투표자가 존재하지 않아 개표를 실패하였습니다."));
    }

    List<Voter> voterList = voterRepository.findAllByApply(apply);

    for (Voter voter : voterList) {

      if (voter.getStatus().equals(VoterStatusType.UNVOTED)) {
        continue;
      }

      List<Result> resultList = resultRepository.findAllByVoterId(voter.getId());

      for (Result result : resultList) {
        if (!results.containsKey(result.getCandidateId())) {
          results.put(result.getCandidateId(), new ResultObject());
        }
        results.get(result.getCandidateId()).addValue(result.getValue());
      }

      voter.setStatus(VoterStatusType.COUNTED);
      voterRepository.save(voter);
    }

    makeRank(results);
    storeResult(results);

    apply.setStatus(ApplyStatusType.COUNTED).setHasResult(true);
    return ResponseEntity.ok().build();
  }

  @Transactional
  private void storeResult(Map<Long, ResultObject> results) {

    for (long candidateId : results.keySet()) {
      Candidate candidate =
          candidateRepository.findById(candidateId).orElseThrow(CandidateNotFoundException::new);
      candidate.setValue(results.get(candidateId).getValue())
          .setElected(results.get(candidateId).getRank());
      candidateRepository.save(candidate);
    }

  }

  private void makeRank(Map<Long, ResultObject> results) {

    // get Rank
    for (ResultObject resultObject : results.values()) {
      int value = resultObject.getValue();
      for (ResultObject comparedResultObject : results.values()) {
        if (value > comparedResultObject.getValue()) {
          comparedResultObject.increaseRank();
        }
      }
    }
  }

  @NoArgsConstructor
  @Getter
  class ResultObject {

    private int value = 0;

    private int rank = 1;

    public void addValue(int value) {

      this.value += value;
    }

    public void increaseRank() {

      this.rank++;
    }
  }
}
