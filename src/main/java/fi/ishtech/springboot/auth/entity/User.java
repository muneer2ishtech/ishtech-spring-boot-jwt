package fi.ishtech.springboot.auth.entity;

import java.io.Serializable;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * {@link Entity} for DB table for user
 *
 * @author Muneer Ahmed Syed
 */
@Entity
@Table(name = "t_user")
@DynamicInsert
@DynamicUpdate
@Data
public class User implements Serializable {

	private static final long serialVersionUID = -6637213531718295584L;

	@Id
	@Column(updatable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, updatable = false, unique = true)
	private String username;

	@EqualsAndHashCode.Exclude
	@ToString.Exclude
	@JsonIgnore
	@Column(nullable = false, updatable = false)
	private String passwordHash;

	@EqualsAndHashCode.Exclude
	@Column(nullable = false)
	private String firstName;

	@EqualsAndHashCode.Exclude
	@Column(nullable = false)
	private String lastName;

	@Column(name = "is_disabled", nullable = false)
	private boolean disabled;

}
