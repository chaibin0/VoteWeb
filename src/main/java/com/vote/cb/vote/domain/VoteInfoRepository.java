package com.vote.cb.vote.domain;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.vote.cb.apply.domain.Apply;

public interface VoteInfoRepository extends JpaRepository<VoteInfomation, Long> {

  public Optional<VoteInfomation> findByApply(Apply apply);
}
