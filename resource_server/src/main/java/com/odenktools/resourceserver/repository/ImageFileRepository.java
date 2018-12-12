package com.odenktools.resourceserver.repository;

import com.odenktools.resourceserver.model.ImageFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageFileRepository extends JpaRepository<ImageFile, Long> {

	ImageFile findByShortName(String shortName);
}
