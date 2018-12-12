package com.odenktools.authserver.security.user;

import com.odenktools.authserver.entity.Customer;
import com.odenktools.authserver.entity.Group;
import com.odenktools.authserver.entity.Permission;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * NOT USE FOR PRINCIPAL = CLIENT_CREDENTIALS.
 * Used By UserDetailsService.
 */
public class PrincipalAdminDetail implements UserDetails {

	private static final Logger LOG = LoggerFactory.getLogger(PrincipalAdminDetail.class);

	private Customer customer;

	PrincipalAdminDetail(Customer customer) {
		this.customer = customer;
		LOG.info("LOGIN_INFO {}", this.customer.getEmail());
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		List<GrantedAuthority> authorities = new ArrayList<>();
		for (Group role : this.customer.getUsersGroups()) {
			// Check role is active?.
			if (role.getIsActive() == 1) {
				// Create Customer Authority.
				authorities.add(new SimpleGrantedAuthority(role.getCoded()));
				for (Permission rolePerm : role.getUsersPermissions()) {
					LOG.info("ROLE_GRANTED {}", rolePerm.getNamePermission());
					authorities.add(new SimpleGrantedAuthority(rolePerm.getNamePermission()));
				}
			} else {
				throw new UsernameNotFoundException("Invalid username or password.");
			}
		}
		return authorities;
	}

	@Override
	public String getPassword() {
		return this.customer.getPassword();
	}

	@Override
	public String getUsername() {
		return this.customer.getUsername();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return this.customer.getIsActive() == 1;
	}
}
