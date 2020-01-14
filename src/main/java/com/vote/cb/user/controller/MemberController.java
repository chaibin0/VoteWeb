package com.vote.cb.user.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/user")
public class MemberController {
  
  @GetMapping("/signup")
  public ModelAndView viewSginUpPage() {

    ModelAndView model = new ModelAndView();
    model.setViewName("user/signup");
    return model;
  }

  @GetMapping("/login")
  public ModelAndView viewLoginPage() {

    ModelAndView model = new ModelAndView();
    model.setViewName("user/login");
    return model;
  }
}
