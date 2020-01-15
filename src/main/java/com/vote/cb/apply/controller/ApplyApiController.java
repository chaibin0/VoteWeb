package com.vote.cb.apply.controller;

import com.vote.cb.apply.controller.dto.ApplyRequestDto;
import com.vote.cb.apply.controller.dto.ApplyResponseDto;
import com.vote.cb.apply.domain.Apply;
import com.vote.cb.apply.service.ApplyService;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/apply")
public class ApplyApiController {

  @Autowired
  private ApplyService applyService;

  @GetMapping("/list")
  public Page<Apply> getApplyList(@AuthenticationPrincipal User user,
      @PageableDefault Pageable pageable) {

    return applyService.getApplyAllList(pageable, user);
  }

  @GetMapping("{id}")
  public ApplyResponseDto getApply(@AuthenticationPrincipal User user, @PathVariable Long id) {

    return applyService.getApply(user, id);
  }

  @PostMapping("")
  public ResponseEntity<?> registerApply(@AuthenticationPrincipal User user,
      @RequestBody @Valid ApplyRequestDto dto) {

    return applyService.registerApply(user, dto);
  }

  @PutMapping("")
  public ResponseEntity<?> modifyApply(@AuthenticationPrincipal User user,
      @RequestBody @Valid ApplyRequestDto dto) {

    return applyService.modifyApply(user, dto);
  }

  @DeleteMapping("{id}")
  public ResponseEntity<?> deleteApply(@AuthenticationPrincipal User user, @PathVariable Long id) {

    return applyService.removeApply(user, id);
  }


}
