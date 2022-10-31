package com.bigdata.medicalplanner.controller;

import com.bigdata.medicalplanner.exceptions.*;
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

import java.io.IOException;

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
    public ResponseEntity<Object> getValue(@PathVariable String key, @RequestHeader HttpHeaders headers) throws ValueNotFoundExceptions {
      if (!redisService.doesKeyExist(key)) {
           throw new ValueNotFoundExceptions(String.format("Medical plan with key %s not found", key));
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
    public ResponseEntity<Object> createPlan(@RequestBody(required = false) String medicalPlan, @RequestHeader HttpHeaders headers) throws KeyAlreadyExistsException, InvalidPayloadException, JsonValidationFailureException {
        if (medicalPlan == null || medicalPlan.isEmpty()) {
            throw new InvalidPayloadException("Payload is empty");
        }

        JSONObject json = new JSONObject(medicalPlan);
        try {
            validator.validateJson(json, jsonSchema);
        } catch (ValidationException | IOException ex){
            throw new JsonValidationFailureException("Schema validation failed, provide a valid json");
        }

        String key = json.get("objectType").toString() + "_" + json.get("objectId").toString();

        if (redisService.doesKeyExist(key)) {
            throw new KeyAlreadyExistsException(String.format("Medical plan with key %s already exists", key));
        }

        String computedETag = redisService.postValue(key, json);

        return ResponseEntity.ok().eTag(computedETag).body(" {\"message\": \"Created a Plan with key: " + key + "\" }");
    }

    @DeleteMapping(path = "/plan/{key}", produces = "application/json")
    public ResponseEntity<Object> deletePlan(@PathVariable String key) throws ValueNotFoundExceptions {
        if (!redisService.doesKeyExist(key)) {
            throw new ValueNotFoundExceptions(String.format("Medical plan with key %s not found", key));
        }

        redisService.deleteValue(key);
        return ResponseEntity.noContent().build();
    }

    @PutMapping(path = "/plan/{key}", produces = "application/json")
    public ResponseEntity<Object> updatePlan(@PathVariable String key, @RequestBody(required = false) String medicalPlan, @RequestHeader HttpHeaders headers) throws ValueNotFoundExceptions, InvalidPayloadException, JsonValidationFailureException, InvalidEtagException {
        if (medicalPlan == null || medicalPlan.isEmpty()) {
            throw new InvalidPayloadException("Payload is empty");
        }

        JSONObject json = new JSONObject(medicalPlan);
        try {
            validator.validateJson(json, jsonSchema);
        } catch (ValidationException | IOException ex){
            throw new JsonValidationFailureException("Schema validation failed, provide a valid json");
        }

        String eTag = headers.getFirst("If-Match");

        //if header contains if-match
        if(eTag != null && headers.containsKey("If-Match")){
                // if-match is not equal to etag
            if(!eTag.equals(redisService.getETagValue(key))){
                throw new InvalidEtagException(String.format("The ETag provided does not match the ETag of the Plan with the requested key: %s", key));
            }
            else{
                String computedETag = redisService.postValue(key, json);
                return ResponseEntity.ok().eTag(computedETag).body(" {\"message\": \"Updated a Plan with key: " + key + "\" }");
            }
        }

        if (!redisService.doesKeyExist(key)) {
            throw new ValueNotFoundExceptions(String.format("Medical plan with key %s not found", key));
        }

        String computedETag = redisService.postValue(key, json);

        return ResponseEntity.ok().eTag(computedETag).body(" {\"message\": \"Successfully updated the Plan with key: " + key + "\" }");
    }
}
