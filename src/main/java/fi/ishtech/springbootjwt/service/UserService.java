package fi.ishtech.springbootjwt.service;

import org.springframework.data.util.Pair;

import fi.ishtech.springbootjwt.dto.ForgotPasswordDto;
import fi.ishtech.springbootjwt.dto.SignupDto;
import fi.ishtech.springbootjwt.dto.UpdatePasswordDto;
import fi.ishtech.springbootjwt.dto.UserProfileDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public interface UserService {

	boolean existsByEmail(@NotBlank String email);

	boolean existsByUsername(@NotBlank String username);

	UserProfileDto create(@NotNull @Valid SignupDto signupDto);

	UserProfileDto updatePassword(@NotNull Long userId, @NotNull @Valid UpdatePasswordDto updatePasswordDto);

	UserProfileDto updatePasswordByToken(@NotNull @Valid UpdatePasswordDto updatePasswordDto);

	Pair<String, UserProfileDto> generatePasswordResetToken(@NotNull @Valid ForgotPasswordDto forgotPasswordDto);

}