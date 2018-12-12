package com.odenktools.resourceserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Bootstap Apps.
 *
 * @author Odenktools.
 */
@SpringBootApplication
public class ResourceserverApplication {

	private static final Logger LOG = LoggerFactory.getLogger(ResourceserverApplication.class);

	public static void main(String[] args) {

		SpringApplication.run(ResourceserverApplication.class, args);
	}
}
