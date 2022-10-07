package com.bigdata.medicalplanner.service;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Service
public class ETagServiceImpl implements ETagService{
    private final MessageDigest messageDigest;

    @Autowired
    public ETagServiceImpl(MessageDigest messageDigest) {
        this.messageDigest = messageDigest;
    }

    @Override
    public String computeETag(JSONObject inputData) {
        byte[] inputDataInBytes = inputData.toString().getBytes(StandardCharsets.UTF_8);
        byte[] hash = messageDigest.digest(inputDataInBytes);
        String encoded = Base64.getEncoder().encodeToString(hash);
        return "\""+encoded+"\"";
    }

    @Override
    public boolean verifyETag(JSONObject json, String eTag) {
        return false;
    }
}
