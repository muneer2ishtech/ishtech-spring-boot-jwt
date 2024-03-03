package fi.ishtech.springboot.jwt.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Config class for Security
 */
@Configuration
@EnableMethodSecurity
public class WebSecurityConfig {

	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

		// @formatter:off
		http
			.authorizeHttpRequests(
					auth -> auth
						.requestMatchers(
								"/error",
								"/",
								"/favicon.ico"
						)
							.permitAll()
						.anyRequest()
							.authenticated());
		// @formatter:on

		return http.build();
	}

}
