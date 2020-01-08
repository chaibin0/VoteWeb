package com.vote.cb.vote.domain;

import com.vote.cb.apply.domain.Apply;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VoteInfoRepository extends JpaRepository<VoteInfomation, Long> {

  public Optional<VoteInfomation> findByApply(Apply apply);
}
