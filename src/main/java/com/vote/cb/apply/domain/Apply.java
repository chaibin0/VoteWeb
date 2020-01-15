package com.vote.cb.apply.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vote.cb.apply.controller.dto.ApplyRequestDto;
import com.vote.cb.apply.domain.enums.ApplyStatusType;
import com.vote.cb.user.domain.Member;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import org.hibernate.annotations.Where;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.userdetails.User;


@Table(name = "TBL_APPLY")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
@Accessors(chain = true)
@Where(clause = "APP_DELETED = false")
public class Apply {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "APP_ID")
  private Long id;

  @Column(name = "APP_NAME")
  private String name;

  @Column(name = "APP_EMAIL")
  private String email;

  @Column(name = "APP_PHONE")
  private String phone;

  @Column(name = "APP_TITLE")
  private String title;

  @Column(name = "APP_EXPECTED_COUNT")
  private int expectedCount;

  @Column(name = "APP_START")
  private LocalDateTime start;

  @Column(name = "APP_END")
  private LocalDateTime end;

  @CreatedDate
  @Column(name = "APP_CREATED_AT")
  private LocalDateTime createdAt;

  @CreatedBy
  @Column(name = "APP_CREATED_BY")
  private String createdBy;

  @LastModifiedDate
  @Column(name = "APP_UPDATED_AT")
  private LocalDateTime updatedAt;

  @LastModifiedBy
  @Column(name = "APP_UPDATED_BY")
  private String updatedBy;

  @Column(name = "APP_STATUS")
  @Enumerated(EnumType.STRING)
  private ApplyStatusType status;

  @Column(name = "APP_APPROVAL")
  private int approval;

  @Column(name = "APP_HAS_VOTE")
  private boolean hasVote;

  @Column(name = "APP_HAS_VOTER")
  private boolean hasVoter;

  @Column(name = "APP_HAS_RESULT")
  private boolean hasResult;

  // 배치를 통해 들어감
  @Column(name = "APP_IS_VOTING")
  private boolean voted;

  @ManyToOne
  @JoinColumn(name = "USER_ID")
  @JsonIgnore
  Member user;

  @Column(name = "APP_DELETED")
  private boolean deleted;

  public boolean isWriter(User user) {

    return this.user.getUserId().equals(user.getUsername());
  }

  public void modify(ApplyRequestDto dto) {

    this.setName(dto.getName().trim())
        .setEmail(dto.getEmail().trim())
        .setPhone(dto.getPhone())
        .setTitle(dto.getTitle().trim())
        .setExpectedCount(dto.getExpectedCount())
        .setStart(dto.getStart())
        .setApproval(-1)
        .setEnd(dto.getEnd());
  }
}
