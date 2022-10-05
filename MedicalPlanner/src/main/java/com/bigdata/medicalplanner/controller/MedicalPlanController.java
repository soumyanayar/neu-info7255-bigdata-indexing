package com.bigdata.medicalplanner.controller;

import com.bigdata.medicalplanner.service.RedisService;
import com.bigdata.medicalplanner.util.JsonValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MedicalPlanController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private RedisService redisService;

    @Autowired
    JsonValidator validator;
}
