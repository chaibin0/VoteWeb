package com.vote.cb.apply.controller;

import com.vote.cb.apply.controller.dto.ApplyResponseDto;
import com.vote.cb.apply.service.ApplyService;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@Slf4j
@RequestMapping("/apply")
public class ApplyController {

  @Autowired
  private ApplyService applyService;

  @GetMapping("/making")
  public ModelAndView viewApply() {

    ModelAndView model = new ModelAndView();
    model.setViewName("apply/apply");
    return model;
  }

  @GetMapping("/making/success")
  public ModelAndView viewSuccessAply(@AuthenticationPrincipal User user, @RequestParam Long id)
      throws Exception {

    ApplyResponseDto apply = applyService.getApply(user, id);

    ModelAndView model = new ModelAndView();
    model.addObject("apply", apply);
    model.setViewName("apply/applySuccess");
    return model;
  }

  @GetMapping("/{id}")
  public ModelAndView viewApply(@AuthenticationPrincipal User user, @PathVariable Long id)
      throws Exception {

    ApplyResponseDto apply = applyService.getApply(user, id);
    ModelAndView model = new ModelAndView();
    model.addObject("apply", apply);
    model.setViewName("apply/applyView");
    return model;
  }

  // session 나중에 security로 바꿀것
  @GetMapping("")
  public ModelAndView viewApplyList(@AuthenticationPrincipal User user) throws Exception {

    ModelAndView model = new ModelAndView();
    model.addObject("applyList", applyService.getApplyList(user));
    model.setViewName("apply/applyList");
    return model;
  }

}
