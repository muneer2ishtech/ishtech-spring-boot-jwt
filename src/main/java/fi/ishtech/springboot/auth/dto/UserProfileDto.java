package fi.ishtech.springboot.auth.dto;

import java.io.Serial;
import java.io.Serializable;

import lombok.Data;

/**
 * @author Muneer Ahmed Syed
 */
@Data
public class UserProfileDto implements Serializable {

	@Serial
	private static final long serialVersionUID = -4206245277207802646L;

	private Long id;

	private String email;

	private String firstName;

	private String middleName;

	private String lastName;

	private String defaultLang;

	public String getFullName() {
		if (middleName != null && !middleName.isBlank()) {
			return String.join(" ", firstName.strip(), middleName.strip(), lastName.strip());
		} else {
			return String.join(" ", firstName.strip(), lastName.strip());
		}
	}

}