package com.vote.cb.user.controller;

import com.vote.cb.user.service.MemberService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/mypage")
public class MemberController {

  @Autowired
  MemberService memberService;

  @GetMapping("")
  public ModelAndView getMypage() {

    ModelAndView model = new ModelAndView();
    model.setViewName("/user/mypage");

    return model;
  }

  @GetMapping("/edit/user")
  public ModelAndView getEditUser(@AuthenticationPrincipal User user) {

    ModelAndView model = new ModelAndView();
    model.setViewName("/user/edit-user");
    model.addObject("user", memberService.getInfo(user));
    return model;
  }

  @GetMapping("/edit/password")
  public ModelAndView getEditPassword(@AuthenticationPrincipal User user) {

    return new ModelAndView("/user/edit-password");
  }
  
  @GetMapping("/remove")
  public ModelAndView getRemove(@AuthenticationPrincipal User user) {

    return new ModelAndView("/user/user-remove");
  }
}
