package com.vote.cb.componant;

import java.util.Optional;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

@Component
public class VoteAuditorAware implements AuditorAware<String> {

  @Override
  public Optional<String> getCurrentAuditor() {

    return Optional.of("ADMIN_SERVER");
  }

}
