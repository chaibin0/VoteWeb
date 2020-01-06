package com.vote.cb.apply.controller;

import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.vote.cb.apply.controller.dto.VoterDto;
import com.vote.cb.apply.domain.Voter;
import com.vote.cb.apply.service.VoterService;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/api/v1/apply/{applyId}/voter")
public class VoterApiController {

  @Autowired
  private VoterService voterService;


  @GetMapping("")
  public Page<Voter> getVoterList(@PageableDefault Pageable pageable,
      @AuthenticationPrincipal User user,
      @PathVariable("applyId") long applyId) {

    return voterService.getVoterListByApply(pageable, user, applyId);
  }

  @PostMapping("")
  public ResponseEntity<?> registerVoter(@AuthenticationPrincipal User user,
      @PathVariable("applyId") long applyId, @RequestBody @Valid VoterDto dto) {

    return voterService.registerVoter(user, dto, applyId);
  }

  @PutMapping("")
  public ResponseEntity<?> modifyVoter(@AuthenticationPrincipal User user,
      @PathVariable("applyId") long applyId, @RequestBody VoterDto dto) {

    return voterService.modifyVoter(user, dto, applyId);
  }

  @DeleteMapping("/{voterId}")
  public ResponseEntity<?> deleteVoter(@AuthenticationPrincipal User user,
      @PathVariable("applyId") long applyId, @PathVariable("voterId") long voterId) {

    return voterService.removeVoter(user, applyId, voterId);
  }
}
