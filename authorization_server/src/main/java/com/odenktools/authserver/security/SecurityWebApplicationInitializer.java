package com.odenktools.authserver.security;

import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;

/**
 * @author Odenktools
 */
public class SecurityWebApplicationInitializer extends AbstractSecurityWebApplicationInitializer {

	public SecurityWebApplicationInitializer() {
		super(WebSecurityConfig.class);
	}
}
