package com.enotes.config;

import java.util.Optional;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;

//step 4
@Configuration
public class AuditAwareConfig implements AuditorAware<Integer> {

	@Override
	public Optional<Integer> getCurrentAuditor() {
		return Optional.of(2);
	}
}
