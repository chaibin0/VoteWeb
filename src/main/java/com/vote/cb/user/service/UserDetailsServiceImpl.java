package com.vote.cb.user.service;

import java.util.HashSet;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.vote.cb.user.domain.Member;
import com.vote.cb.user.domain.MemberRepository;
import lombok.extern.slf4j.Slf4j;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

  @Autowired
  private MemberRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

    Member member =
        userRepository.findById(username).orElseThrow(() -> new UsernameNotFoundException(""));
    Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
    grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_" + member.getRole().toString()));
    return User.builder()
        .username(member.getUserId())
        .password(member.getPassword())
        .authorities(grantedAuthorities).build();


  }

}
