package com.vote.cb.vote.domain;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CandidateRepository extends JpaRepository<Candidate, Long>{

  List<Candidate> findAllByVoteOrderBySequenceNumber(Vote vote);

}
