package com.odenktools.authserver.repository;

import com.odenktools.authserver.entity.OauthClientDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author Odenktools.
 */
@Repository
public interface IOauthClientDetails extends JpaRepository<OauthClientDetails, String> {

	Optional<OauthClientDetails> findByClientId(String clientId);
}
