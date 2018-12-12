package com.odenktools.resourceserver.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

@JsonSerialize
@Data
public class FileResponse {

	@JsonProperty("file_name")
	private String fileName;

	@JsonProperty("status")
	private String status;

	@JsonProperty("message")
	private String message;

	@JsonProperty("file_download_uri")
	private String fileDownloadUri;

	@JsonProperty("file_type")
	private String fileType;

	@JsonProperty("size")
	private Long size;

	public FileResponse(String status, String message) {
		this.status = status;
		this.message = message;
	}

	public FileResponse(String status, String message, String fileName, String fileDownloadUri, String fileType) {
		this.status = status;
		this.message = message;
		this.fileName = fileName;
		this.fileDownloadUri = fileDownloadUri;
		this.fileType = fileType;
	}

	public FileResponse(String status, String message, String fileName, String fileDownloadUri, String fileType, Long size) {
		this.status = status;
		this.message = message;
		this.fileName = fileName;
		this.fileDownloadUri = fileDownloadUri;
		this.fileType = fileType;
		this.size = size;
	}
}
