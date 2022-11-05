package com.bigdata.medicalplanner.controller;

import com.bigdata.medicalplanner.exceptions.*;
import com.bigdata.medicalplanner.service.RedisService;
import com.bigdata.medicalplanner.util.JsonValidator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.everit.json.schema.Schema;
import org.everit.json.schema.ValidationException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.github.fge.jsonpatch.JsonPatchException;
import com.github.fge.jsonpatch.mergepatch.JsonMergePatch;

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
        } catch (ValidationException | IOException ex) {
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
        } catch (ValidationException | IOException ex) {
            throw new JsonValidationFailureException("Schema validation failed, provide a valid json");
        }

        if (!redisService.doesKeyExist(key)) {
            throw new ValueNotFoundExceptions(String.format("Medical plan with key %s not found", key));
        }

        String eTag = headers.getFirst("If-Match");
        String planEtag = redisService.getETagValue(key);

        // if header contains If-Match, check if it matches the etag of the key
        if (eTag != null && !planEtag.equals(eTag)) {
            throw new InvalidEtagException(String.format("The ETag provided does not match the ETag of the Plan with the requested key: %s", key));
        }

        String computedETag = redisService.postValue(key, json);
        return ResponseEntity.ok().eTag(computedETag).body(" {\"message\": \"Successfully updated the Plan with key: " + key + "\" }");
    }

    @PatchMapping(path = "/plan/{key}", produces = "application/json")
    public ResponseEntity<Object> patchPlan(@PathVariable String key, @RequestBody(required = false) String medicalPlanToPatch, @RequestHeader HttpHeaders headers) throws ValueNotFoundExceptions, InvalidPayloadException, JsonValidationFailureException, InvalidEtagException, JsonProcessingException, JsonPatchException {
        if (medicalPlanToPatch == null || medicalPlanToPatch.isEmpty()) {
            throw new InvalidPayloadException("Payload is empty");
        }

        if (!redisService.doesKeyExist(key)) {
            throw new ValueNotFoundExceptions(String.format("Medical plan with key %s not found", key));
        }

        String eTag = headers.getFirst("If-Match");
        String planEtag = redisService.getETagValue(key);

        // if header contains If-Match, check if it matches the etag of the key
        if (eTag != null && !planEtag.equals(eTag)) {
            throw new InvalidEtagException(String.format("The ETag provided does not match the ETag of the Plan with the requested key: %s", key));
        }

        JSONObject currentMedicalPlan = redisService.getValue(key);

        JsonNode currentMedicalPlanJsonNode = new ObjectMapper().readTree(currentMedicalPlan.toString());
        JsonNode medicalPlanToPatchJsonNode = new ObjectMapper().readTree(medicalPlanToPatch);
        JsonNode mergedMedicalPlan = JsonMergePatch.fromJson(medicalPlanToPatchJsonNode).apply(currentMedicalPlanJsonNode);
        JSONObject mergedMedicalPlanJsonObject = new JSONObject(mergedMedicalPlan.toString());

        try {
            validator.validateJson(mergedMedicalPlanJsonObject, jsonSchema);
        } catch (ValidationException | IOException ex) {
            throw new JsonValidationFailureException("Schema validation failed, provide a valid json");
        }

        String computedETag = redisService.postValue(key, mergedMedicalPlanJsonObject);
        return ResponseEntity.ok().eTag(computedETag).body(" {\"message\": \"Successfully patched the Plan with key: " + key + "\" }");
    }
}
