package com.odenktools.authserver;

import com.odenktools.authserver.security.CustomPasswordEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Bootstap Apps.
 *
 * @author Odenktools.
 */
@SpringBootApplication
public class AuthserverApplication implements CommandLineRunner {

	private static final Logger LOG = LoggerFactory.getLogger(AuthserverApplication.class);

	public static void main(String[] args) {

		SpringApplication.run(AuthserverApplication.class, args);

		LOG.info("PASSWORD FOR server-server = {}", new CustomPasswordEncoder().encode("server-server"));
		LOG.info("PASSWORD FOR android-client = {}", new CustomPasswordEncoder().encode("android-client"));
		LOG.info("PASSWORD FOR external-server = {}", new CustomPasswordEncoder().encode("external-server"));

		LOG.info("PASSWORD FOR customer_one = {}", new CustomPasswordEncoder().encode("customer_one"));
		LOG.info("PASSWORD FOR customer_two = {}", new CustomPasswordEncoder().encode("customer_two"));
		LOG.info("PASSWORD FOR customer_bad = {}", new CustomPasswordEncoder().encode("customer_bad"));
	}

	@Override
	public void run(String... args) throws Exception {

	}
}
