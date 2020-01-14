package com.vote.cb.vote.controller;

import com.vote.cb.vote.controller.dto.VoteInfoDto;
import com.vote.cb.vote.controller.dto.VoteResponseDto;
import com.vote.cb.vote.domain.VoteInfomation;
import com.vote.cb.vote.service.VoteService;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/vote/making")
public class VoteMakingApiController {

  @Autowired
  private VoteService voteService;

  @PostMapping("")
  public ResponseEntity<VoteInfomation> storeVote(@AuthenticationPrincipal User user,
      @RequestBody @Valid VoteInfoDto dto)
      throws Exception {

    return voteService.saveVoteInfo(user, dto);
  }

  @PutMapping("")
  public ResponseEntity<?> modifyVote(@AuthenticationPrincipal User user,
      @RequestBody @Valid VoteInfoDto dto) throws Exception {

    return voteService.modifyVoteInfo(user, dto);
  }

  @GetMapping("/{applyId}")
  public VoteResponseDto getVote(@AuthenticationPrincipal User user,
      @PathVariable(name = "applyId") Long applyId) {

    return voteService.getVoteListByApplyId(user, applyId);
  }
  
  @GetMapping("/{applyId}/result")
  public VoteResponseDto getResult(@AuthenticationPrincipal User user,
      @PathVariable(name = "applyId") Long applyId) {

    return voteService.getVoteListByApplyId(user, applyId);
  }

}
