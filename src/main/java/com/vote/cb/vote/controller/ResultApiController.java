package com.vote.cb.vote.controller;

import com.vote.cb.vote.service.ResultService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/result")
@RequiredArgsConstructor
public class ResultApiController {

  private final ResultService resultService;

  @PostMapping("/{applyId}")
  public ResponseEntity<?> countVote(@AuthenticationPrincipal User user,
      @PathVariable(value = "applyId") long applyId) throws Exception {

    return resultService.countVote(user, applyId);
  }


}
