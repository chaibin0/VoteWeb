package com.vote.cb.vote.domain;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VoteRepository extends JpaRepository<Vote, Long> {
  
  List<Vote> findAllByVoteInfoOrderBySequenceNumber(VoteInfomation voteInfo);

  List<Vote> findAllByVoteInfo(VoteInfomation voteInfo);
}
