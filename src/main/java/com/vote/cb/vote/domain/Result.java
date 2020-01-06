package com.vote.cb.vote.domain;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import com.vote.cb.apply.domain.Voter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
@Builder
@Table(name = "TBL_RESULT")
@IdClass(ResultKey.class)
@EntityListeners(AuditingEntityListener.class)
public class Result {

  @Id
  @Column(name = "RESULT_VOTER_ID")
  Long voterId;

  @Id
  @Column(name = "RESULT_CANDIDATE_ID")
  Long candidateId;

  @Column(name = "RESULT_VALUE")
  int value;

  @CreatedDate
  @Column(name = "RESULT_CREATED_AT")
  LocalDateTime createdAt;

  @CreatedBy
  @Column(name = "RESULT_CREATED_BY")
  String createdBy;

  public static Result of(Voter voter, Candidate candidate, int value) {

    return Result.builder()
        .voterId(voter.getId())
        .candidateId(candidate.getId())
        .value(value)
        .build();
  }
}
