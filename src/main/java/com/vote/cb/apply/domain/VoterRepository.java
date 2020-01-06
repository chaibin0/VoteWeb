package com.vote.cb.apply.domain;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VoterRepository extends JpaRepository<Voter, Long> {

  Page<Voter> findAllByApply(Pageable pageable, Apply apply);

  List<Voter> findAllByApply(Apply apply);

  Optional<Voter> findByNameAndPhoneAndSsn(String userName, String phone, String ssn);

  boolean existsBySsn(String ssn);
}
