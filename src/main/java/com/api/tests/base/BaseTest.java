package com.api.tests.base;

import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.AfterClass;

@Slf4j
public abstract class BaseTest {

    @BeforeClass
    public void setupClass() {
        log.info("Setting up test class: {}", this.getClass().getSimpleName());
    }
    
    @AfterClass
    public void teardownClass() {
        log.info("Cleaning up test class: {}", this.getClass().getSimpleName());
    }

    protected void logTestInfo(String testName, String description) {
        log.info("Starting test: {} - {}", testName, description);
    }
}