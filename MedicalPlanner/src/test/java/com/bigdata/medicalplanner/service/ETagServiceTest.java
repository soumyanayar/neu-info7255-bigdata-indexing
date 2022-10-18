package com.bigdata.medicalplanner.service;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.security.MessageDigest;
import java.util.Base64;


@ExtendWith(MockitoExtension.class)
class ETagServiceTest {
    @Mock
    private MessageDigest messageDigest;

    String medicalPlan = "{\"planCostShares\":{\"deductible\":5000,\"_org\":\"example.com\",\"copay\":23,\"objectId\":\"1234vxc2324sdf-550\",\"objectType\":\"membercostshare\"}," +
            "\"linkedPlanServices\":[{\"linkedService\":{\"_org\":\"example.com\",\"objectId\":\"1234520xvc30asdf-502\",\"objectType\":\"service\",\"name\":\"Yearlyphysical\"},\"planserviceCostShares\":{\"deductible\":20,\"_org\":\"example.com\",\"copay\":0,\"objectId\":\"1234512xvc1314asdfs-503\",\"objectType\":\"membercostshare\"},\"_org\":\"example.com\",\"objectId\":\"27283xvx9asdff-504\"," +
            "\"objectType\":\"planservice\"},{\"linkedService\":{\"_org\":\"example.com\",\"objectId\":\"1234520xvc30sfs-505\",\"objectType\":\"service\",\"name\":\"wellbaby\"},\"planserviceCostShares\":{\"deductible\":10,\"_org\":\"example.com\",\"copay\":175,\"objectId\":\"1234512xvc1314sdfsd-506\",\"objectType\":\"membercostshare\"},\"_org\":\"example.com\",\"objectId\":\"27283xvx9sdf-507\"," +
            "\"objectType\":\"planservice\"}],\"_org\":\"example.com\",\"objectId\":\"12xvxc345ssdsds-508\",\"objectType\":\"plan\",\"planType\":\"inNetwork\",\"creationDate\":\"12-12-2017\"}";

        @Test
        void computeETag_success() throws JSONException {
            JSONObject medicalPlanJson = new JSONObject(medicalPlan);
            ETagService eTagService = new ETagServiceImpl(messageDigest);
            Mockito.when(messageDigest.digest(medicalPlanJson.toString().getBytes())).thenReturn("1234".getBytes());
            String encoded = Base64.getEncoder().encodeToString("1234".getBytes());
            String result = eTagService.computeETag(medicalPlanJson);
            Assertions.assertEquals( "\""+encoded+"\""  , result);
        }

        @Test
        void verifyETag_success() throws JSONException {
            ETagService eTagService = new ETagServiceImpl(messageDigest);
            JSONObject medicalPlanJson = new JSONObject(medicalPlan);
            String eTag = "1234";
            boolean result = eTagService.verifyETag(medicalPlanJson, eTag);
            Assertions.assertFalse(result);
        }

}