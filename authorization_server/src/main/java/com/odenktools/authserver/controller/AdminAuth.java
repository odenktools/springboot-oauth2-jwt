package com.odenktools.authserver.controller;

import com.google.gson.JsonObject;
import com.odenktools.authserver.dto.group.GroupDto;
import com.odenktools.authserver.entity.Group;
import com.odenktools.authserver.entity.OauthClientDetails;
import com.odenktools.authserver.security.user.CustomClientDetailsService;
import com.odenktools.authserver.service.GroupService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

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

    private final CustomClientDetailsService customClientDetailsService;

    @Autowired
    public AdminAuth(GroupService groupService, CustomClientDetailsService customClientDetailsService) {
        this.groupService = groupService;
        this.customClientDetailsService = customClientDetailsService;
    }

    //=========================== #START GROUP# ==============================//

    /**
     * Get all available groups on database.
     *
     * @param name      search by name.
     * @param coded     search by coded.
     * @param page      pagenumber. Default 0.
     * @param size      display limit, Default 10.
     * @param direction sorting "ASC OR DESC", default to DESC.
     * @return Groups Model.
     */
    @RequestMapping(value = "/group/list",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findAllGroups(@RequestParam(name = "name", defaultValue = "") String name,
                                           @RequestParam(name = "coded", defaultValue = "") String coded,
                                           @RequestParam(required = false, defaultValue = "0") Integer page,
                                           @RequestParam(required = false, defaultValue = "10") Integer size,
                                           @RequestParam(required = false, defaultValue = "DESC") String direction) {

        List<String> sortProperties = new ArrayList<>();
        sortProperties.add("createdAt");
        Sort sort = new Sort(Sort.Direction.fromString(direction), sortProperties);

        Page<Group> listData = this.groupService
                .findUserByNamedOrCoded(name, coded,
                        sort, PageRequest.of(page, size));

        return new ResponseEntity<>(listData, HttpStatus.FOUND);
    }

    /**
     * Get Group detail.
     *
     * @param id id do you want to check.
     * @return GroupDto
     */
    @GetMapping(value = "/group",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findGroupById(@RequestParam("id") Long id) {

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
        jsonObject.addProperty("messages", String.format("Group with name ``%s`` was successfuly added",
                request.getNamed()));
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
     * @param id Group "id" do you want to delete.
     * @return JsonObject.
     */
    @DeleteMapping(
            value = "/group/delete/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> removeGroup(@PathVariable("id") Long id) {

        JsonObject jsonObject = new JsonObject();

        if (!groupService.existById(id)) {
            jsonObject.addProperty("code", HttpStatus.BAD_REQUEST.value());
            jsonObject.addProperty("messages", String.format("Group with code ``%s`` not exist",
                    id));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(jsonObject.toString());
        }

        boolean removed = this.groupService.removeGroup(id);

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

    private void addCredentials() {

        OauthClientDetails clientDetails = new OauthClientDetails();
        clientDetails.setClientId("testing");
        Set<String> resourceIds = new HashSet<>();
        resourceIds.add("resource_id");
        clientDetails.setResourceIds(resourceIds);
        clientDetails.setClientSecret("odenktools");
        Set<String> scopes = new HashSet<>();
        scopes.add("read");
        scopes.add("write");
        clientDetails.setScope(scopes);
        Set<String> grantType = new HashSet<>();
        grantType.add("password");
        grantType.add("refresh_token");
        grantType.add("implicit");
        grantType.add("authorization_code");
        clientDetails.setAuthorizedGrantTypes(grantType);
        clientDetails.setAccessTokenValiditySeconds(500);
        clientDetails.setRefreshTokenValiditySeconds(500);

        Set<String> autoApprove = new HashSet<>();
        autoApprove.add("true");
        clientDetails.setAutoApproveScope(autoApprove);

        customClientDetailsService.addClientDetails(clientDetails);
    }
}
