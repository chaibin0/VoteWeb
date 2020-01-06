package com.vote.cb.home.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("")
public class HomeController {

  @GetMapping("")
  public ModelAndView viewHomePage() {

    ModelAndView model = new ModelAndView();
    model.setViewName("main/index");
    return model;
  }
}
