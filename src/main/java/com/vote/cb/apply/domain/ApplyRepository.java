package com.vote.cb.apply.domain;

import com.vote.cb.apply.domain.enums.ApplyStatusType;
import com.vote.cb.user.domain.Member;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplyRepository extends JpaRepository<Apply, Long> {

  List<Apply> findAllByName(String userName);

  Apply findByIdAndName(String id, String userName);

  Page<Apply> findAllByUser(Pageable pageable, Member user);

  List<Apply> findAllByNameAndPhone(String userName, String phone);

  List<Apply> findAllByUser(Member user);

  List<Apply> findAllByVotedEqualsAndStartLessThanEqual(boolean isVoting, LocalDateTime start);

  List<Apply> findAllByStatusEqualsAndEndLessThan(ApplyStatusType voting, LocalDateTime now);
}
