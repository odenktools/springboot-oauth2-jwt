package com.odenktools.resourceserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * Application Configuration.
 *
 * @author Odenktools.
 */
@Configuration
@Component
public class AppConfig {

	@Value("${security.oauth2.resource.tokenInfoUri}")
	public String tokenInfoUri;

	@Value("${security.oauth2.client.client-id}")
	public String clientId;

	@Value("${security.oauth2.client.client-secret}")
	public String clientSecret;
}
