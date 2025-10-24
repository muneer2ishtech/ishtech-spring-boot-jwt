package fi.ishtech.springbootjwt.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import fi.ishtech.springbootjwt.dto.SignupDto;
import fi.ishtech.springbootjwt.dto.UserProfileDto;
import fi.ishtech.springbootjwt.entity.UserProfile;
import fi.ishtech.springbootjwt.mapper.UserProfileMapper;
import fi.ishtech.springbootjwt.repo.UserProfileRepo;
import fi.ishtech.springbootjwt.service.UserProfileService;
import jakarta.transaction.Transactional;
import jakarta.transaction.Transactional.TxType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author Muneer Ahmed Syed
 */
@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
@Validated
public class UserProfileServiceImpl implements UserProfileService {

	private final UserProfileRepo userProfileRepo;
	private final UserProfileMapper userProfileMapper;

	@Transactional(TxType.MANDATORY)
	@Override
	public UserProfileDto create(Long userId, SignupDto signupDto) {
		log.debug("Creating new UserProfile for User({})", userId);

		UserProfile userProfile = userProfileMapper.toNewEntity(signupDto);
		userProfile.setId(userId);

		userProfile = userProfileRepo.save(userProfile);
		log.info("Created new UserProfile({})", userProfile.getId());

		UserProfileDto userProfileDto = userProfileMapper.toBriefDto(userProfile);

		return userProfileDto;
	}

}