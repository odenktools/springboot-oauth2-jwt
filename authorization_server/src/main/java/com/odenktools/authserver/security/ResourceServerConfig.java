package com.odenktools.authserver.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;

import javax.servlet.http.HttpServletResponse;

/**
 * Handling Protected Content.
 *
 * @author Odenktools.
 */
@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

  public final static String RESOURCE_ID = "resource_id";

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
        .and()
        .authorizeRequests()
        .anyRequest().authenticated();
  }

  /**
   * ```resource_id``` must match in ```DATABASE```.
   *
   * @param resources
   * @throws Exception
   */
  @Override
  public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
    resources.resourceId(ResourceServerConfig.RESOURCE_ID);
  }
}
