package com.odenktools.authserver.service.impl;

import com.mysema.query.jpa.impl.JPAQuery;
import com.odenktools.authserver.dto.group.GroupDto;
import com.odenktools.authserver.entity.Group;
import com.odenktools.authserver.entity.QGroup;
import com.odenktools.authserver.filter.QueryFilter;
import com.odenktools.authserver.repository.IGroup;
import com.odenktools.authserver.service.GroupService;
import com.odenktools.authserver.service.RestResponsePage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Optional;

/**
 * Group CRUD Implementation.
 *
 * @author Odenktools.
 */
@Service
public class GroupServiceImpl extends QueryFilter<Group> implements GroupService {

	//private static final Logger LOG = LoggerFactory.getLogger(GroupServiceImpl.class);

	private final IGroup iGroup;

	public GroupServiceImpl(IGroup iGroup) {
		this.iGroup = iGroup;
	}

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	@Transactional(readOnly = true)
	public Optional<Group> findById(Long id) {
		Optional<Group> groupOptional = this.iGroup.findById(id);
		return groupOptional;
	}

	@Override
	@Transactional(readOnly = true)
	public Page<Group> findUserByNamedOrCoded(String named, String coded, Sort sort, Pageable pageable) {

		QGroup qGroup = QGroup.group;
		JPAQuery query = new JPAQuery(this.entityManager).from(qGroup);
		if (named != null && !named.isEmpty()) {
			query.where(qGroup.named.containsIgnoreCase(named));
		}
		if (coded != null && !coded.isEmpty()) {
			query.where(qGroup.coded.containsIgnoreCase(coded));
		}
		long total = query.count();
		query.offset(pageable.getOffset());
		query.limit(pageable.getPageSize());
		for (Sort.Order order : sort) {
			query.orderBy(this.toOrderSpecifier(Group.class, qGroup.getMetadata(), order));
		}
		return new RestResponsePage<>(query.list(qGroup), pageable, total);
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
	@Transactional
	public Boolean removeGroup(Long id) {

		Optional<Group> groupOptional = this.iGroup.findById(id);
		if (groupOptional.isPresent()) {
			Group group = groupOptional.get();
			//delete from database
			this.iGroup.deleteById(group.getId());
			return true;
		}
		return false;
	}

	@Override
	@Transactional(readOnly = true)
	public Boolean existsByNamed(String named) {

		Boolean isExistsInGroup = iGroup.existsByNamed(named);
		return isExistsInGroup;
	}

	@Override
	@Transactional(readOnly = true)
	public Boolean existsByCoded(String coded) {
		Boolean isExistsInGroup = iGroup.existsByCoded(coded);
		return isExistsInGroup;
	}
}
