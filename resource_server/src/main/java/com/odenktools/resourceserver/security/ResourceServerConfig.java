package com.odenktools.resourceserver.security;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.Charset;

/**
 * Handling Protected Content.
 *
 * @author Odenktools.
 */
@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

	public final static String RESOURCE_ID = "resource_id";

	private final CustomAccessTokenConverter customAccessTokenConverter;

	@Autowired
	public ResourceServerConfig(CustomAccessTokenConverter customAccessTokenConverter) {
		this.customAccessTokenConverter = customAccessTokenConverter;
	}

	/**
	 * Configure ``Allowed Resource`` only in here.
	 *
	 * @param http HttpSecurity.
	 * @throws Exception Error.
	 */
	@Override
	public void configure(HttpSecurity http) throws Exception {

		http
				.exceptionHandling()
				.authenticationEntryPoint
						((request, response, authException) -> response.sendError(HttpServletResponse.SC_UNAUTHORIZED))
				.and()
				.csrf().disable()
				.headers().frameOptions().disable()
				.and()
				.authorizeRequests()
				.antMatchers("/api/v1/hello/**").permitAll()
				.antMatchers(HttpMethod.GET, "/api/v1/**").access("#oauth2.hasScope('read')")
				.antMatchers(HttpMethod.POST, "/api/v1/**").access("#oauth2.hasScope('write')")
				.antMatchers(HttpMethod.PATCH, "/api/v1/**").access("#oauth2.hasScope('write')")
				.antMatchers(HttpMethod.PUT, "/api/v1/**").access("#oauth2.hasScope('write')")
				.antMatchers(HttpMethod.DELETE, "/api/v1/**").access("#oauth2.hasScope('write')")
				.and()
				.authorizeRequests()
				.anyRequest().authenticated();
	}

	@Bean
	public JwtAccessTokenConverter accessTokenConverter() {
		final JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
		converter.setAccessTokenConverter(customAccessTokenConverter);
		final Resource resource = new ClassPathResource("certificate/pubkey.txt");
		String publicKey = null;
		try {
			publicKey = IOUtils.toString(resource.getInputStream(), Charset.defaultCharset());
		} catch (final IOException e) {
			throw new UncheckedIOException(e);
		}
		converter.setVerifierKey(publicKey);
		return converter;
	}

	/**
	 * ```resource_id``` must match in ```DATABASE```.
	 *
	 * @param resources
	 * @throws Exception
	 */
	@Override
	public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
		resources.tokenServices(tokenServices());
		resources.resourceId(ResourceServerConfig.RESOURCE_ID);
	}

	@Bean
	public TokenStore tokenStore() {
		return new JwtTokenStore(accessTokenConverter());
	}

	@Bean
	@Primary
	public DefaultTokenServices tokenServices() {
		final DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
		defaultTokenServices.setTokenStore(tokenStore());
		return defaultTokenServices;
	}
}
