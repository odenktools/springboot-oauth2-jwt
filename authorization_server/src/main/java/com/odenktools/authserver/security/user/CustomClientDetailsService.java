package com.odenktools.authserver.security.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.SQLException;

@Service
public class CustomClientDetailsService extends JdbcClientDetailsService {

	private static final Logger LOG = LoggerFactory.getLogger(CustomClientDetailsService.class);

	public CustomClientDetailsService(DataSource dataSource) {
		super(dataSource);
		try {
			LOG.debug("DATASOURCE {}", dataSource.getConnection().getCatalog());
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/*@Override
	public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {

		LOG.debug("ID {}", clientId);

		List<String> authorizedGrantTypes = new ArrayList<String>();
		authorizedGrantTypes.add("password");
		authorizedGrantTypes.add("refresh_token");
		authorizedGrantTypes.add("client_credentials");
		authorizedGrantTypes.add("authorization_code");
		authorizedGrantTypes.add("implicit");

		BaseClientDetails clientDetails = new BaseClientDetails();
		clientDetails.setClientId(this.id);
		clientDetails.setClientSecret(this.secretKey);
		clientDetails.setAuthorizedGrantTypes(authorizedGrantTypes);
		clientDetails.setResourceIds(Arrays.asList("resource_id"));

		return clientDetails;
	}*/
}
