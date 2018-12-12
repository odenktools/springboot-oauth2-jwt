package com.odenktools.resourceserver.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Data
public class IdSeq {

	@GeneratedValue(strategy = GenerationType.AUTO)
	@Id
	private Long id;
}
