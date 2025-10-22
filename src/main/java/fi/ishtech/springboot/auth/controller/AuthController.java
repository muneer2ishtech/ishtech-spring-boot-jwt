package fi.ishtech.springboot.auth.controller;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import fi.ishtech.springboot.auth.entity.User;
import fi.ishtech.springboot.auth.jwt.JwtService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1/auth")
@Slf4j
public class AuthController {
	
	@Autowired
	private JwtService jwtService;

	@Autowired
	private UserService userService;
	
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = JwtResponse.class))),
			@ApiResponse(responseCode = "403", description = "Bad credentials", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = CustomErrorResponse.class))) })
	@PostMapping("/signin")
	public ResponseEntity<?> signin(@Valid @RequestBody SigninRequest signinRequest) {
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(signinRequest.getUsername(), signinRequest.getPassword()));
		log.debug("Sigin request for {}", signinRequest.getUsername());

		SecurityContextHolder.getContext().setAuthentication(authentication);

		return ResponseEntity.ok(jwtUtil.getJwtResponse(authentication));
	}

	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "CREATED", content = @Content(mediaType = MediaType.TEXT_PLAIN_VALUE)),
			@ApiResponse(responseCode = "403", description = "Bad credentials", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = CustomErrorResponse.class))) })
	@PostMapping("/signup")
	public ResponseEntity<?> signup(@Valid @RequestBody SignupRequest signupRequest) {
		log.debug("Signigup: {}", signupRequest.getUsername());

		if (userService.existsByEmail(signupRequest.getUsername())) {
			log.warn("Username: {} already exists", signupRequest.getUsername());
			throw new UsernameAlreadyExistsException();
		}

		User user = userService.create(signupRequest);

		URI uri = ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/v1/users/{userId}")
				.buildAndExpand(user.getId()).toUri();

		return ResponseEntity.created(uri).body(user.getId());
	}

	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "OK"),
			@ApiResponse(responseCode = "403", description = "Bad credentials", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = CustomErrorResponse.class))) })
	@PreAuthorize(value = "hasAuthority('ROLE_USER')")
	@PatchMapping("/update-password")
	public ResponseEntity<Void> update(@Valid @RequestBody PasswordUpdateRequest passwordUpdateRequest) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Long userId = jwtService.getUserId(authentication);
		log.debug("Updating password for {}", userId);

		userService.updatePassword(userId, passwordUpdateRequest);

		return ResponseEntity.ok(null);
	}

}
