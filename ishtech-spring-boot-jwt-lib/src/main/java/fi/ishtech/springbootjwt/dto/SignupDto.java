package fi.ishtech.springbootjwt.dto;

import java.io.Serial;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.ToString;

/**
 *
 * @author Muneer Ahmed Syed
 */
@Data
public class SignupDto implements Serializable {

	@Serial
	private static final long serialVersionUID = -7883197620560603955L;

	@NotBlank
	private String firstName;

	@NotBlank
	private String lastName;

	private String username;

	@NotBlank
	@Email
	private String email;

	@ToString.Exclude
	@NotBlank
	private String password;

	@ToString.Exclude
	@NotBlank
	private String passwordConfirm;

	/**
	 * Checks that {@link #password} and {@link #passwordConfirm} are same.
	 */
	@AssertTrue(message = "password and passwordConfirm are not matching")
	@JsonIgnore
	public boolean validatePasswordAndPasswordConfirmMatch() {
		return password.equals(passwordConfirm);
	}

	/**
	 * I18N language code, e.g. {@code en}, {@code fi} etc.
	 */
	private String lang;

	/**
	 * Validates input is I18N language code when present.
	 */
	@AssertTrue(message = "lang must be exactly 2 lowercase letters")
	@JsonIgnore
	public boolean validateLang() {
		return lang != null && !lang.isBlank() ? lang.matches("^[a-z]{2}$") : true;
	}

	@AssertTrue
	private boolean acceptTermsConditions;

}