package com.odenktools.authserver.dto.group;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.odenktools.authserver.entity.Customer;
import com.odenktools.authserver.entity.Permission;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Set;

/**
 * Group DataTransferObject.
 *
 * @author Odenktools.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class GroupDto {

	private Long id;

	@NotNull(message = "named cannot null")
	@NotEmpty(message = "named cannot empty")
	private String named;

	@NotNull(message = "coded cannot null")
	@NotEmpty(message = "coded cannot empty")
	private String coded;

	@NotNull(message = "namedDescription cannot null")
	@NotEmpty(message = "namedDescription cannot empty")
	private String namedDescription;

	private int isActive;

	/**
	 * Permission for Customers (Not For Admin)
	 */
	private Set<Permission> usersPermissions;

	private Set<Customer> users;
}
