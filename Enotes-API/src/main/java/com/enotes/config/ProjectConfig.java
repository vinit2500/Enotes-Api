package com.enotes.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
public class ProjectConfig {

	@Bean
	public ModelMapper mapper() {
		return new ModelMapper();
	}

	// step 5
	@Bean
	public AuditorAware<Integer> auditAware() {
		return new AuditAwareConfig();
	}

//	@Bean
//	public JavaMailSender javaMailSender() {
//		return new JavaMailSenderImpl();
//	}
}
