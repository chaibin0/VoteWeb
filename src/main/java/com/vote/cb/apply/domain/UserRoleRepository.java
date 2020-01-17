package com.vote.cb.apply.domain;

import com.vote.cb.user.domain.UserRole;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRoleRepository extends JpaRepository<UserRole, Long>{
}
