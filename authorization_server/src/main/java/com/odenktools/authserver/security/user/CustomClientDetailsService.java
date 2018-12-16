package com.odenktools.authserver.security.user;

import com.odenktools.authserver.entity.OauthClientDetails;
import com.odenktools.authserver.repository.IOauthClientDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Odenktools.
 */
@Service
public class CustomClientDetailsService extends JdbcClientDetailsService {

	private static final Logger Logger = LoggerFactory.getLogger(CustomClientDetailsService.class);

	@Autowired
	private IOauthClientDetails iOauthClientDetails;

	public CustomClientDetailsService(DataSource dataSource) {
		super(dataSource);
	}

	@Override
	public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {

		Optional<OauthClientDetails> oauthClientDetails = iOauthClientDetails.findByClientId(clientId);

		if (!oauthClientDetails.isPresent()) {
			throw new ClientRegistrationException("invalid_client");
		}

		OauthClientDetails client = oauthClientDetails.get();

		String resourceIds = client.getResourceIds().stream().collect(Collectors.joining(","));
		String scopes = client.getScope().stream().collect(Collectors.joining(","));
		String grantTypes = client.getAuthorizedGrantTypes().stream().collect(Collectors.joining(","));

		Logger.debug("RESOURCE_ID {}, SCOPE {}, GRANT_TYPES {}", resourceIds, scopes, grantTypes);

		return new BaseClientDetails(client);
	}

	/*private PasswordEncoder passwordEncoder() {
		return new CustomPasswordEncoder();
	}*/

	/*@Override
	public void addClientDetails(ClientDetails clientDetails) throws ClientAlreadyExistsException {
		try {
			OauthClientDetails oauthClientDetail = new OauthClientDetails();
			setOrUpdateClientDetails(oauthClientDetail, clientDetails);
			oauthClientDetail.setClientId(clientDetails.getClientId());
			oauthClientDetail.setClientSecret(clientDetails.getClientSecret() != null ? passwordEncoder().encode(clientDetails.getClientSecret())
					: null);
			iOauthClientDetails.createApplication(oauthClientDetail);
		} catch (DuplicateKeyException e) {
			throw new ClientAlreadyExistsException("Client already exists: " + clientDetails.getClientId(), e);
		}
	}*/

	/*@Override
	public void updateClientSecret(String clientId, String secret) throws NoSuchClientException {
		Optional<OauthClientDetails> oauthOptional = iOauthClientDetails.findByClientId(clientId);
		if (!oauthOptional.isPresent()) {
			throw new NoSuchClientException("No client found with client_id = " + clientId);
		}
		OauthClientDetails oauthClientDetails = oauthOptional.get();
		oauthClientDetails.setClientSecret(secret != null ? this.passwordEncoder().encode(secret) : null);
		iOauthClientDetails.updateApplication(oauthClientDetails);
	}*/

	/*@Override
	public void removeClientDetails(String clientId) throws NoSuchClientException {
		Optional<OauthClientDetails> oauthOptional = iOauthClientDetails.findByClientId(clientId);
		if (!oauthOptional.isPresent()) {
			throw new NoSuchClientException("No client found with client_id = " + clientId);
		}
		OauthClientDetails oauthClientDetails = oauthOptional.get();
		iOauthClientDetails.deleteApplication(oauthClientDetails);
	}*/

	/*private void setOrUpdateClientDetails(OauthClientDetails oauthClientDetail, ClientDetails clientDetails) {

		oauthClientDetail.setResourceIds(clientDetails.getResourceIds());
		oauthClientDetail.setScope(clientDetails.getScope());
		oauthClientDetail.setAuthorizedGrantTypes(clientDetails.getAuthorizedGrantTypes());
		oauthClientDetail.setRegisteredRedirectUri(clientDetails.getRegisteredRedirectUri());
		oauthClientDetail.setAuthorities(clientDetails.getAuthorities());
		oauthClientDetail.setAccessTokenValiditySeconds(clientDetails.getAccessTokenValiditySeconds());
		oauthClientDetail.setRefreshTokenValiditySeconds(clientDetails.getRefreshTokenValiditySeconds());
		//oauthClientDetail.setAdditionalInformation();
	}

	private String processFieldsAuthorities(Collection<GrantedAuthority> authorities) {
		if (authorities == null) {
			return null;
		}
		return org.springframework.util.StringUtils.collectionToCommaDelimitedString(authorities);
	}

	private String processFieldsResourceIds(Set<?> resourceIds) {
		if (resourceIds == null) {
			return null;
		}
		return org.springframework.util.StringUtils.collectionToCommaDelimitedString(resourceIds);
	}*/
}
