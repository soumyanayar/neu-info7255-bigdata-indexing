package com.bigdata.medicalplanner.controller;

import com.bigdata.medicalplanner.service.RedisService;
import com.bigdata.medicalplanner.util.JsonValidator;
import com.fasterxml.jackson.databind.JsonNode;
import netscape.javascript.JSObject;
import org.everit.json.schema.Schema;
import org.everit.json.schema.ValidationException;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class MedicalPlanController {
    private final RedisService redisService;
    private final JsonValidator validator;
    private final Schema jsonSchema;

    @Autowired
    public MedicalPlanController(RedisService redisService, JsonValidator validator, Schema jsonSchema) {
        this.redisService = redisService;
        this.validator = validator;
        this.jsonSchema = jsonSchema;
    }

    @GetMapping(value = "/plan/{key}", produces = "application/json")
    public ResponseEntity<Object> getValue(@PathVariable String key) {
        JSONObject value = redisService.getValue(key);

        if (value == null) {
            return new ResponseEntity<Object>("{\"message\": \"No Data Found\" }", HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.ok().body(value);
    }


    @PostMapping(path = "/plan", produces = "application/json")
    public ResponseEntity<Object> createPlan(@RequestBody(required = false) String medicalPlan, @RequestHeader HttpHeaders headers) throws JSONException, Exception {
        if (medicalPlan == null || medicalPlan.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new JSONObject().put("Error", "Body is Empty. Kindly provide the JSON").toString());
        }

        JSONObject json = new JSONObject(medicalPlan);
        try {
            validator.validateJson(json, jsonSchema);
        } catch (ValidationException ex){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new JSONObject().put("Error",ex.getErrorMessage()).toString());
        }

        String key = json.get("objectType").toString() + "_" + json.get("objectId").toString();

        if (redisService.doesKeyExist(key)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(" {\"message\": \"A resource already exists with the id: " + key + "\" }");
        }

        String computedETag = redisService.postValue(key, json);

        return ResponseEntity.ok().eTag(computedETag).body(" {\"message\": \"Created data with key: " + key + "\" }");
    }

    @DeleteMapping(path = "/plan/{key}", produces = "application/json")
    public ResponseEntity<Object> deletePlan(@PathVariable String key) {
        if (!redisService.doesKeyExist(key)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(" {\"message\": \"A resource does not exists with the id: " + key + "\" }");
        }

        redisService.deleteValue(key);
        return ResponseEntity.noContent().build();
    }
}
