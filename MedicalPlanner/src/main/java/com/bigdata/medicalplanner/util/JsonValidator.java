package com.bigdata.medicalplanner.util;

import org.everit.json.schema.Schema;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import java.io.IOException;

@Service
public class JsonValidator {
    public void validateJson(JSONObject object, Schema schema) throws IOException {
        schema.validate(object);
    }
}
