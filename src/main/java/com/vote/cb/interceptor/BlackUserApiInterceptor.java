package com.vote.cb.interceptor;

import com.vote.cb.exception.CustomException;
import com.vote.cb.user.domain.Member;
import com.vote.cb.user.domain.MemberRepository;
import com.vote.cb.user.domain.enums.UserStatusType;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.servlet.HandlerInterceptor;


public class BlackUserApiInterceptor implements HandlerInterceptor {

  @Autowired
  MemberRepository memberRepository;

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
      throws Exception {

    UserDetails user =
        (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

    Member member = memberRepository.findById(user.getUsername())
        .orElseThrow(() -> CustomException.MEMBER_NOT_FOUND);

    if (member.getStatus().equals(UserStatusType.BLACK)) {
      response.sendError(403, "블랙당하셨습니다.");
    }

    return HandlerInterceptor.super.preHandle(request, response, handler);
  }

}
