package com.odenktools.authserver.service;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.io.Serializable;
import java.util.List;

/**
 * Custom JSON Response.
 *
 * @param <T> Object Model.
 * @author Odenktools.
 */
public class RestResponsePage<T> extends PageImpl<T> implements Serializable {

	private static final long serialVersionUID = 1L;

	@JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
	public RestResponsePage(@JsonProperty("content") List<T> content,
							@JsonProperty("number") int page,
							@JsonProperty("size") int size,
							@JsonProperty("totalElements") long total) {

		super(content, PageRequest.of(page, size), total);
	}

	public RestResponsePage(List<T> content, Pageable pageable, long total) {

		super(content, pageable, total);
	}

	public RestResponsePage(List<T> content) {

		super(content);
	}
}

