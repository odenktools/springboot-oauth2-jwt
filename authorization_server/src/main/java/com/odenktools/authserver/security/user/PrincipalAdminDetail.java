package com.odenktools.authserver.security.user;

import com.odenktools.authserver.entity.Customer;
import com.odenktools.authserver.entity.Group;
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
 * PRINCIPAL != CLIENT_CREDENTIALS.
 * Used By UserDetailsService.
 */
public class PrincipalAdminDetail implements UserDetails {

  private static final Logger LOG = LoggerFactory.getLogger(PrincipalAdminDetail.class);

  private Customer customer;

  PrincipalAdminDetail(Customer customer) {
    this.customer = customer;
    LOG.info("LOGIN INFO {}", this.customer.getEmail());
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    List<GrantedAuthority> authorities = new ArrayList<>();
    for (Group role : this.customer.getUsersGroups()) {
      LOG.info("GrantedAuthority INFO {}", role.getCoded());
      //-- Check apakah role tersebut aktif atau tidak.
      if (role.getIsActive() == 1) {
        //-- Tambahkan Default Otoritas berdasarkan CODED (BUKAN NAMED).
        authorities.add(new SimpleGrantedAuthority(role.getCoded()));
        //-- Tambahkan Otoritas Tambahan berdasarkan ADMIN YANG MEMBUAT
        authorities.addAll(role.getUsersPermissions()
            .stream()
            .map(p -> new SimpleGrantedAuthority(p.getNamePermission()))
            .collect(Collectors.toList()));
      }
      else {
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
