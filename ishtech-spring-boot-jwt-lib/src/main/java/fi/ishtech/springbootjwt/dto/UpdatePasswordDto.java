package fi.ishtech.springbootjwt.dto;

import java.io.Serial;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.ToString;

@Data
public class UpdatePasswordDto implements Serializable {

	@Serial
	private static final long serialVersionUID = 3504854805844405021L;

	@ToString.Exclude
	private String token;

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

}