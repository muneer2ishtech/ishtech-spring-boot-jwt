package fi.ishtech.springbootjwt.service.impl;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import fi.ishtech.springbootjwt.entity.UserRole;
import fi.ishtech.springbootjwt.repo.UserRoleRepo;
import fi.ishtech.springbootjwt.service.UserRoleService;
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
public class UserRoleServiceImpl implements UserRoleService {

	private final UserRoleRepo userRoleRepo;

	private static final String[] DEFAULT_ROLES = { "USER" };

	@Transactional(TxType.MANDATORY)
	@Override
	public void createDefaultRoles(Long userId) {
		log.debug("Creating default roles for User({})", userId);

		List<UserRole> userRoles = Arrays.stream(DEFAULT_ROLES).map(roleName -> {
			UserRole userRole = new UserRole();
			userRole.setUserId(userId);
			userRole.setRoleName(roleName);
			userRole.setActive(true);
			return userRole;
		}).toList();

		log.info("Created default roles for User({})", userId);

		userRoleRepo.saveAll(userRoles);
	}

}