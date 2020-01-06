package com.vote.cb.vote.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import com.vote.cb.exception.UnAuthorizedException;
import com.vote.cb.vote.service.VoteService;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/apply/{applyId}/vote")
@RequiredArgsConstructor
public class VoteMakingController {

  private final VoteService voteService;

  @GetMapping("/making")
  public ModelAndView viewMakingVote(@PathVariable(name = "applyId") Long applyId)
      throws UnAuthorizedException {

    if (!voteService.isApproval(applyId)) {
      throw new UnAuthorizedException();
    }

    ModelAndView model = new ModelAndView();
    model.setViewName("vote/voteApply");
    model.addObject("applyId", applyId);
    return model;
  }

  @GetMapping("/view")
  public ModelAndView viewVoteInfo(@AuthenticationPrincipal User user,
      @PathVariable(name = "applyId") Long applyId) throws Exception {

    ModelAndView model = new ModelAndView();
    model.setViewName("vote/voteInfo");
    model.addObject("voteInfo", voteService.getVoteListByApplyId(user, applyId));
    return model;
  }

  @GetMapping("/modify")
  public ModelAndView modifyViewVoteInfo(@AuthenticationPrincipal User user,
      @PathVariable(name = "applyId") Long applyId) throws Exception {

    ModelAndView model = new ModelAndView();
    model.setViewName("vote/voteModify");
    model.addObject("voteInfo", voteService.getVoteListByApplyId(user, applyId));
    return model;
  }

  @GetMapping("/result")
  public ModelAndView viewResult(@AuthenticationPrincipal User user,
      @PathVariable(name = "applyId") Long applyId) throws Exception {

    ModelAndView model = new ModelAndView();
    model.setViewName("vote/result");
    model.addObject("voteInfo", voteService.getVoteListByApplyId(user, applyId));
    return model;
  }

}
