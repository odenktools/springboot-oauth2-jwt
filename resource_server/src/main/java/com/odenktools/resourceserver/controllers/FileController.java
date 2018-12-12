package com.odenktools.resourceserver.controllers;

import com.odenktools.resourceserver.model.FileResponse;
import com.odenktools.resourceserver.model.IdSeq;
import com.odenktools.resourceserver.model.ImageFile;
import com.odenktools.resourceserver.repository.IdSeqRepository;
import com.odenktools.resourceserver.repository.ImageFileRepository;
import com.odenktools.resourceserver.service.StorageService;
import com.odenktools.resourceserver.util.FileUtil;
import lombok.experimental.FieldDefaults;
import org.apache.commons.lang3.StringUtils;
import org.hashids.Hashids;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.URLConnection;

import static lombok.AccessLevel.PRIVATE;

@RestController
@RequestMapping(path = "/api/v1")
@FieldDefaults(level = PRIVATE, makeFinal = true)
@EnableAsync
public class FileController {

	private static final Logger log = LoggerFactory.getLogger(FileController.class);

	/**
	 * (1) DependencyInjection Fields.
	 */
	private final StorageService service;

	private final ImageFileRepository imageFileRepository;

	private final IdSeqRepository idSeqRepository;

	/**
	 * (2) DependencyInjection Fields.
	 *
	 * @param service service injection.
	 */
	@Autowired
	public FileController(StorageService service, ImageFileRepository imageFileRepository, IdSeqRepository idSeqRepository) {
		this.service = service;
		this.imageFileRepository = imageFileRepository;
		this.idSeqRepository = idSeqRepository;
	}

	@PostMapping("/file/upload")
	public FileResponse handleFileUpload(@RequestParam("file") MultipartFile file) {
		String url;
		String originName = file.getOriginalFilename();
		String suffix = FileUtil.getSuffix(originName);
		String contentType = "";
		String fileDownloadUri = "";
		long fileSize;
		try (InputStream is = new BufferedInputStream(file.getInputStream())) {
			Long id = idSeqRepository.save(new IdSeq()).getId();
			log.debug("DATA SEQUENCE = [{}]", id);
			Hashids hashids = new Hashids();
			String name = hashids.encode(id) + ".".concat(suffix);
			contentType = file.getContentType();
			if (StringUtils.isBlank(contentType)) {
				contentType = URLConnection.guessContentTypeFromName(originName);
			}
			if (StringUtils.isBlank(contentType)) {
				contentType = "image/png";
			}
			fileSize = file.getSize();
			url = this.service.upload(is, name, contentType, fileSize);
			ImageFile imageFileDto = new ImageFile();
			imageFileDto.setId(id);
			imageFileDto.setOriginName(originName);
			imageFileDto.setShortName(url);
			imageFileDto.setSize(file.getSize());
			this.imageFileRepository.save(imageFileDto);
			fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
					.path("images/")
					.path(url)
					.toUriString();
		} catch (Exception e) {
			log.error(e.getMessage());
			return new FileResponse("error", "Error:" + e.getMessage());
		}
		if (url != null) {
			return new FileResponse("success", "images/" + url, originName, fileDownloadUri,
					contentType, fileSize);
		} else {
			return new FileResponse("error", "Failed to upload");
		}
	}
}
