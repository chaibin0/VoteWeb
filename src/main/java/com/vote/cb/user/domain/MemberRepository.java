package com.vote.cb.user.domain;

import com.vote.cb.user.domain.enums.UserStatusType;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface MemberRepository extends JpaRepository<Member, String> {

  // Select * from TBL_USER where id=? and password = ?
  Optional<Member> findByUserIdAndPassword(String id, String password);

  Optional<Member> findByUserId(String id);

  Page<Member> findAllByUserIdContainingAndStatusOrderByCreatedAt(Pageable pageable, String search,
      UserStatusType status);

  // Page<Member> findAllByStatusOrderByCreatedAt(Pageable pageable, UserStatusType status);

  Page<Member> findAllByUserIdContainingOrderByCreatedAt(Pageable pageable, String search);

  // Page<Member> findAllByOrderByCreatedAt(Pageable pageable);
}
