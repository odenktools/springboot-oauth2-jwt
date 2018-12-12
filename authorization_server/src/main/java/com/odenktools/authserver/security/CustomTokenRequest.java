package com.odenktools.authserver.security;

import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.TokenRequest;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class CustomTokenRequest extends TokenRequest {

	private String grantType;

	public CustomTokenRequest(Map<String, String> requestParameters, String clientId, Collection<String> scope,
							  String grantType) {
		super(requestParameters, clientId, scope, grantType);

		this.grantType = grantType;
	}

	/**
	 * Extract the additionalInformation from authenticated client.
	 *
	 * @param client ClientDetails
	 * @return Map with additionalInformation from authenticated client
	 */
	private Map<String, Serializable> extensions(ClientDetails client) {
		Map<String, Serializable> extensions = new HashMap<>();

		Set<Map.Entry<String, Object>> additionalInformations = client.getAdditionalInformation().entrySet();

		for (Map.Entry additionalInformation : additionalInformations) {
			extensions.put((String) additionalInformation.getKey(), (String) additionalInformation.getValue());
		}

		return extensions;
	}

	public OAuth2Request createOAuth2Request(ClientDetails client) {
		Map<String, String> requestParameters = getRequestParameters();
		HashMap<String, String> modifiable = new HashMap<>(requestParameters);
		// Remove password if present to prevent leaks
		modifiable.remove("password");
		modifiable.remove("client_secret");
		// Add grant type so it can be retrieved from OAuth2Request
		modifiable.put("grant_type", grantType);

		return new OAuth2Request(modifiable, client.getClientId(), client.getAuthorities(), true, this.getScope(),
				client.getResourceIds(), null, null, extensions(client));
	}

}
