package com.odenktools.authserver.service;

import com.odenktools.authserver.dto.group.GroupDto;
import com.odenktools.authserver.entity.Group;

import java.util.Optional;

/**
 * @author Odenktools.
 */
public interface GroupService {

	void createGroup(GroupDto request);

	Optional<Group> findById(Long id);

	Boolean existById(Long id);

	Boolean updateGroup(GroupDto request);

	Boolean removeGroup(GroupDto request);

	Boolean existsByNamed(String named);

	Boolean existsByCoded(String coded);
}
