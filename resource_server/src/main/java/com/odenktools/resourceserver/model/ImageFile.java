package com.odenktools.resourceserver.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;

@JsonSerialize
@Entity
@Table(name = "image_files",
		uniqueConstraints = {@UniqueConstraint(columnNames = {"origin_name"})})
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class ImageFile implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "id", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull(message = "originName cannot null")
	@NotEmpty(message = "originName cannot empty")
	@Basic
	@Column(name = "origin_name", unique = true, nullable = false, length = 100)
	private String originName;

	@NotBlank
	@NotNull(message = "shortName cannot null")
	@NotEmpty(message = "shortName cannot empty")
	@Size(min = 1, max = 255)
	@Column(name = "short_name", nullable = false)
	private String shortName;

	@Size(min = 1, max = 255)
	@Column(name = "file_status")
	private String fileStatus;

	@Column(name = "file_size", nullable = false)
	private double size;

	@Basic
	@Column(name = "is_active", nullable = false)
	private Boolean isActive = true;

	@CreationTimestamp
	@Column(name = "created_at", nullable = false, updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdAt;
}