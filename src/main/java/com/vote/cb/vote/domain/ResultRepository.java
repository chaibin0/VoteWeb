package com.vote.cb.vote.domain;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResultRepository extends JpaRepository<Result, ResultKey> {
  
  public List<Result> findAllByCandidateId(long candidateId);

  public List<Result> findAllByVoterId(Long id);
  
}
