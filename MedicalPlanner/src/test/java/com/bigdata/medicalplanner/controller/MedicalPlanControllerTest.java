package com.bigdata.medicalplanner.controller;

import com.bigdata.medicalplanner.configuration.JsonSchemaConfiguration;
import com.bigdata.medicalplanner.service.RedisService;
import com.bigdata.medicalplanner.util.JsonValidator;
import org.everit.json.schema.Schema;
import org.everit.json.schema.ValidationException;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
@RunWith(SpringRunner.class)
@WebMvcTest(MedicalPlanController.class)
class MedicalPlanControllerTest {
    @Autowired
    MockMvc mockMvc;
    @MockBean
    private RedisService redisService;
    @MockBean
    private JsonValidator validator;
    @MockBean
    private Schema jsonSchema;

    @MockBean
    private JsonSchemaConfiguration jsonSchemaConfiguration;

    @BeforeEach
    void setUp()  {
        this.mockMvc = MockMvcBuilders.standaloneSetup(new MedicalPlanController(redisService, validator, jsonSchema)).build();
    }

    String medicalPlan = "{\"planCostShares\":{\"deductible\":5000,\"_org\":\"example.com\",\"copay\":23,\"objectId\":\"1234vxc2324sdf-550\",\"objectType\":\"membercostshare\"}," +
            "\"linkedPlanServices\":[{\"linkedService\":{\"_org\":\"example.com\",\"objectId\":\"1234520xvc30asdf-502\",\"objectType\":\"service\",\"name\":\"Yearlyphysical\"},\"planserviceCostShares\":{\"deductible\":20,\"_org\":\"example.com\",\"copay\":0,\"objectId\":\"1234512xvc1314asdfs-503\",\"objectType\":\"membercostshare\"},\"_org\":\"example.com\",\"objectId\":\"27283xvx9asdff-504\"," +
            "\"objectType\":\"planservice\"},{\"linkedService\":{\"_org\":\"example.com\",\"objectId\":\"1234520xvc30sfs-505\",\"objectType\":\"service\",\"name\":\"wellbaby\"},\"planserviceCostShares\":{\"deductible\":10,\"_org\":\"example.com\",\"copay\":175,\"objectId\":\"1234512xvc1314sdfsd-506\",\"objectType\":\"membercostshare\"},\"_org\":\"example.com\",\"objectId\":\"27283xvx9sdf-507\"," +
            "\"objectType\":\"planservice\"}],\"_org\":\"example.com\",\"objectId\":\"12xvxc345ssdsds-508\",\"objectType\":\"plan\",\"planType\":\"inNetwork\",\"creationDate\":\"12-12-2017\"}";

    @Test
    void createMedicalPlan_success() throws Exception {
        MedicalPlanController medicalPlanController = new MedicalPlanController(redisService, validator, jsonSchema);
        JSONObject json = new JSONObject(medicalPlan);
        validator.validateJson(json, jsonSchema);
        String key  = json.get("objectType").toString() + "_" + json.get("objectId").toString();
        Mockito.when(redisService.doesKeyExist(key)).thenReturn(false);
        Mockito.when(redisService.postValue(Mockito.eq(key), Mockito.any(JSONObject.class))).thenReturn("eTag");
        Assertions.assertEquals(200, medicalPlanController.createPlan(medicalPlan, null).getStatusCodeValue());
    }

    @Test
    void createMedicalPlan_conflict() throws Exception {
        MedicalPlanController medicalPlanController = new MedicalPlanController(redisService, validator, jsonSchema);
        JSONObject json = new JSONObject(medicalPlan);
        //validator.validateJson(json, jsonSchema);
        String key  = json.get("objectType").toString() + "_" + json.get("objectId").toString();
        Mockito.when(redisService.doesKeyExist(key)).thenReturn(true);
        Assertions.assertEquals(409, medicalPlanController.createPlan(medicalPlan, null).getStatusCodeValue());
    }

    @Test
    void createMedicalPlan_badRequest() throws Exception {
        MedicalPlanController medicalPlanController = new MedicalPlanController(redisService, validator, jsonSchema);
        JSONObject json = new JSONObject(medicalPlan);
        Mockito.doThrow(new ValidationException("error")).when(validator).validateJson(Mockito.any(JSONObject.class), Mockito.any(Schema.class));
        Assertions.assertEquals(400, medicalPlanController.createPlan(medicalPlan, null).getStatusCodeValue());
    }

    @Test
    void createMedicalPlan_success_usingMvc() throws Exception {
        JSONObject json = new JSONObject(medicalPlan);
        String key  = json.get("objectType").toString() + "_" + json.get("objectId").toString();
        Mockito.when(redisService.postValue(Mockito.eq(key), Mockito.any(JSONObject.class))).thenReturn("eTag");

        this.mockMvc.perform(
                        MockMvcRequestBuilders.post("/plan")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(medicalPlan))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void createMedicalPlan_conflict_usingMvc() throws Exception {
        JSONObject json = new JSONObject(medicalPlan);
        String key  = json.get("objectType").toString() + "_" + json.get("objectId").toString();
        Mockito.when(redisService.doesKeyExist(key)).thenReturn(true);

        this.mockMvc.perform(
                        MockMvcRequestBuilders.post("/plan")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(medicalPlan))
                .andExpect(MockMvcResultMatchers.status().isConflict());
    }

    @Test
    void createMedicalPlan_badRequest_usingMvc() throws Exception {
        JSONObject json = new JSONObject(medicalPlan);
        String key  = json.get("objectType").toString() + "_" + json.get("objectId").toString();
        Mockito.doThrow(new ValidationException("error")).when(validator).validateJson(Mockito.any(JSONObject.class), Mockito.any(Schema.class));

        this.mockMvc.perform(
                        MockMvcRequestBuilders.post("/plan")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(medicalPlan))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void getMedicalPlan_notFound_usingMvc() throws Exception {
        JSONObject json = new JSONObject(medicalPlan);
        String key  = json.get("objectType").toString() + "_" + json.get("objectId").toString();
        Mockito.when(redisService.doesKeyExist(key)).thenReturn(false);

        this.mockMvc.perform(
                        MockMvcRequestBuilders.get("/plan/plan_12xvxc345ssdsds-508")
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void getMedicalPlan_notModified_usingMvc() throws Exception {
        JSONObject json = new JSONObject(medicalPlan);
        String key  = json.get("objectType").toString() + "_" + json.get("objectId").toString();
        Mockito.when(redisService.doesKeyExist(key)).thenReturn(true);
        String eTag = "eTag";
        Mockito.when(redisService.getETagValue(key)).thenReturn(eTag);
        this.mockMvc.perform(
                        MockMvcRequestBuilders.get("/plan/plan_12xvxc345ssdsds-508")
                                .header("If-None-Match", eTag))
                .andExpect(MockMvcResultMatchers.status().isNotModified());

    }

    @Test
    void getMedicalPlan_success_usingMvc() throws Exception {
        JSONObject json = new JSONObject(medicalPlan);
        String key  = json.get("objectType").toString() + "_" + json.get("objectId").toString();
        Mockito.when(redisService.doesKeyExist(key)).thenReturn(true);
        String eTag = "eTag";
        Mockito.when(redisService.getETagValue(key)).thenReturn(eTag);
        Mockito.when(redisService.getValue(key)).thenReturn(json);
        //verify if the response is a valid json
        this.mockMvc.perform(
                        MockMvcRequestBuilders.get("/plan/plan_12xvxc345ssdsds-508")
                                .header("If-None-Match", "someETag"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.objectType").value("plan"))
                .andExpect(MockMvcResultMatchers.status().isOk());

    }

    @Test
    void deleteMedicalPlan_success_usingMvc() throws Exception {
        JSONObject json = new JSONObject(medicalPlan);
        String key  = json.get("objectType").toString() + "_" + json.get("objectId").toString();
        Mockito.when(redisService.doesKeyExist(key)).thenReturn(true);
        String eTag = "eTag";
        Mockito.when(redisService.getETagValue(key)).thenReturn(eTag);
        Mockito.when(redisService.deleteValue(key)).thenReturn(true);
        this.mockMvc.perform(
                        MockMvcRequestBuilders.delete("/plan/plan_12xvxc345ssdsds-508")
                                .header("If-Match", eTag))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    void deleteMedicalPlan_notFound_usingMvc() throws Exception {
        JSONObject json = new JSONObject(medicalPlan);
        String key  = json.get("objectType").toString() + "_" + json.get("objectId").toString();
        Mockito.when(redisService.doesKeyExist(key)).thenReturn(false);
        this.mockMvc.perform(
                        MockMvcRequestBuilders.delete("/plan/plan_12xvxc345ssdsds-508")
                                .header("If-Match", "someETag"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }
}