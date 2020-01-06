package com.vote.cb.vote.controller;

import java.util.List;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.vote.cb.vote.controller.dto.VoteSignDto;
import com.vote.cb.vote.controller.dto.VotingDto;
import com.vote.cb.vote.service.VoteService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/vote")
@RequiredArgsConstructor
public class VoteApiController {

  private final VoteService voteService;

  @PostMapping("/{uid}/sign")
  public ResponseEntity<?> signVoting(@PathVariable(value = "uid") String uid,
      @RequestBody VoteSignDto dto, HttpSession session) {

    return voteService.authVoterInfo(uid, dto, session);
  }

  @PostMapping("/{uid}/voting")
  public ResponseEntity<?> vote(@RequestBody @Valid List<VotingDto> dto, @PathVariable("uid") String uid,
      HttpSession session) {
    
    String name = (String) session.getAttribute("name");
    String phone = (String) session.getAttribute("phone");

    if (name == null || phone == null) {
      return ResponseEntity.badRequest().body("시간이 지났습니다.");
    }

    return voteService.vote(name, phone, uid, dto);

  }

}
