package com.odenktools.authserver.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Odenktools
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
		prePostEnabled = true,
		securedEnabled = true
)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	//private static final Logger Logger = LoggerFactory.getLogger(WebSecurityConfig.class);

	@Resource(name = "customUserDetailsService")
	private UserDetailsService userDetailsService;

	//private CustomUserDetailsService customUserDetailsService;

	/**
	 * If grant_type != client_credentials.
	 *
	 * @param auth AuthenticationManagerBuilder
	 * @throws Exception Error jika data tidak sesuai.
	 */
	@Autowired
	public void globalUserDetails(AuthenticationManagerBuilder auth) throws Exception {

		auth
				.userDetailsService(this.userDetailsService)
				.passwordEncoder(new CustomPasswordEncoder());
	}

	@Override
	@Bean
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

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
				.antMatchers("/actuator/**").permitAll()
				.antMatchers("/swagger-ui/index.html").permitAll()
				.and()
				.authorizeRequests()
				.anyRequest().authenticated();
	}

  /*@Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.userDetailsService(this.customUserDetailsService)
        .passwordEncoder(new CustomPasswordEncoder());
  }*/

}
