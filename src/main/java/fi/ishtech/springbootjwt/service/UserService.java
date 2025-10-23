package fi.ishtech.springbootjwt.service;

import org.springframework.data.util.Pair;

import fi.ishtech.springbootjwt.dto.ForgotPasswordDto;
import fi.ishtech.springbootjwt.dto.SignupDto;
import fi.ishtech.springbootjwt.dto.UpdatePasswordDto;
import fi.ishtech.springbootjwt.dto.UserProfileDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Service interface for user related operations.
 *
 * @author Muneer Ahmed Syed
 */
public interface UserService {

	/**
	 * Checks if a user exists with the given email.
	 *
	 * @param email {@link String}
	 * @return {@code boolean}
	 */
	boolean existsByEmail(@NotBlank String email);

	/**
	 * Checks if a user exists with the given username.
	 *
	 * @param username {@link String}
	 * @return {@code boolean}
	 */
	boolean existsByUsername(@NotBlank String username);

	/**
	 * Creates a new user from the signup data.
	 *
	 * @param signupDto {@link SignupDto}
	 * @return {@link UserProfileDto}
	 */
	UserProfileDto create(@NotNull @Valid SignupDto signupDto);

	/**
	 * Updates the password for a user identified by user ID.
	 *
	 * @param userId            {@link Long}
	 * @param updatePasswordDto {@link UpdatePasswordDto}
	 * @return {@link UserProfileDto}
	 */
	UserProfileDto updatePassword(@NotNull Long userId, @NotNull @Valid UpdatePasswordDto updatePasswordDto);

	/**
	 * Updates the password for a user using a token.
	 *
	 * @param updatePasswordDto {@link UpdatePasswordDto}
	 * @return {@link UserProfileDto}
	 */
	UserProfileDto updatePasswordByToken(@NotNull @Valid UpdatePasswordDto updatePasswordDto);

	/**
	 * Generates a password reset token for the user.
	 *
	 * @param forgotPasswordDto {@link ForgotPasswordDto}
	 * @return {@link Pair}&lt; {@link String}, {@link UserProfileDto}&gt;
	 */
	Pair<String, UserProfileDto> generatePasswordResetToken(@NotNull @Valid ForgotPasswordDto forgotPasswordDto);

}