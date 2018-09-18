package com.serhii.monitor.service;

import com.serhii.monitor.dao.ResourceRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Slf4j
public class URLValidation {
    private final RestTemplate restTemplate;
    private ConcurrentHashMap<String, String> concurrentHashMap;

    @Autowired
    public URLValidation(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        concurrentHashMap = new ConcurrentHashMap<>();
    }

    public ConcurrentHashMap<String, String> validate(ResourceRequest resourceRequest) {
        RequestEntity request = null;
        try {
            request = new RequestEntity(HttpMethod.GET, new URI(resourceRequest.getUrl()));
        } catch (URISyntaxException e) {
            log.error("URISyntaxException exception {}", e.getMessage());
        }
        try {
            restTemplate.exchange(request, String.class);
        } catch (RestClientException e) {
            concurrentHashMap.put(resourceRequest.getUrl(), e.getMessage());
        }
        return concurrentHashMap;
    }
}
