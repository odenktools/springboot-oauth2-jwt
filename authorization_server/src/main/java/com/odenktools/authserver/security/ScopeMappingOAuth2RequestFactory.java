package com.odenktools.authserver.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.oauth2.provider.*;
import org.springframework.security.oauth2.provider.request.DefaultOAuth2RequestFactory;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class ScopeMappingOAuth2RequestFactory extends DefaultOAuth2RequestFactory {

	private static final Logger LOG = LoggerFactory.getLogger(ScopeMappingOAuth2RequestFactory.class);

	private SecurityContextAccessor securityContextAccessor = new DefaultSecurityContextAccessor();

	public ScopeMappingOAuth2RequestFactory(ClientDetailsService clientDetailsService) {
		super(clientDetailsService);
		//super.setCheckUserScopes(true);
	}

	@Override
	public void setCheckUserScopes(boolean checkUserScopes) {
		super.setCheckUserScopes(checkUserScopes);
	}

	@Override
	public TokenRequest createTokenRequest(Map<String, String> requestParameters, ClientDetails authenticatedClient) {
		TokenRequest tokenRequest = super.createTokenRequest(requestParameters, authenticatedClient);
		if (requestParameters.get("grant_type").equals("refresh_token")) {
			LOG.debug("createTokenRequest {}", requestParameters.get("grant_type"));
		}
		return new CustomTokenRequest(requestParameters, tokenRequest.getClientId(),
				tokenRequest.getScope(),
				tokenRequest.getGrantType());
	}

	@Override
	public TokenRequest createTokenRequest(AuthorizationRequest authorizationRequest, String grantType) {
		LOG.debug("createTokenRequest {}", grantType);
		return new CustomTokenRequest(authorizationRequest.getRequestParameters(),
				authorizationRequest.getClientId(),
				authorizationRequest.getScope(),
				grantType);
	}

	/**
	 * @param securityContextAccessor the security context accessor to set
	 */
	@Override
	public void setSecurityContextAccessor(SecurityContextAccessor securityContextAccessor) {
		this.securityContextAccessor = securityContextAccessor;
		super.setSecurityContextAccessor(securityContextAccessor);
	}

	@Override
	public AuthorizationRequest createAuthorizationRequest(Map<String, String> authorizationParameters) {
		AuthorizationRequest request = super.createAuthorizationRequest(authorizationParameters);
		LOG.debug("getAuthorities {}", securityContextAccessor.getAuthorities());
		if (securityContextAccessor.isUser()) {
			request.setAuthorities(securityContextAccessor.getAuthorities());
		}

		return request;
	}

}
