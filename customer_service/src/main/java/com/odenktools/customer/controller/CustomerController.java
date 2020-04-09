package com.odenktools.customer.controller;

import com.google.gson.JsonObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1")
public class CustomerController {

    @PreAuthorize("hasRole('ROLE_MOBILE')")
    @RequestMapping(value = "/customer/me",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> user(Principal principal) {
        System.out.println(principal);

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("messages",
                String.format("Welcome again ``%s``. And Happy nice day!", principal.getName()));
        return new ResponseEntity<>(jsonObject.toString(), HttpStatus.FOUND);
    }

}