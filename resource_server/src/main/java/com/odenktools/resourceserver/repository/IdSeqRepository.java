package com.odenktools.resourceserver.repository;

import com.odenktools.resourceserver.model.IdSeq;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IdSeqRepository extends JpaRepository<IdSeq, Long> {

	IdSeq save(IdSeq entity);
}
