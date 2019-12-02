package com.agh.restaurant.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "firebase")
public class ConfigProperties {
    private String hostName;
    private int port;
    private String from;
}