package com.vote.cb.user.domain;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface MemberRepository extends JpaRepository<Member, String> {

  // Select * from TBL_USER where id=? and password = ?
  Optional<Member> findByUserIdAndPassword(String id, String password);
  
  Optional<Member> findByUserId(String id);
}
