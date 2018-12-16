package com.odenktools.authserver.dto.customer;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

/**
 * Handling Permission for END USER (Customer)
 * @author Odenktools
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@SuppressWarnings("unused")
public class CustomerDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private String username;

	private String email;

	private String phoneNumber;

	private String password;

	private Date lastLogin;

	private int isActive;

	private int isVerified;
}

