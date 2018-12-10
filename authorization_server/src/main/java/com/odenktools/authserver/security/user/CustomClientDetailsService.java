package com.odenktools.authserver.security.user;

import com.odenktools.authserver.security.ResourceServerConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class CustomClientDetailsService extends JdbcClientDetailsService {

  private static final Logger LOG = LoggerFactory.getLogger(CustomClientDetailsService.class);

  private String id;

  private String secretKey;

  public CustomClientDetailsService(DataSource dataSource) {
    super(dataSource);
  }

  public String getId() {

    return this.id;
  }

  public void setId(String id) {

    this.id = id;
  }

  public String getSecretKey() {

    return this.secretKey;
  }

  public void setSecretKey(String secretKey) {

    this.secretKey = secretKey;
  }

  @Override
  public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {

    LOG.debug("ID {}", clientId);

    List<String> authorizedGrantTypes = new ArrayList<String>();
    //authorizedGrantTypes.add("password");
    //authorizedGrantTypes.add("refresh_token");
    //authorizedGrantTypes.add("client_credentials");
    //authorizedGrantTypes.add("authorization_code");
    //authorizedGrantTypes.add("implicit");

    BaseClientDetails clientDetails = new BaseClientDetails();
    clientDetails.setClientId(this.id);
    clientDetails.setClientSecret(this.secretKey);
    clientDetails.setAuthorizedGrantTypes(authorizedGrantTypes);
    clientDetails.setResourceIds(Arrays.asList(ResourceServerConfig.RESOURCE_ID));

    return clientDetails;
  }
}
