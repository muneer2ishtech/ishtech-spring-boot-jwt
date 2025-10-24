package fi.ishtech.springbootjwt.controller;

import java.net.URI;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.util.Pair;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import fi.ishtech.springbootjwt.dto.CustomErrorResponse;
import fi.ishtech.springbootjwt.dto.ForgotPasswordDto;
import fi.ishtech.springbootjwt.dto.SigninDto;
import fi.ishtech.springbootjwt.dto.SignupDto;
import fi.ishtech.springbootjwt.dto.UpdatePasswordDto;
import fi.ishtech.springbootjwt.dto.UserProfileDto;
import fi.ishtech.springbootjwt.jwt.JwtResponse;
import fi.ishtech.springbootjwt.jwt.JwtService;
import fi.ishtech.springbootjwt.service.UserService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author Muneer Ahmed Syed
 */
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

	@Value("${fi.ishtech.springbootjwt.login-by-email:true}")
	private boolean loginByEmail;

	private final AuthenticationManager authenticationManager;
	private final JwtService jwtService;

	private final UserService userService;

	// @formatter:off
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "OK",
					content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
							schema = @Schema(implementation = JwtResponse.class))),
			@ApiResponse(responseCode = "403", description = "Bad credentials",
					content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
							schema = @Schema(implementation = CustomErrorResponse.class)))
	})
	// @formatter:on
	@PostMapping("/signin")
	public ResponseEntity<?> signin(@Valid @RequestBody SigninDto signinDto) {
		String username = loginByEmail ? signinDto.getEmail() : signinDto.getUsername();
		log.debug("Signin request for {}", username);

		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(username, signinDto.getPassword()));

		SecurityContextHolder.getContext().setAuthentication(authentication);

		return ResponseEntity.ok(jwtService.generateJwtResponse(authentication));
	}

	// @formatter:off
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "CREATED",
					content = @Content(mediaType = MediaType.TEXT_PLAIN_VALUE))
	})
	// @formatter:on
	@PostMapping("/signup")
	public ResponseEntity<?> signup(@Valid @RequestBody SignupDto signupDto) {
		log.debug("New Signup: {}", loginByEmail ? signupDto.getEmail() : signupDto.getUsername());

		// TODO: verify recaptcha

		if (userService.existsByEmail(signupDto.getEmail())) {
			log.warn("Username Email: {} already exists", signupDto.getEmail());
			return ResponseEntity.badRequest().body(null);
		}

		UserProfileDto userProfileDto = userService.create(signupDto);

		// TODO: send verification email

		// @formatter:off
		URI uri = ServletUriComponentsBuilder
					.fromCurrentContextPath()
					.path("/api/v1/users/{userId}")
					.buildAndExpand(userProfileDto.getId())
					.toUri();
		// @formatter:on

		return ResponseEntity.created(uri).body(userProfileDto.getId());
	}

	// @formatter:off
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "OK")
	})
	// @formatter:on
	@PatchMapping(path = "/forgot-password", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Void> forgotPassword(@Valid @RequestBody ForgotPasswordDto forgotPasswordDto) {
		log.debug("Forgot password request for {}", forgotPasswordDto.getEmail());

		// TODO: recaptcha

		Pair<String, UserProfileDto> pair = userService.generatePasswordResetToken(forgotPasswordDto);
		// emailService.sendResetPasswordLink(pair.getFirst(), pair.getSecond());

		return ResponseEntity.ok().build();
	}

	// @formatter:off
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "OK")
	})
	// @formatter:on
	@PutMapping(path = "/reset-password", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Void> updatePasswordByToken(@Valid @RequestBody UpdatePasswordDto updatePasswordDto) {
		log.debug("Rest to new password request");
		Assert.hasText(updatePasswordDto.getToken(), "Token is mandatory");

		// TODO: recaptcha

		UserProfileDto userProfileDto = userService.updatePasswordByToken(updatePasswordDto);
		// emailService.notifyPasswordUpdated(userProfileDto);

		return ResponseEntity.ok().build();
	}

	// @formatter:off
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "OK")
	})
	// @formatter:on
	@PutMapping(path = "/update-password", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Void> updatePassword(@Valid @RequestBody UpdatePasswordDto updatePasswordDto,
			HttpServletRequest request) {
		Long userId = jwtService.extractUserIdFromRequest(request);
		log.debug("Update password request for {}", userId);

		// TODO: recaptcha

		UserProfileDto userProfileDto = userService.updatePassword(userId, updatePasswordDto);
		// emailService.notifyPasswordUpdated(userProfileDto);

		return ResponseEntity.ok().build();
	}

}