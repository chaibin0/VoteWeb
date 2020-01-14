package com.vote.cb.admin.controller;

import com.vote.cb.admin.service.AdminService;
import com.vote.cb.apply.controller.dto.ApprovalDto;
import com.vote.cb.apply.service.ApplyService;
import com.vote.cb.user.domain.Member;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
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

  //관리자기능은 adminService로 옮길 예정
  @Autowired
  private ApplyService applyService;

  @GetMapping("/user")
  public ResponseEntity<Member> getUser(@RequestParam(name = "id") String id) {

    return adminService.getUser(id);
  }

  @DeleteMapping("/user/remove")
  public ResponseEntity<?> removeUser(@RequestParam(name = "id") String id) {

    return adminService.removeUser(id);
  }

  @PostMapping("/apply/reject")
  public ResponseEntity<?> rejectApply(@RequestBody @Valid ApprovalDto dto) {

    return applyService.rejectApply(dto.getId());
  }

  @PostMapping("/apply/approval")
  public ResponseEntity<?> approvalApply(@RequestBody @Valid ApprovalDto dto) {

    return applyService.approvalApply(dto.getId());

  }


}
