package com.vote.cb.admin.controller;

import com.vote.cb.admin.service.AdminService;
import com.vote.cb.apply.controller.dto.ApprovalDto;
import com.vote.cb.user.domain.Member;
import com.vote.cb.user.domain.enums.UserStatusType;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v1/admin")
public class AdminApiController {

  @Autowired
  private AdminService adminService;


  @GetMapping("/user")
  public Page<Member> viewUser(Pageable pageable, @RequestParam String search,
      @RequestParam UserStatusType type) {

    return adminService.getUserList(pageable, search, type);
  }


  @DeleteMapping("/user/remove")
  public ResponseEntity<?> removeUser(@RequestParam(name = "id") String id) {

    return adminService.removeUser(id);
  }

  @PostMapping("/apply/reject")
  public ResponseEntity<?> rejectApply(@RequestBody @Valid ApprovalDto dto) {

    return adminService.rejectApply(dto.getId());
  }

  @PostMapping("/apply/approval")
  public ResponseEntity<?> approvalApply(@RequestBody @Valid ApprovalDto dto) {

    return adminService.approvalApply(dto.getId());
  }


}
