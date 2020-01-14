package com.vote.cb.admin.controller;

import com.vote.cb.apply.service.ApplyService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/admin")
public class AdminController {

  @Autowired
  private ApplyService applyService;

  public AdminController(ApplyService applyService) {

    this.applyService = applyService;
  }

  @GetMapping("")
  public ModelAndView viewSystemMain(Pageable pageable) {

    ModelAndView model = new ModelAndView();
    model.addObject("applyList", applyService.getApplyAllList(pageable));
    model.setViewName("admin/adminMain");
    return model;
  }
}
