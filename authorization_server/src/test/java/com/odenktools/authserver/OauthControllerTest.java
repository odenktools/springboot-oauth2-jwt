package com.odenktools.authserver;

import com.odenktools.authserver.security.AuthorizationServerConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsResourceDetails;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;
import java.util.Map;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class OauthControllerTest {

	private static final Logger LOG = LoggerFactory.getLogger(OauthControllerTest.class);

	@LocalServerPort
	int randomServerPort;

	@Test
	public void testGetCustomerMe() {

		ClientCredentialsResourceDetails resourceDetails = new ClientCredentialsResourceDetails();
		resourceDetails.setAccessTokenUri(java.lang.String.format("http://localhost:%d/oauth/token", this.randomServerPort));
		resourceDetails.setClientId("server-server");
		resourceDetails.setClientSecret("server-server");
		resourceDetails.setGrantType("client_credentials");
		resourceDetails.setScope(java.util.Arrays.asList("read", "write", "trust"));

		DefaultOAuth2ClientContext clientContext = new DefaultOAuth2ClientContext();

		OAuth2RestTemplate restTemplate = new OAuth2RestTemplate(resourceDetails, clientContext);
		restTemplate.setMessageConverters(java.util.Arrays.asList(new MappingJackson2HttpMessageConverter()));
		LOG.debug("access token {}", restTemplate.getAccessToken());

		/*final List<Map> users = restTemplate.getForObject(
				java.lang.String.format("http://localhost:%d/api/v1/customer/me", this.randomServerPort), List.class);*/

	}
}
