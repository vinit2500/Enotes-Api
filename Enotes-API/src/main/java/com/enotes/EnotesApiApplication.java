package com.enotes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditAware") //3rd step then step 6 give name of bean 
public class EnotesApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(EnotesApiApplication.class, args);
	}

}
