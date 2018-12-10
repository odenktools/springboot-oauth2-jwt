package com.odenktools.authserver.controllers;

import com.google.gson.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class HelloController {

  private static final Logger LOG = LoggerFactory.getLogger(HelloController.class);

  @RequestMapping(value = "/hello",
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> user() {

    JsonObject jsonObject = new JsonObject();
    jsonObject.addProperty("messages", "HelloController");
    return new ResponseEntity<>(jsonObject.toString(), HttpStatus.FOUND);
  }

}
