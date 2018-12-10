package com.odenktools.authserver.repository;

import com.odenktools.authserver.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ICustomer extends JpaRepository<Customer, Long> {

  Optional<Customer> findByUsername(String username);

  Optional<Customer> findByEmail(String email);
}
