package com.vote.cb.user.service;

import com.vote.cb.user.domain.Member;
import com.vote.cb.user.domain.MemberRepository;
import com.vote.cb.user.domain.UserRole;

import java.util.HashSet;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

  private MemberRepository userRepository;

  public UserDetailsServiceImpl(MemberRepository userRepository) {

    this.userRepository = userRepository;
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

    Member member =
        userRepository.findById(username)
            .orElseThrow(() -> new UsernameNotFoundException("아이디를 찾을 수 없습니다 : " + username));

    Set<GrantedAuthority> grantedAuthorities = new HashSet<>();

    for (UserRole role : member.getRole()) {
      grantedAuthorities.add(new SimpleGrantedAuthority(role.getRole().getRoleName()));
    }

    return User.builder()
        .username(member.getUserId())
        .password(member.getPassword())
        .authorities(grantedAuthorities).build();
  }

}
