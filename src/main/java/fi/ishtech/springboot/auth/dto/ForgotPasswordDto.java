package fi.ishtech.springboot.auth.dto;

import java.io.Serial;
import java.io.Serializable;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 *
 * @author Muneer Ahmed Syed
 */
@Data
public class ForgotPasswordDto implements Serializable {

	@Serial
	private static final long serialVersionUID = -7311190433437016528L;

	@NotBlank
	@Email
	private String email;

}