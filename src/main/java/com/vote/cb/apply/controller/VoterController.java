package com.vote.cb.apply.controller;

import com.vote.cb.apply.service.ApplyService;
import com.vote.cb.apply.service.VoterService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/apply/{applyId}/voter")
public class VoterController {

  @Autowired
  private VoterService voterService;

  @Autowired
  private ApplyService applyService;

  @GetMapping("")
  public ModelAndView viewVoterListAndModify(@PageableDefault Pageable pageable,
      @AuthenticationPrincipal User user,
      @PathVariable(value = "applyId") long applyId) {

    ModelAndView model = new ModelAndView();
    String viewName =
        applyService.alreadyStart(user, applyId) ? "voter/voterList" : "voter/voterListAndModify";
    model.addObject("voterList", voterService.getVoterListByApply(pageable, user, applyId));
    model.setViewName(viewName);
    return model;
  }

}
