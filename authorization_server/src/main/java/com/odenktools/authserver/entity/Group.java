package com.odenktools.authserver.entity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.io.Serializable;
import java.util.Set;

@Getter
@Setter
@JsonSerialize
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity(name = "Group")
@Table(name = "groups",
		uniqueConstraints = {@UniqueConstraint(columnNames = {"named", "coded"})})
public class Group implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	private Long id;

	@Column(name = "named", nullable = false)
	private String named;

	/**
	 * Alias untuk kolum ``named``. ini digunakan agar data tetap konstant, tidak berpengaruh oleh update.
	 * Ini harus digenerate ``UNIQUE`` berdasarkan kolum ``named``.
	 * Misalkan :
	 * named = Admin Mobile
	 * coded = ROLE_ADMIN_MOBILE (UPPERCASE, hapus SPACE menjadi UNDERSCORES, Tambahkan ROLE_)
	 * </p>
	 */
	@Column(name = "coded", nullable = false, updatable = false)
	private String coded;

	@Column(name = "named_description")
	private String namedDescription;

	@Column(name = "is_active", nullable = false)
	private int isActive;

	/**
	 * Permission for Customers (Not For Admin)
	 */
	@ManyToMany(fetch = FetchType.EAGER, cascade = {
			CascadeType.PERSIST,
			CascadeType.MERGE
	})
	@JoinTable(name = "permissions_rel",
			joinColumns = @JoinColumn(name = "role_id"),
			inverseJoinColumns = @JoinColumn(name = "perm_id")
	)
	private Set<Permission> usersPermissions;

	@ManyToMany(mappedBy = "usersGroups")
	private Set<Customer> users;

	public void customAddPermission(Permission usersPermissions) {

		this.usersPermissions.add(usersPermissions);
		usersPermissions.getUsersGroups().add(this);
	}

	public void customRemovePermission(Permission usersPermissions) {

		this.usersPermissions.remove(usersPermissions);
		usersPermissions.getUsersGroups().remove(this);
	}

	public void customAddUsers(Customer user) {

		this.users.add(user);
		user.getUsersGroups().add(this);
	}

	public void customRemoveUsers(Customer user) {

		this.users.remove(user);
		user.getUsersGroups().remove(this);
	}
}
