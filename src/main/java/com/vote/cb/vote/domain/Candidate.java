package com.vote.cb.vote.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.vote.cb.vote.controller.dto.CandidateDto;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
@Entity
@Table(name = "TBL_CANDIDATE")
@Accessors(chain = true)
public class Candidate {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "CAND_ID")
  Long id;

  @Column(name = "CAND_SEQ_NO")
  int sequenceNumber;

  @Column(name = "CAND_NAME")
  String name;

  @Column(name = "CAND_DESC")
  String description;

  @Column(name = "CAND_VALUE")
  int value;

  @Column(name = "CAND_SELECTED_VALUE")
  int selectedValue;

  @Column(name = "CAND_ELECTED")
  int elected;

  @Column(name = "CAND_IMAGE")
  String image;

  // 조인됨 M : 1
  @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JsonBackReference
  @JoinColumn(name = "VOTE_ID")
  Vote vote;

  public void addValue(int value) {

    this.value += value;
  }
}
