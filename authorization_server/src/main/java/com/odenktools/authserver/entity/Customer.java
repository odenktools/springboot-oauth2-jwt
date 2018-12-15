package com.odenktools.authserver.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

/**
 * Handling Permission for END USER (Customer)
 */
@Getter
@Setter
@JsonSerialize
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity(name = "Customer")
@Table(name = "customers")
@JsonIgnoreProperties(
		ignoreUnknown = true,
		value = {"createdAt", "updatedAt"},
		allowGetters = true
)
@SuppressWarnings("unused")
public class Customer implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_users", nullable = false)
	private Long id;

	@NotBlank
	@NotNull
	@Size(min = 2, max = 100)
	@Column(name = "username", unique = true, nullable = false)
	private String username;

	@NotBlank
	@NotNull
	@Size(min = 2, max = 255)
	@Column(name = "email", unique = true, nullable = false)
	private String email;

	@NotBlank
	@NotNull
	@Size(max = 255)
	@Column(name = "phone_number", unique = true, nullable = false)
	private String phoneNumber;

	@NotBlank
	@NotNull
	@Size(max = 255)
	@Column(name = "password", unique = true, nullable = false)
	private String password;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created_at", updatable = false)
	private Date createdAt;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "updated_at")
	private Date updatedAt;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "last_login")
	private Date lastLogin;

	@Column(name = "is_active", columnDefinition = "default int 0")
	@JsonProperty("is_active")
	private int isActive;

	@Column(name = "is_verified", columnDefinition = "default int 0")
	@JsonProperty("is_verified")
	private int isVerified;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "customers_groups_rel",
			joinColumns = @JoinColumn(name = "user_id"),
			inverseJoinColumns = @JoinColumn(name = "group_id")
	)
	private Set<Group> usersGroups;

	/**
	 * Sets created_at before insert.
	 */
	@PrePersist
	public void setCreationDate() {

		this.createdAt = new Date();
	}

	/**
	 * Sets updated_at before update.
	 */
	@PreUpdate
	public void setChangedDate() {

		this.updatedAt = new Date();
	}

	public void customAddGroups(Group group) {

		this.usersGroups.add(group);
		group.getUsers().add(this);
	}

	public void customRemoveGroups(Group group) {

		this.usersGroups.remove(usersGroups);
		group.getUsers().remove(this);
	}
}

