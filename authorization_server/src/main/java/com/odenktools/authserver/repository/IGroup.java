package com.odenktools.authserver.repository;

import com.odenktools.authserver.entity.Group;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IGroup extends JpaRepository<Group, Long> {

	Page<Group> findAll(Pageable pageable);

	Optional<Group> findByNamed(String named);

	Optional<Group> findByCoded(String coded);

	Boolean existsByNamed(String named);

	Boolean existsByCoded(String coded);
}
