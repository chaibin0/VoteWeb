package com.vote.cb.admin.controller;

import com.vote.cb.admin.service.AdminService;
import com.vote.cb.apply.domain.enums.ApplyStatusType;
import com.vote.cb.apply.service.ApplyService;
import com.vote.cb.user.domain.enums.UserStatusType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/admin")
public class AdminController {


  private ApplyService applyService;

  private AdminService adminService;

  @Autowired
  public AdminController(ApplyService applyService, AdminService adminService) {

    this.applyService = applyService;
    this.adminService = adminService;
  }

  @GetMapping("")
  public ModelAndView viewSystemMain(Pageable pageable) {

    ModelAndView model = new ModelAndView();
    model.addObject("applyList", applyService.getApplyAllList(pageable));
    model.setViewName("admin/adminMain");
    return model;
  }

  @GetMapping("/user")
  public ModelAndView viewUser(Pageable pageable, @RequestParam(defaultValue = "") String search) {

    ModelAndView model = new ModelAndView();
    model.addObject("userList", adminService.getUserList(pageable, search));
    model.setViewName("admin/adminUser");
    return model;
  }

  @GetMapping("/user/black")
  public ModelAndView viewBlackUser(Pageable pageable,
      @RequestParam(defaultValue = "") String search) {

    ModelAndView model = new ModelAndView();
    model.addObject("userList", adminService.getUserBlackList(pageable, search));
    model.setViewName("admin/adminBlackUser");
    return model;
  }
}
