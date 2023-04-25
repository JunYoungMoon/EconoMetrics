package com.mjy.econometrics;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class ConfigDebugger implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(ConfigDebugger.class);

    @Value("${security.oauth2.client.registration.google.client-id}")
    private String clientId;

    @Value("${security.oauth2.client.registration.google.client-secret}")
    private String clientSecret;

    @Override
    public void run(String... args) {
        logger.info("Client ID: {}", clientId);
        logger.info("Client Secret: {}", clientSecret);
    }
}

