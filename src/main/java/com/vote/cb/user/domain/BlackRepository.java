package com.vote.cb.user.domain;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BlackRepository extends JpaRepository<Black, Long> {

  Optional<Black> findByUser(Member member);

}
