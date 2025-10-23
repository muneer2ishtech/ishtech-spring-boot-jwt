package fi.ishtech.springboot.auth.dto;

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

	@AssertTrue(message = "password and passwordConfirm are not matching")
	@JsonIgnore
	public boolean isPasswordAndPasswordConfirmMatch() {
		return password.equals(passwordConfirm);
	}

	// TODO: regex to match {a-z}[2]
	private String lang;

	@AssertTrue
	private boolean acceptTermsConditions;

}