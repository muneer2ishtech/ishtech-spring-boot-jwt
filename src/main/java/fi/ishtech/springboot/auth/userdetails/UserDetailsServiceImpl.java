package fi.ishtech.springboot.auth.userdetails;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fi.ishtech.springboot.auth.entity.User;
import fi.ishtech.springboot.auth.entity.UserRole;
import fi.ishtech.springboot.auth.repo.UserRepo;
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
		User user = (loginByEmail ? userRepo.findOneByUsername(username) : userRepo.findOneByEmail(username))
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