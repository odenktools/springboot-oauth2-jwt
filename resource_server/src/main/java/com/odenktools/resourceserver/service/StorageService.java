package com.odenktools.resourceserver.service;

import java.io.IOException;
import java.io.InputStream;

public interface StorageService {

	String upload(InputStream is, String filename, String contentType, long length) throws IOException;
}
