package fi.ishtech.springboot.auth.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import fi.ishtech.springboot.auth.entity.User;

/**
 * {@link Repository} for entity {@link User}
 *
 * @author Muneer Ahmed Syed
 */
public interface UserRepo extends JpaRepository<User, Long> {

	boolean existsByUsername(String username);

	boolean existsByEmail(String email);

	Optional<User> findOneByUsername(String username);

	Optional<User> findOneByEmail(String email);

	Optional<User> findOneByPasswordResetToken(String token);

	@Query("SELECT u.passwordHash FROM User u WHERE u.id = :userId")
	String findPasswordHashById(Long userId);

	@Modifying
	@Query("UPDATE User u SET u.passwordHash = :newPassword WHERE u.id = :userId")
	void updatePassword(Long userId, String newPassword);

}