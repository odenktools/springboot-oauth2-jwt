package com.odenktools.customer;

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
public class CustomerApplication {

	private static final Logger LOG = LoggerFactory.getLogger(CustomerApplication.class);

	public static void main(String[] args) {

		SpringApplication.run(CustomerApplication.class, args);
	}
}
