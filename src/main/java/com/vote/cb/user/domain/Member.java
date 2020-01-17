package com.vote.cb.user.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vote.cb.user.domain.enums.UserStatusType;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "TBL_USER")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Builder
public class Member {

  @Id
  @Column(name = "USER_ID")
  String userId;

  @Column(name = "USER_PASSWORD")
  @JsonIgnore
  String password;

  @Column(name = "USER_NAME")
  String name;

  @Column(name = "USER_PHONE")
  String phone;

  @Column(name = "USER_EMAIL")
  String email;

  @CreatedDate
  @Column(name = "USER_CREATED_AT")
  LocalDateTime createdAt;

  @CreatedBy
  @Column(name = "USER_CREATED_BY")
  @JsonIgnore
  String createdBy;

  @LastModifiedDate
  @Column(name = "USER_UPDATED_AT")
  @JsonIgnore
  LocalDateTime updatedAt;

  @LastModifiedBy
  @Column(name = "USER_UPDATED_BY")
  @JsonIgnore
  String updatedBy;

  @Column(name = "USER_STATUS")
  @Enumerated(EnumType.STRING)
  UserStatusType status;

  @Column(name = "USER_ROLE")
  @JsonIgnore
  @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
  List<UserRole> role;
}
