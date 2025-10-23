package fi.ishtech.springbootjwt.service;

import fi.ishtech.springbootjwt.dto.SignupDto;
import fi.ishtech.springbootjwt.dto.UserProfileDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

/**
 * Service interface for user profile related operations.
 *
 * @author Muneer Ahmed Syed
 * 
 */
public interface UserProfileService {

	/**
	 * Creates a user profile from the provided signup data.
	 *
	 * @param userId    the ID of the user
	 * @param signupDto the signup data transfer object
	 * @return UserProfileDto
	 */
	UserProfileDto create(@NotNull Long userId, @NotNull @Valid SignupDto signupDto);

}