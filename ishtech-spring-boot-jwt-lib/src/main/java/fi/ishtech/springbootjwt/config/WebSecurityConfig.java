package fi.ishtech.springbootjwt.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import fi.ishtech.springbootjwt.jwt.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;

/**
 * @author Muneer Ahmed Syed
 */
@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

	/**
	 * Comma-separated list of API endpoints that should be accessible without authentication.<br>
	 * If this property is not defined, no additional URLs are permitted beyond the defaults.<br>
	 * <br>
	 * Examples:
	 * 
	 * <pre>
	 * # none → handled by default
	 * fi.ishtech.springbootjwt.permitted-urls=
	 *
	 * # one
	 * fi.ishtech.springbootjwt.permitted-urls=/first-api
	 *
	 * # multiple (comma-separated)
	 * fi.ishtech.springbootjwt.permitted-urls=/first-api,/second-api,/third-api
	 * </pre>
	 */
	@Value("${fi.ishtech.springbootjwt.permitted-urls:}")
	private String[] permittedUrls;

	/**
	 * Enables access to Swagger UI and OpenAPI documentation without authentication.<br>
	 * <br>
	 * Default: {@code false}
	 * 
	 * <pre>
	 * fi.ishtech.springbootjwt.permit-swagger-urls=true
	 * </pre>
	 */
	@Value("${fi.ishtech.springbootjwt.permit-swagger-urls:false}")
	private boolean permitSwaggerUrls;

	private final UserDetailsService userDetailsService;
	private final JwtAuthenticationFilter jwtAuthenticationFilter;

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		// @formatter:off
		http
			.csrf(csrf -> csrf.disable())
			.sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			.authorizeHttpRequests(auth -> auth
				.requestMatchers(getPermittedUrls().toArray(String[]::new)).permitAll()
				.anyRequest().authenticated()
			)
			.exceptionHandling(ex -> ex.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))
			.authenticationProvider(authenticationProvider())
			.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
		// @formatter:on

		return http.build();
	}

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider(userDetailsService);
		authenticationProvider.setPasswordEncoder(passwordEncoder());
		return authenticationProvider;
	}
	
	@Bean
	AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
		return config.getAuthenticationManager();
	}

	private List<String> getPermittedUrls() {
		List<String> urls = new ArrayList<>(getDefaultPermittedUrls());
		
		urls.addAll(getAuthUrls());

		if (permitSwaggerUrls) {
			urls.addAll(getSwaggerUrls());
		}

		urls.addAll(getActuatorUrls());

		if (permittedUrls != null && permittedUrls.length > 0) {
			urls.addAll(Arrays.asList(permittedUrls));
		}

		return urls;
	}

	private List<String> getDefaultPermittedUrls() {
		// @formatter:off
		return List.of(
				"/h2-console/**",
				"/",
				"/error"
		);
		// @formatter:on
	}

	private List<String> getAuthUrls() {
		// @formatter:off
		return List.of(
				"/api/v1/auth/signin",
				"/api/v1/auth/signup",
				"/api/v1/auth/forgot-password",
				"/api/v1/auth/reset-password"
		);
		// @formatter:on
	}

	private List<String> getSwaggerUrls() {
		// @formatter:off
		return List.of(
				"/swagger-ui.html",
				"/swagger-ui/**",
				"/v3/api-docs/**"
		);
		// @formatter:on
	}

	/**
	 * Allows actuator endpoints like health and info to be accessible without authentication.
	 */
	private List<String> getActuatorUrls() {
		// @formatter:off
		return List.of(
			"/actuator/health",
			"/actuator/info"
		);
		// @formatter:on
	}

}