package com.bigdata.medicalplanner.configuration;

import org.everit.json.schema.Schema;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.io.InputStream;

@Configuration
public class JsonSchemaConfiguration {
    @Bean
    Schema jsonSchema() throws IOException {
        try (InputStream inputStream = getClass().getResourceAsStream("/JsonSchema.json")) {
            JSONObject rawSchema = new JSONObject(new JSONTokener(inputStream));
            return SchemaLoader.load(rawSchema);
        }
    }
}
