package fi.ishtech.springboot.auth.userdetails;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Implementation of {@link UserDetails}
 *
 * @author Muneer Ahmed Syed
 */
@Getter
@Setter(lombok.AccessLevel.PRIVATE)
@NoArgsConstructor
public class UserDetailsImpl implements UserDetails {

	private static final long serialVersionUID = 8045685643481477217L;

	private Long id;

	private String username;

	@ToString.Exclude
	@JsonIgnore
	private String password;

	private Collection<? extends GrantedAuthority> authorities;

	private boolean isAccountNonExpired;

	private boolean isAccountNonLocked;

	private boolean isCredentialsNonExpired;

	private boolean isEnabled;

	public static UserDetails of(Long id, String username, String password, boolean isEnabled) {
		UserDetailsImpl userDetails = new UserDetailsImpl();

		userDetails.setId(id);

		userDetails.setUsername(username);
		userDetails.setPassword(password);

		userDetails.setEnabled(isEnabled);

		userDetails.setAccountNonExpired(isEnabled);
		userDetails.setAccountNonLocked(isEnabled);
		userDetails.setCredentialsNonExpired(isEnabled);

		userDetails.setAuthorities(Arrays.asList(new SimpleGrantedAuthority("ROLE_USER")));

		return userDetails;
	}

	@JsonIgnore
	public List<String> getScopes() {
		return getAuthorities().stream().map(item -> item.getAuthority()).collect(Collectors.toList());
	}

}
