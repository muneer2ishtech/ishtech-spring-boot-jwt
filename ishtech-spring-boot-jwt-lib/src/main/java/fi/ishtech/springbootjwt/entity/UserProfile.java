package fi.ishtech.springbootjwt.entity;

import java.io.Serial;
import java.io.Serializable;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author Muneer Ahmed Syed
 */
@Entity
@Table(name = "t_user_profile")
@DynamicInsert
@DynamicUpdate
@Data
public class UserProfile implements Serializable {

	@Serial
	private static final long serialVersionUID = -6549501435122538676L;

	@Id
	@Column(name = "id", nullable = false, insertable = true, updatable = false)
	private Long id;

	@Column(name = "first_name", nullable = false, insertable = true, updatable = true)
	private String firstName;

	@Column(name = "middle_name", nullable = true, insertable = true, updatable = true)
	private String middleName;

	@Column(name = "last_name", nullable = false, insertable = true, updatable = true)
	private String lastName;

	@Column(name = "default_lang", length = 2, nullable = false, insertable = true, updatable = true)
	private String defaultLang;

	@EqualsAndHashCode.Exclude
	@ToString.Exclude
	@ManyToOne(optional = false, fetch = FetchType.EAGER)
	@JoinColumn(name = "id", referencedColumnName = "id", nullable = true, insertable = false, updatable = false,
			foreignKey = @ForeignKey(name = "fk_user_profile_user"))
	private User user;

	public String getFullName() {
		if (middleName != null && !middleName.isBlank()) {
			return String.join(" ", firstName.strip(), middleName.strip(), lastName.strip());
		} else {
			return String.join(" ", firstName.strip(), lastName.strip());
		}
	}

	public String getEmail() {
		return getUser() == null ? null : user.getEmail();
	}

}