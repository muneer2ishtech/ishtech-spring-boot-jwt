package fi.ishtech.springbootjwt.service.impl;

import java.security.SecureRandom;
import java.util.Base64;

import org.springframework.data.util.Pair;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import fi.ishtech.springbootjwt.dto.ForgotPasswordDto;
import fi.ishtech.springbootjwt.dto.SignupDto;
import fi.ishtech.springbootjwt.dto.UpdatePasswordDto;
import fi.ishtech.springbootjwt.dto.UserProfileDto;
import fi.ishtech.springbootjwt.entity.User;
import fi.ishtech.springbootjwt.repo.UserRepo;
import fi.ishtech.springbootjwt.service.UserService;
import io.jsonwebtoken.lang.Assert;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
@Validated
public class UserServiceImpl implements UserService {

	private final UserRepo userRepo;
	private final PasswordEncoder passwordEncoder;

	@Override
	public boolean existsByEmail(String email) {
		return userRepo.existsByEmail(email);
	}

	@Override
	public boolean existsByUsername(String username) {
		return userRepo.existsByUsername(username);
	}

	@Override
	public UserProfileDto create(SignupDto signupDto) {
		log.debug("New Signup: {}", signupDto.getEmail());

		User user = null; // TODO: dto to entity
		user.setPasswordHash(passwordEncoder.encode(signupDto.getPassword()));

		user = userRepo.save(user);
		log.info("New User({}) created for {}", user.getId(), user.getUsername());

		// TODO: default UserRole
		// TODO: UserProfile

		UserProfileDto userProfileDto = null; // TODO to dto

		return userProfileDto;
	}

	@Override
	public UserProfileDto updatePassword(Long userId, UpdatePasswordDto updatePasswordDto) {
		log.debug("Update password request for {}", userId);

		User user = userRepo.findById(userId).orElseThrow();

		user.setPasswordHash(passwordEncoder.encode(updatePasswordDto.getPassword()));
		user.setPasswordResetToken(null); // TODO: effect of DynamicUpdate

		user = userRepo.save(user);
		log.info("Password updated for user:{}", user.getId());

		UserProfileDto userProfileDto = null; // TODO to dto

		return userProfileDto;
	}

	@Override
	public UserProfileDto updatePasswordByToken(UpdatePasswordDto updatePasswordDto) {
		Assert.hasText(updatePasswordDto.getToken(), "Token is mandatory");

		User user = userRepo.findOneByPasswordResetToken(updatePasswordDto.getToken()).orElseThrow();
		log.debug("Update password request for {}", user.getId());

		user.setPasswordHash(passwordEncoder.encode(updatePasswordDto.getPassword()));
		user.setPasswordResetToken(null); // TODO: effect of DynamicUpdate
		user.setForceChangePassword(false);

		user = userRepo.save(user);
		log.info("Password updated for user:{}", user.getId());

		UserProfileDto userProfileDto = null; // TODO to dto

		return userProfileDto;
	}

	@Override
	public Pair<String, UserProfileDto> generatePasswordResetToken(ForgotPasswordDto forgotPasswordDto) {
		log.debug("PasswordResetToken request for: {}", forgotPasswordDto.getEmail());

		User user = userRepo.findOneByEmail(forgotPasswordDto.getEmail()).orElseThrow();
		log.debug("PasswordResetToken request for {}", user.getId());

		String passwordResetToken = generateRandomToken();
		user.setPasswordResetToken(passwordResetToken);
		user.setForceChangePassword(true);
		// TODO: should password be jumbled prevent normal login

		UserProfileDto userProfileDto = null; // TODO to dto

		return Pair.of(passwordResetToken, userProfileDto);
	}

	private String generateRandomToken() {
		return generateBase64UrlEncodedRandomString(32) + "_" + (System.currentTimeMillis() / 1000L);
	}

	/**
	 * TODO: move to Util
	 * 
	 * @param length
	 * @return base64 url encoded string
	 */
	private static String generateBase64UrlEncodedRandomString(int length) {
		byte[] bytes = new byte[length];
		new SecureRandom().nextBytes(bytes);
		return Base64.getUrlEncoder().encodeToString(bytes).substring(0, length - 1);
	}

}