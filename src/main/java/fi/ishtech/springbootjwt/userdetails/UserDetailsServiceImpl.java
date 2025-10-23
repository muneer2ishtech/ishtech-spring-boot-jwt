package fi.ishtech.springbootjwt.userdetails;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fi.ishtech.springbootjwt.entity.User;
import fi.ishtech.springbootjwt.entity.UserRole;
import fi.ishtech.springbootjwt.repo.UserRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {

	@Value("${fi.istech.springboot.auth.login-by-email:true}")
	private boolean loginByEmail;

	private final UserRepo userRepo;

	@Override
	@Transactional
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		log.debug("load user by username: {}", username);

		User user = (loginByEmail ? userRepo.findOneByEmail(username) : userRepo.findOneByUsername(username))
				.orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));

		// @formatter:off
		List<String> userRoleNames = user.getUserRoles() == null ? List.of()
				: user.getUserRoles()
						.stream()
						.filter(UserRole::isActive)
						.map(UserRole::getRoleName)
						.toList();
		// @formatter:on

		var userProfile = user.getUserProfile();

		// @formatter:off
		return UserDetailsImpl.of(user.getId(), user.getUsername(), user.getEmail(), user.getPasswordHash(),
				!user.isActive(), userRoleNames,
				userProfile == null ? null : userProfile.getFullName(),
				userProfile == null ? null : userProfile.getDefaultLang());
		// @formatter:on
	}

}