package com.odenktools.resourceserver.service;

import com.odenktools.resourceserver.repository.ImageFileRepository;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractStorageService implements StorageService {

	@Autowired
	protected ImageFileRepository imageFileRepository;
}
