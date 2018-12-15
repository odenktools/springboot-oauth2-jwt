package com.odenktools.authserver.security.user;

import com.odenktools.authserver.entity.Customer;
import com.odenktools.authserver.repository.ICustomer;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Class for handling Customer. authorized_grant_types != client_credentials
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

	private static final org.slf4j.Logger Logger = LoggerFactory.getLogger(CustomUserDetailsService.class);

	private final ICustomer iCustomer;

	@Autowired
	public CustomUserDetailsService(ICustomer iCustomer) {

		this.iCustomer = iCustomer;
	}

	@Transactional(readOnly = true)
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		Logger.debug("loadUserByUsername {}", username);

		Optional<Customer> customer = this.iCustomer.findByUsername(username);
		if (!customer.isPresent()) {
			throw new UsernameNotFoundException("Invalid username or password.");
		}
		return new PrincipalAdminDetail(customer.get());
	}

	@Transactional(readOnly = true)
	public List<Customer> findAll() {

		List<Customer> list = new ArrayList<>();
		this.iCustomer.findAll().iterator().forEachRemaining(list::add);
		return list;
	}

	@Transactional
	public void delete(Customer id) {

		this.iCustomer.delete(id);
	}

	@Transactional(readOnly = true)
	public Optional<Customer> findOne(long id) {

		return this.iCustomer.findById(id);
	}

	@Transactional
	public Customer save(Customer user) {

		return this.iCustomer.save(user);
	}
}
