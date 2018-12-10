package com.odenktools.authserver.security;

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
import org.springframework.http.HttpMethod;
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

  @Autowired
  @Qualifier("authenticationManagerBean")
  private AuthenticationManager authenticationManager;

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
   * Akses TABLE oauth_code.
   *
   * @return AuthorizationCodeServices
   */
  @Bean
  public AuthorizationCodeServices authorizationCodeServices() {

    return new CustomJdbcAuthorizationCodeServices(this.dataSource);
  }

  @Bean
  public DefaultTokenServices tokenServices() {

    final DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
    defaultTokenServices.setTokenStore(tokenStore());
    defaultTokenServices.setSupportRefreshToken(true);
    return defaultTokenServices;
  }

  @Bean
  @Primary
  public PasswordEncoder passwordEncoder() {

    return new CustomPasswordEncoder();
  }

  @Bean
  public JwtAccessTokenConverter accessTokenConverter() {

    JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
    KeyPair keyPair = new KeyStoreKeyFactory(
        new ClassPathResource("certificate/jwt.p12"),
        "odenktools123".toCharArray())
        .getKeyPair("jwt");
    converter.setKeyPair(keyPair);
    return converter;
  }

  @Override
  public void configure(ClientDetailsServiceConfigurer configurer) throws Exception {

    //configurer.withClientDetails(clientDetailsService());

    configurer
        .jdbc(this.dataSource)
        .passwordEncoder(this.passwordEncoder());
  }

  /**
   * Agar bisa checktoken.
   * {{api.url}}/oauth/check_token (INI HARUS MENGGUNAKAN BASIC AUTH LAGI)
   *
   * @param oauthServer oauth2
   */
  @Override
  public void configure(AuthorizationServerSecurityConfigurer oauthServer) {

    oauthServer.passwordEncoder(this.passwordEncoder())
        .tokenKeyAccess("permitAll()")
        .checkTokenAccess("isAuthenticated()");
  }

  @Override
  public void configure(AuthorizationServerEndpointsConfigurer endpoints) {

    Collection<TokenEnhancer> tokenEnhancers = applicationContext.getBeansOfType(TokenEnhancer.class).values();
    TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
    tokenEnhancerChain.setTokenEnhancers(new ArrayList<>(tokenEnhancers));
    endpoints
        .allowedTokenEndpointRequestMethods(HttpMethod.GET, HttpMethod.POST)
        .authenticationManager(authenticationManager)
        .approvalStoreDisabled()
        .tokenStore(this.tokenStore())
        .tokenEnhancer(tokenEnhancerChain)
        .accessTokenConverter(this.accessTokenConverter())
        .authorizationCodeServices(this.authorizationCodeServices());
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
