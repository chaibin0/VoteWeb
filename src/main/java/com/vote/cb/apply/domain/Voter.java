package com.vote.cb.apply.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vote.cb.apply.controller.dto.VoterDto;
import com.vote.cb.apply.domain.enums.VoterStatusType;

import java.time.LocalDateTime;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "TBL_VOTER")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Builder
@Accessors(chain = true)
public class Voter {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "VOTER_ID")
  Long id;

  @Column(name = "VOTER_NAME")
  String name;

  @Column(name = "VOTER_PHONE")
  String phone;

  @Column(name = "VOTER_SSN")
  @JsonIgnore
  String ssn;

  @Column(name = "VOTER_STATUS")
  // @Enumerated(EnumType.STRING)
  VoterStatusType status;

  @CreatedDate
  @Column(name = "VOTER_CREATED_AT")
  LocalDateTime createdAt;

  @CreatedBy
  @Column(name = "VOTER_CREATED_BY")
  String createdBy;

  @LastModifiedDate
  @Column(name = "VOTER_UPDATED_AT")
  LocalDateTime updatedAt;

  @LastModifiedBy
  @Column(name = "VOTER_UPDATED_BY")
  String updatedBy;

  @Column(name = "VOTER_VOTED_DATE")
  LocalDateTime votedDate;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "APPLY_ID")
  Apply apply;

  public static Voter of(Apply apply, VoterDto voterDto) {

    return Voter.builder()
        .name(voterDto.getVoterName().trim())
        .phone(voterDto.getVoterPhone())
        .apply(apply)
        .ssn(UUID.randomUUID().toString().replace("-", ""))
        .status(VoterStatusType.UNVOTED)
        .build();
  }
}
