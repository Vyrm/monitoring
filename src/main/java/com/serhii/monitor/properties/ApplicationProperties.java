package com.serhii.monitor.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties("application")
@PropertySource(value = "classpath:application.yml")
@EnableConfigurationProperties
public class ApplicationProperties {
    private Timeout timeout;

    @Data
    public static class Timeout{
        int read;
        int connection;
    }
}
