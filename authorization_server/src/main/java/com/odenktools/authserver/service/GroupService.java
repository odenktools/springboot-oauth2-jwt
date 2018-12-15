package com.odenktools.authserver.service;

import com.odenktools.authserver.dto.group.GroupDto;

/**
 * @author Odenktools.
 */
public interface GroupService {

	void createGroup(GroupDto request);

	Boolean existById(Long id);

	Boolean updateGroup(GroupDto request);

	Boolean existsByNamed(String named);

	Boolean existsByCoded(String coded);
}
