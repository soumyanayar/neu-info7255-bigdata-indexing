package com.bigdata.medicalplanner.service;

import org.json.JSONObject;

import java.util.List;

public interface ETagService {
    public String computeETag(JSONObject json);

    public boolean verifyETag(JSONObject json, String eTag);
}
