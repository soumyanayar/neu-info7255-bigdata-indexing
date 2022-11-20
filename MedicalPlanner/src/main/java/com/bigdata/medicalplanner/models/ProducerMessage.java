package com.bigdata.medicalplanner.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProducerMessage implements Serializable {
    String payload;
    String action;
    String key;
}
