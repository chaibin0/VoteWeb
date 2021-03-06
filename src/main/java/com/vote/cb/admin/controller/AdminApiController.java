package com.vote.cb.admin.controller;

import com.vote.cb.admin.controller.dto.ApprovalDto;
import com.vote.cb.admin.controller.dto.UserBlackDto;
import com.vote.cb.admin.service.AdminService;
import com.vote.cb.user.domain.Member;
import com.vote.cb.user.domain.enums.UserStatusType;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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
  public Page<Member> viewUser(@PageableDefault(size = 10) Pageable pageable,
      @RequestParam(defaultValue = "") String search) {

    return adminService.getUserList(pageable, search);
  }


  @DeleteMapping("/user")
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

  @PostMapping("/user/black")
  public ResponseEntity<?> modifyBlackUser(@RequestBody @Valid UserBlackDto dto) {

    return adminService.blackUser(dto);
  }


}
