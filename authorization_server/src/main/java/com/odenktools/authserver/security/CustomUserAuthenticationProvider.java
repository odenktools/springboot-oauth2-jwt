package com.odenktools.authserver.security;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;

import java.util.ArrayList;
import java.util.List;

public class CustomUserAuthenticationProvider implements AuthenticationProvider {

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {

		List<GrantedAuthority> grantedAuthorities = new ArrayList();
		UsernamePasswordAuthenticationToken auth = new
				UsernamePasswordAuthenticationToken(authentication.getPrincipal(),
				authentication.getCredentials(), grantedAuthorities);
		return auth;
	}

	@Override
	public boolean supports(Class<?> authentication) {

		return true;
	}

}
