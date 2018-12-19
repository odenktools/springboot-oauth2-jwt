package com.odenktools.authserver.service;

import com.odenktools.authserver.dto.group.GroupDto;
import com.odenktools.authserver.entity.Group;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Optional;

/**
 * Group CRUD Implementation.
 *
 * @author Odenktools.
 */
public interface GroupService {

	void createGroup(GroupDto request);

	Optional<Group> findById(Long id);

	Page<Group> findUserByNamedOrCoded(String named, String coded, Sort sort, Pageable pageable);

	Boolean existById(Long id);

	Boolean updateGroup(GroupDto request);

	Boolean removeGroup(Long id);

	Boolean existsByNamed(String named);

	Boolean existsByCoded(String coded);
}
