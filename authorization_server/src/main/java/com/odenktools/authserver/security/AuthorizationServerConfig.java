package com.odenktools.authserver.security;

import com.odenktools.authserver.security.user.CustomClientDetailsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;

import javax.sql.DataSource;
import java.security.KeyPair;
import java.util.ArrayList;
import java.util.Collection;

/**
 * The server issuing access tokens to the client after successfully,
 * authenticating the resource owner and obtaining authorization.
 *
 * @author Odenktools.
 */
@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter implements ApplicationContextAware {

	private static final Logger LOG = LoggerFactory.getLogger(AuthorizationServerConfig.class);

	private ApplicationContext applicationContext;

	private final DataSource dataSource;

	private final static String PASS_KEY = "odenktools123";

	@Autowired
	@Qualifier("authenticationManagerBean")
	private AuthenticationManager authenticationManager;

	@Autowired
	@Qualifier("scopeMappingOAuth2RequestFactory")
	private ScopeMappingOAuth2RequestFactory defaultOAuth2RequestFactory;

	@Bean
	public TokenStore tokenStore() {
		return new JdbcTokenStore(this.dataSource);
	}

	public AuthorizationServerConfig(DataSource dataSource) {

		this.dataSource = dataSource;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	/**
	 * Access TABLE oauth_code.
	 *
	 * @return AuthorizationCodeServices
	 */
	@Bean
	public AuthorizationCodeServices authorizationCodeServices() {

		return new CustomJdbcAuthorizationCodeServices(this.dataSource);
	}

	/**
	 * Oauth2 ClientDetail (Not fully working now), you can disable this feature.
	 *
	 * @return ClientDetailsService.
	 */
	@Bean
	public ClientDetailsService clientDetailsService() {

		CustomClientDetailsService client = new CustomClientDetailsService(this.dataSource);
		client.setPasswordEncoder(this.passwordEncoder());
		return client;
	}

	@Override
	public void configure(ClientDetailsServiceConfigurer configurer) throws Exception {

		//Not fully working, you can disable for now.
		configurer.withClientDetails(this.clientDetailsService());
		//configurer.jdbc(this.dataSource).passwordEncoder(this.passwordEncoder());
	}

	/*@Bean
	public DefaultOAuth2RequestFactory oAuth2RequestFactory() throws Exception {
		return new ScopeMappingOAuth2RequestFactory(
				this.clientDetailsService());
	}*/

	/**
	 * TokenService.
	 *
	 * @return DefaultTokenServices.
	 */
	@Bean
	public DefaultTokenServices tokenServices() {

		final DefaultTokenServices defaultTokenServices = new DefaultTokenServices();

		//Not fully working, you can disable for now.
		defaultTokenServices.setClientDetailsService(this.clientDetailsService());
		defaultTokenServices.setAuthenticationManager(this.authenticationManager);
		defaultTokenServices.setTokenStore(this.tokenStore());
		defaultTokenServices.setTokenEnhancer(this.tokenEnhancerChain());
		defaultTokenServices.setSupportRefreshToken(true);
		return defaultTokenServices;
	}

	/**
	 * Custom Password Encoded, We not use Bcrypt for encoded.
	 *
	 * @return CustomPasswordEncoder.
	 */
	@Bean
	@Primary
	public PasswordEncoder passwordEncoder() {

		return new CustomPasswordEncoder();
	}

	/**
	 * Convert default token to JWTToken.
	 *
	 * @return JwtAccessTokenConverter.
	 */
	@Bean
	public JwtAccessTokenConverter accessTokenConverter() {

		JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
		KeyPair keyPair = new KeyStoreKeyFactory(
				new ClassPathResource("certificate/jwt.p12"),
				PASS_KEY.toCharArray())
				.getKeyPair("jwt");
		//Setup keypair
		converter.setKeyPair(keyPair);
		return converter;
	}

	/**
	 * Customer must have ```CheckToken permission```.
	 * {{api.url}}/oauth/check_token
	 *
	 * @param oauthServer oauth2
	 */
	@Override
	public void configure(AuthorizationServerSecurityConfigurer oauthServer) {

		oauthServer.passwordEncoder(this.passwordEncoder())
				.tokenKeyAccess("permitAll()")
				.checkTokenAccess("isAuthenticated()");
	}

	@Bean
	public TokenEnhancerChain tokenEnhancerChain() {
		final Collection<TokenEnhancer> tokenEnhancers =
				applicationContext.getBeansOfType(TokenEnhancer.class).values();
		TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
		tokenEnhancerChain.setTokenEnhancers(new ArrayList<>(tokenEnhancers));
		return tokenEnhancerChain;
	}

	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {

		// @formatter:off
		endpoints
				.authenticationManager(this.authenticationManager)
				.approvalStoreDisabled()
				.requestFactory(this.defaultOAuth2RequestFactory)
				.tokenStore(this.tokenStore())
				.tokenEnhancer(this.tokenEnhancerChain())
				.accessTokenConverter(this.accessTokenConverter())
				.authorizationCodeServices(this.authorizationCodeServices());
		// @formatter:on
	}

	@EventListener
	public void authSuccessEventListener(AuthenticationSuccessEvent event) {

		Authentication auth = event.getAuthentication();
		LOG.debug("LOGIN SUCCESS [CRED = {}] [PRINCIPAL = {}] ", auth.getDetails(), auth.getPrincipal());
	}

	@EventListener
	public void authFailedEventListener(AbstractAuthenticationFailureEvent event) {

		Authentication auth = event.getAuthentication();

		LOG.debug("LOGIN FAILED [CRED = {}] [PRINCIPAL = {}] ", auth.getCredentials(), auth.getPrincipal());
	}

  /*class CustomOauth2RequestFactory extends DefaultOAuth2RequestFactory {
    @Autowired
    private TokenStore tokenStore;

    public CustomOauth2RequestFactory(ClientDetailsService clientDetailsService) {
      super(clientDetailsService);
    }

    @Override
    public TokenRequest createTokenRequest(Map<String, String> requestParameters,
                                           ClientDetails authenticatedClient) {
      if (requestParameters.get("grant_type").equals("refresh_token")) {
        OAuth2Authentication authentication = tokenStore.readAuthenticationForRefreshToken(
            tokenStore.readRefreshToken(requestParameters.get("refresh_token")));
        SecurityContextHolder.getContext()
            .setAuthentication(new UsernamePasswordAuthenticationToken(authentication.getName(), null,
                userDetailsService.loadUserByUsername(authentication.getName()).getAuthorities()));
      }
      return super.createTokenRequest(requestParameters, authenticatedClient);
    }
  }*/

	/*@EnableResourceServer
	public static class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {

	}*/
}
