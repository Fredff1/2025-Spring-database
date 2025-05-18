package com.repairhub.management.configuration;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@ConfigurationProperties(prefix = "security")
@Data
public class SecurityProperties {
    private List<String> whitelist;
    private List<String> adminAllowedList;
    private List<String> userAllowedList;

}
