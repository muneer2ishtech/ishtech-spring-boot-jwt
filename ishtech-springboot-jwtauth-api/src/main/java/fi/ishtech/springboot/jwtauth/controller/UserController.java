package fi.ishtech.springboot.jwtauth.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fi.ishtech.springboot.jwtauth.dto.UserProfileDto;
import fi.ishtech.springboot.jwtauth.payload.params.UserProfileFilterParams;
import fi.ishtech.springboot.jwtauth.service.AuthInfoService;
import fi.ishtech.springboot.jwtauth.service.UserProfileService;
import fi.ishtech.springboot.jwtauth.spec.UserProfileSpec;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author Muneer Ahmed Syed
 */
@RestController
@RequestMapping
@RequiredArgsConstructor
@Slf4j
public class UserController {

	private final UserProfileService userProfileService;
	private final AuthInfoService authInfoService;

	/**
	 * Finds UserProfile(s) by search filters and pagination
	 *
	 * @param params   - {@link UserProfileFilterParams}
	 * @param pageable - {@link Pageable}
	 * @return {@link ResponseEntity}&lt;{@link Page}&lt;{@link UserProfileDto}&gt;&gt;
	 */
	@GetMapping(path = "/api/v1/users", produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
	public ResponseEntity<Page<UserProfileDto>> searchUserProfiles(@Valid UserProfileFilterParams params,
			Pageable pageable) {
		log.trace("UserProfileFilterParams:{}", params);

		UserProfileSpec userProfileSpec = new UserProfileSpec(params);

		Page<UserProfileDto> result = userProfileService.findAllAndMapToVo(userProfileSpec, pageable);

		return ResponseEntity.ok(result);
	}

	/**
	 * Finds UserProfile by userId or for current user
	 *
	 * @param userId user ID or me
	 * @return {@link ResponseEntity}&lt;{@link UserProfileDto}&gt;
	 */
	// @formatter:off
	@GetMapping(path = "/api/v1/users/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasAuthority('ROLE_ADMIN') || #userId == 'me'"
			+ " || T(java.lang.Long).valueOf(#userId) == principal.id")
	public ResponseEntity<UserProfileDto> findUserProfile(
			@Pattern(regexp = "^(me|\\d+)$", message = "Invalid input. Only 'me' or an integer allowed.")
				@PathVariable("userId") String userId) {
		// @formatter:on
		log.info("Input userId:{}", userId);

		Long loggedInUserId = authInfoService.getUserId();
		log.debug("URL param: {}, logged in userId: {}", userId, loggedInUserId);

		Long inputUserId = "me".equalsIgnoreCase(userId) ? loggedInUserId : Long.valueOf(userId);

		var userProfileDto = userProfileService.findOneByIdAndMapToVoOrElseThrow(inputUserId);

		return ResponseEntity.ok(userProfileDto);
	}

	/**
	 * Updates UserProfile by userId or for current user
	 *
	 * @param userId user ID or me
	 * @return {@link ResponseEntity}&lt;{@link UserProfileDto}&gt;
	 */
	// @formatter:off
	@PutMapping(path = "/api/v1/users/{userId}",
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("(#userProfileDto.id == null || T(java.lang.Long).valueOf(#userId) == #userProfileDto.id)"
					+ " && (hasAuthority('ROLE_ADMIN') || T(java.lang.Long).valueOf(#userId) == principal.id"
						+ " || (#userId == 'me' && #userProfileDto.id == principal.id))")
	public ResponseEntity<UserProfileDto> updateUserProfile(
			@Pattern(regexp = "^(me|\\d+)$", message = "Invalid input. Only 'me' or an integer allowed.")
			@PathVariable("userId") String userId,
			@Valid @RequestBody UserProfileDto userProfileDto) {
	// @formatter:on
		log.info("Input userId:{}", userId);

		Long loggedInUserId = authInfoService.getUserId();
		log.debug("URL param: {}, request body param: {}, logged in userId: {}", userId, userProfileDto.getId(),
				loggedInUserId);

		// @formatter:off
		if ((userProfileDto.getId() == null || userId.equals(userProfileDto.getId().toString()))
				&& (authInfoService.isAdmin() || userId.equals(loggedInUserId.toString())
						|| ("me".equalsIgnoreCase(userId) && loggedInUserId.equals(userProfileDto.getId()))
				)
		) {
		// @formatter:on
			// ok
			// this is redundant kept here for explanation as Spring-EL in PreAuthorize would take care of it
		} else {
			// forbidden
			log.error("URL param: {}, request body param: {}, logged in userId: {} are NOT matching", userId,
					userProfileDto.getId(), loggedInUserId);
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); // "Cannot modify info of other users"
		}

		if (userProfileDto.getId() == null) {
			userProfileDto.setId("me".equalsIgnoreCase(userId) ? loggedInUserId : Long.valueOf(userId));
		}

		UserProfileDto result = userProfileService.updateAndMapToDto(userProfileDto);

		return ResponseEntity.ok(result);
	}

}