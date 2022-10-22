package com.bigdata.medicalplanner.controller;

import com.bigdata.medicalplanner.service.RedisService;
import com.bigdata.medicalplanner.util.JsonValidator;
import org.everit.json.schema.Schema;
import org.everit.json.schema.ValidationException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
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
    public ResponseEntity<Object> getValue(@PathVariable String key, @RequestHeader HttpHeaders headers) {
        if (!redisService.doesKeyExist(key)) {
            return new ResponseEntity<Object>("{\"message\": \"No Plan Found with the requested key\" }", HttpStatus.NOT_FOUND);
        }

        String eTag = headers.getFirst("If-None-Match");
        String planEtag = redisService.getETagValue(key);
        if (eTag != null && eTag.equals(planEtag)) {
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED).eTag(planEtag).build();
        }

        JSONObject value = redisService.getValue(key);
        return ResponseEntity.ok().eTag(planEtag).body(value.toString());
    }


    @PostMapping(path = "/plan", produces = "application/json")
    public ResponseEntity<Object> createPlan(@RequestBody(required = false) String medicalPlan, @RequestHeader HttpHeaders headers) throws Exception {
        if (medicalPlan == null || medicalPlan.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new JSONObject().put("Error", "Empty Payload. Kindly provide the JSON Payload for the Plan").toString());
        }

        JSONObject json = new JSONObject(medicalPlan);
        try {
            validator.validateJson(json, jsonSchema);
        } catch (ValidationException ex){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new JSONObject().put("Error", ex.getErrorMessage()).toString());
        }

        String key = json.get("objectType").toString() + "_" + json.get("objectId").toString();

        if (redisService.doesKeyExist(key)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(" {\"message\": \"A Plan already exists with the id: " + key + "\" }");
        }

        String computedETag = redisService.postValue(key, json);

        return ResponseEntity.ok().eTag(computedETag).body(" {\"message\": \"Created a Plan with key: " + key + "\" }");
    }

    @DeleteMapping(path = "/plan/{key}", produces = "application/json")
    public ResponseEntity<Object> deletePlan(@PathVariable String key) {
        if (!redisService.doesKeyExist(key)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(" {\"message\": \"A Plan does not exist with the requested key: " + key + "\" }");
        }

        redisService.deleteValue(key);
        return ResponseEntity.noContent().build();
    }
}


