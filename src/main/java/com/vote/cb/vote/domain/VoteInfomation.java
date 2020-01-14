package com.vote.cb.vote.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.vote.cb.apply.domain.Apply;
import com.vote.cb.vote.controller.dto.VoteInfoDto;
import com.vote.cb.vote.domain.enums.VoteInfoStatusType;
import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
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

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "TBL_VOTEINFO")
@Builder
@EntityListeners(AuditingEntityListener.class)
@Accessors(chain = true)
public class VoteInfomation {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "VOTEINFO_ID")
  Long id;

  @Column(name = "VOTEINFO_NAME")
  String name;

  @Column(name = "VOTEINFO_DESC")
  String description;

  @Column(name = "VOTEINFO_COUNT")
  int count;

  @Enumerated(EnumType.STRING)
  @Column(name = "VOTEINFO_STATUS")
  VoteInfoStatusType status;

  @CreatedDate
  @Column(name = "VOTEINFO_CREATED_AT")
  LocalDateTime createdAt;

  @CreatedBy
  @Column(name = "VOTEINFO_CREATED_BY")
  String createdBy;

  @LastModifiedDate
  @Column(name = "VOTEINFO_UPDATED_AT")
  LocalDateTime updatedAt;

  @LastModifiedBy
  @Column(name = "VOTEINFO_UPDATED_BY")
  String updatedBy;

  @Column(name = "VOTEINFO_CURRENT")
  int current;

  @OneToMany(mappedBy = "voteInfo", cascade = CascadeType.ALL, orphanRemoval = true)
  @JsonManagedReference
  List<Vote> voteList;

  @OneToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "APPLY_ID")
  @JsonBackReference
  Apply apply;

  public void addCurrent() {

    this.current++;
  }

  public static VoteInfomation of(Apply apply, VoteInfoDto voteInfoDto) {

    return VoteInfomation.builder()
        .apply(apply)
        .description(voteInfoDto.getVoteInfoDesc().trim())
        .name(voteInfoDto.getVoteInfoTitle().trim())
        .status(VoteInfoStatusType.NORMAL)
        .build();
  }
}
