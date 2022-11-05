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

    //validate partial json
    public void validateJson(JSONObject object, Schema schema, String... fields) throws IOException {
        JSONObject partialObject = new JSONObject();
        for (String field : fields) {
            partialObject.put(field, object.get(field));
        }
        schema.validate(partialObject);
    }
}
