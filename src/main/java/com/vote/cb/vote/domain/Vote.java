package com.vote.cb.vote.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.vote.cb.vote.controller.dto.VoteDto;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
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


@Entity
@Table(name = "TBL_VOTE")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
@Accessors(chain = true)
public class Vote {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "VOTE_ID")
  Long id;

  @Column(name = "VOTE_NAME")
  String name;

  // 투표 순서
  @Column(name = "VOTE_SEQ_NUM")
  int sequenceNumber;

  // 선택 갯수
  @Column(name = "VOTE_SEL_NUM")
  int selectedNumber;

  // 당선 갯수
  @Column(name = "VOTE_ELEC_COUNT")
  int electedCount;

  @OneToMany(mappedBy = "vote", cascade = CascadeType.ALL, orphanRemoval = true)
  @JsonManagedReference
  List<Candidate> candidateList;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "VOTEINFO_ID")
  @JsonBackReference
  VoteInfomation voteInfo;

  public static Vote of(VoteInfomation voteInfo, VoteDto voteDto) {

    return Vote.builder()
        .sequenceNumber(voteDto.getVoteSeqNum())
        .selectedNumber(voteDto.getVoteSelNum())
        .name(voteDto.getVoteName().trim())
        .electedCount(voteDto.getVoteElecNum())
        .voteInfo(voteInfo)
        .build();
  }
}
