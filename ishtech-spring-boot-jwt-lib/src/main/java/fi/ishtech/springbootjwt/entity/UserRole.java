package fi.ishtech.springbootjwt.entity;

import java.io.Serial;
import java.io.Serializable;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 *
 * @author Muneer Ahmed Syed
 */
@Entity
@Table(name = "t_user_role", uniqueConstraints = {
		@UniqueConstraint(name = "uk_user_role_user_id_role", columnNames = { "user_id", "role_name" }) })
@DynamicInsert
@DynamicUpdate
@Data
public class UserRole implements Serializable {

	@Serial
	private static final long serialVersionUID = 419376785102679249L;

	@Id
	@Column(nullable = false, updatable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "user_id", nullable = false, insertable = true, updatable = false)
	private Long userId;

	@Column(name = "role_name", nullable = false, insertable = true, updatable = false)
	private String roleName;

	@EqualsAndHashCode.Exclude
	@ToString.Exclude
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", referencedColumnName = "id", nullable = true, insertable = false, updatable = false, foreignKey = @ForeignKey(name = "fk_user_role_user"))
	private User user;

	@Column(name = "is_active", nullable = false, insertable = true, updatable = true)
	private boolean isActive;

}