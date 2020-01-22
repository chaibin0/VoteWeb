package com.vote.cb.user.domain;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;


@Entity
@Table(name = "TBL_BLACK")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Builder
public class Black {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "BLACK_ID")
  Long id;

  @OneToOne
  @JoinColumn(name = "BLACK_USER")
  Member user;

  @Column(name = "BLACK_PHONE")
  String phone;

  @CreatedDate
  @Column(name = "BLACK_CREATED_AT")
  LocalDateTime createdAt;

  @Column(name = "BLACK_END")
  LocalDateTime end;
}
