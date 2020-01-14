package com.vote.cb.vote.controller;

import com.vote.cb.vote.controller.dto.VoteSignDto;
import com.vote.cb.vote.controller.dto.VotingDto;
import com.vote.cb.vote.service.VoteService;

import java.util.List;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/vote")
public class VoteApiController {

  @Autowired
  private VoteService voteService;

  @PostMapping("/sign")
  public ResponseEntity<?> signVoting(@RequestBody @Valid VoteSignDto dto, HttpSession session) {

    return voteService.authVoterInfo(dto, session);
  }

  @PostMapping("/voting")
  public ResponseEntity<?> vote(@RequestBody @Valid List<VotingDto> dto, HttpSession session) {

    String name = (String) session.getAttribute("name");
    String phone = (String) session.getAttribute("phone");
    String uid = (String) session.getAttribute("uid");

    if (name == null || phone == null) {
      return ResponseEntity.badRequest().body("시간이 지났습니다.");
    }

    return voteService.vote(name, phone, uid, dto);

  }

}
