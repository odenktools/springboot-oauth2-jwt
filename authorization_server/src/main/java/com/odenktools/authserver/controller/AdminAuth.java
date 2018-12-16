package com.odenktools.authserver.controller;

import com.google.gson.JsonObject;
import com.odenktools.authserver.dto.group.GroupDto;
import com.odenktools.authserver.entity.Group;
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
import java.util.Optional;

/**
 * Oauth2 Admin Api Management.
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

	/**
	 * Check Authorize.
	 *
	 * @param principal Pricipal Person.
	 * @return JsonObject.
	 */
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

	//=========================== #START GROUP# ==============================//

	/**
	 * Get Group detail.
	 *
	 * @param id id do you want to check.
	 * @return GroupDto
	 */
	@GetMapping(value = "/group/{id}",
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> findGroupById(@PathVariable Long id) {

		Optional<Group> groupOptional = this.groupService.findById(id);

		JsonObject jsonObject = new JsonObject();

		if (!groupOptional.isPresent()) {
			jsonObject.addProperty("code", HttpStatus.BAD_REQUEST.value());
			jsonObject.addProperty("messages",
					String.format("Group with id ``%s`` not exist", id));
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(jsonObject.toString());
		}

		Group group = groupOptional.get();

		jsonObject.addProperty("code", HttpStatus.OK.value());

		JsonObject jsonData = new JsonObject();
		jsonData.addProperty("named", group.getNamed());
		jsonData.addProperty("coded", group.getCoded());
		jsonData.addProperty("namedDescription", group.getNamedDescription());
		jsonData.addProperty("isActive", group.getIsActive());
		jsonData.addProperty("createdAt", group.getCreatedAt().toInstant().toString());
		jsonData.addProperty("updatedAt", group.getUpdatedAt() != null ?
				group.getUpdatedAt().toInstant().toString() : null);

		//Add to sub-object
		jsonObject.add("data", jsonData);

		return new ResponseEntity<>(jsonObject.toString(), HttpStatus.OK);
	}

	/**
	 * Create a new Group.
	 *
	 * @param request GroupDto.
	 * @return GroupDto.
	 */
	@PostMapping(
			value = "/group/create",
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

	/**
	 * Update existing group.
	 *
	 * @param request GroupDto want to update.
	 * @return GroupDto.
	 */
	@PutMapping(
			value = "/group/update",
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

	/**
	 * Delete existing group.
	 *
	 * @param request Group do you want to delete.
	 * @return JsonObject.
	 */
	@PutMapping(
			value = "/group/delete",
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE
	)
	public ResponseEntity<?> removeGroup(@RequestBody @Valid GroupDto request) {

		JsonObject jsonObject = new JsonObject();

		if (!groupService.existById(request.getId())) {
			jsonObject.addProperty("code", HttpStatus.BAD_REQUEST.value());
			jsonObject.addProperty("messages", String.format("Group with code ``%s`` not exist",
					request.getCoded()));
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(jsonObject.toString());
		}

		boolean removed = this.groupService.removeGroup(request);

		if (removed) {
			jsonObject.addProperty("code", HttpStatus.OK.value());
			jsonObject.addProperty("messages", "Group was successfuly removed.");
			return new ResponseEntity<>(jsonObject.toString(), HttpStatus.OK);
		}

		jsonObject.addProperty("code", HttpStatus.BAD_REQUEST.value());
		jsonObject.addProperty("messages", "Group was unsuccessfuly removed.");
		return new ResponseEntity<>(jsonObject.toString(), HttpStatus.BAD_REQUEST);
	}
	//=========================== #END GROUP# ==============================//
}
