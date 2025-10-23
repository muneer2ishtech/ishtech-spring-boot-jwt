package fi.ishtech.springboot.auth.dto;

import java.io.Serial;
import java.io.Serializable;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SigninDto implements Serializable {

	@Serial
	private static final long serialVersionUID = -7571955490506238597L;

	// TODO: either username or email is mandatory
	private String username;

	@NotBlank
	@Email
	private String email;

	@NotBlank
	private String password;

}