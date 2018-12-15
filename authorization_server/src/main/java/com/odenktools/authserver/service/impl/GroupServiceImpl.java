package com.odenktools.authserver.service.impl;

import com.odenktools.authserver.dto.group.GroupDto;
import com.odenktools.authserver.entity.Group;
import com.odenktools.authserver.repository.IGroup;
import com.odenktools.authserver.service.GroupService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class GroupServiceImpl implements GroupService {

	//private static final Logger LOG = LoggerFactory.getLogger(GroupServiceImpl.class);

	private final IGroup iGroup;

	public GroupServiceImpl(IGroup iGroup) {
		this.iGroup = iGroup;
	}

	@Override
	@Transactional
	public void createGroup(GroupDto request) {

		Group group = new Group(request.getNamed(),
				request.getCoded(),
				request.getNamedDescription(),
				request.getIsActive());

		//Save it to database
		this.iGroup.save(group);
	}

	@Override
	@Transactional(readOnly = true)
	public Boolean existById(Long id) {
		Optional<Group> groupOptional = this.iGroup.findById(id);
		return groupOptional.isPresent();
	}

	@Override
	@Transactional
	public Boolean updateGroup(GroupDto request) {

		Optional<Group> groupOptional = this.iGroup.findById(request.getId());
		if (groupOptional.isPresent()) {
			Group group = groupOptional.get();
			group.setNamed(request.getNamed());
			group.setCoded(request.getCoded());
			group.setNamedDescription(request.getNamedDescription());
			group.setIsActive(request.getIsActive());
			//update it to database
			this.iGroup.save(group);
			return true;
		}
		return false;
	}

	@Override
	public Boolean existsByNamed(String named) {

		Boolean isExistsInGroup = iGroup.existsByNamed(named);
		return isExistsInGroup;
	}

	@Override
	public Boolean existsByCoded(String coded) {
		Boolean isExistsInGroup = iGroup.existsByCoded(coded);
		return isExistsInGroup;
	}
}
