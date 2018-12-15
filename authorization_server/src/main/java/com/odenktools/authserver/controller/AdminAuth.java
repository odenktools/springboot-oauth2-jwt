package com.odenktools.authserver.controller;

import com.google.gson.JsonObject;
import com.odenktools.authserver.dto.group.GroupDto;
import com.odenktools.authserver.service.GroupService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

/**
 * Oauth2 Admin Management.
 *
 * @author Odenktools.
 */
@RestController
@RequestMapping("/api/v1/admin")
public class AdminAuth {

	private static final Logger LOG = LoggerFactory.getLogger(AdminAuth.class);

	private final GroupService groupService;

	@Autowired
	public AdminAuth(GroupService groupService) {
		this.groupService = groupService;
	}

	@GetMapping(value = "/me",
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> user(Principal principal) {
		System.out.println(principal);

		LOG.debug("PRICIPAL {}", principal.getName());

		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("code", HttpStatus.OK.value());
		jsonObject.addProperty("messages",
				String.format("Welcome again ``%s``. And Happy nice day!", principal.getName()));
		return new ResponseEntity<>(jsonObject.toString(), HttpStatus.OK);
	}

	@PostMapping(
			value = "/create",
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE
	)
	public ResponseEntity<?> createGroup(@RequestBody @Valid GroupDto request) {

		JsonObject jsonObject = new JsonObject();

		if (this.groupService.existsByNamed(request.getNamed())) {
			jsonObject.addProperty("code", HttpStatus.CONFLICT.value());
			jsonObject.addProperty("messages", String.format("Group with name ``%s`` already exist",
					request.getNamed()));
			return ResponseEntity.status(HttpStatus.CONFLICT).body(jsonObject.toString());
		}

		if (this.groupService.existsByCoded(request.getCoded())) {
			jsonObject.addProperty("code", HttpStatus.CONFLICT.value());
			jsonObject.addProperty("messages", String.format("Group with code ``%s`` already exist",
					request.getCoded()));
			return ResponseEntity.status(HttpStatus.CONFLICT).body(jsonObject.toString());
		}

		this.groupService.createGroup(request);
		jsonObject.addProperty("code", HttpStatus.OK.value());
		jsonObject.addProperty("messages", "Ok!");
		return new ResponseEntity<>(jsonObject.toString(), HttpStatus.OK);
	}

	@PutMapping(
			value = "/update",
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE
	)
	public ResponseEntity<?> updateGroup(@RequestBody @Valid GroupDto request) {

		JsonObject jsonObject = new JsonObject();

		if (!groupService.existById(request.getId())) {
			jsonObject.addProperty("code", HttpStatus.BAD_REQUEST.value());
			jsonObject.addProperty("messages", String.format("Group with code ``%s`` not exist",
					request.getCoded()));
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(jsonObject.toString());
		}

		boolean updated = this.groupService.updateGroup(request);

		if (updated) {
			jsonObject.addProperty("code", HttpStatus.OK.value());
			jsonObject.addProperty("messages", "Group was successfuly updated.");
			return new ResponseEntity<>(jsonObject.toString(), HttpStatus.OK);
		}

		jsonObject.addProperty("code", HttpStatus.BAD_REQUEST.value());
		jsonObject.addProperty("messages", "Group was unsuccessfuly updated.");
		return new ResponseEntity<>(jsonObject.toString(), HttpStatus.BAD_REQUEST);
	}
}
