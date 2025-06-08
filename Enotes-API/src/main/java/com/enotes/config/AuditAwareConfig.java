package com.enotes.config;

import java.util.Optional;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;

import com.enotes.entity.User;
import com.enotes.util.CommonUtils;

//step 4
@Configuration
public class AuditAwareConfig implements AuditorAware<Integer> {

	@Override
	public Optional<Integer> getCurrentAuditor() {
		User loggedInUser = CommonUtils.getLoggedInUser();
		return Optional.of(loggedInUser.getId());
	}
}
