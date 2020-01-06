package com.vote.cb.componant;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import com.vote.cb.apply.domain.Apply;
import com.vote.cb.apply.domain.ApplyRepository;
import com.vote.cb.apply.domain.enums.ApplyStatusType;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class VoteSchedule {

  @Autowired
  private ApplyRepository applyRepository;

  @Scheduled(cron = "0 0/5 * * * ?")
  public void voteStartExecute() {

    log.info("shedule Start");
    List<Apply> applyList =
        applyRepository.findAllByVotedEqualsAndStartLessThanEqual(false, LocalDateTime.now());

    for (Apply apply : applyList) {
      apply.setVoted(true);
      apply.setStatus(ApplyStatusType.VOTING);
      applyRepository.save(apply);
    }
  }
  
  @Scheduled(cron = "0 0/5 * * * ?")
  public void voteEndExecute() {

    log.info("shedule End");
    List<Apply> applyList =
        applyRepository.findAllByStatusEqualsAndEndLessThan(ApplyStatusType.VOTING, LocalDateTime.now());

    for (Apply apply : applyList) {
      apply.setStatus(ApplyStatusType.FINISHED);
      applyRepository.save(apply);
    }
  }
}
