package fi.ishtech.springbootjwt.service;

import jakarta.validation.constraints.NotNull;

/**
 * Service interface for user role related operations.
 *
 * @author Muneer Ahmed Syed
 */
public interface UserRoleService {

	/**
	 * Creates default roles for a user.
	 *
	 * @param userId {@link Long}
	 * @return {@code Void}
	 */
	void createDefaultRoles(@NotNull Long userId);

}