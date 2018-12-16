package com.odenktools.authserver.repository;

import com.odenktools.authserver.entity.OauthClientDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author Odenktools.
 */
@Repository
public interface IOauthClientDetails extends JpaRepository<OauthClientDetails, Long> {

	Optional<OauthClientDetails> findByClientId(String clientId);
	//void createApplication(OauthClientDetails client);
	//void updateApplication(OauthClientDetails client);
	//void deleteApplication(OauthClientDetails client);
}
