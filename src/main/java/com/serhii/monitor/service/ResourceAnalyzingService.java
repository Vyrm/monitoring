package com.serhii.monitor.service;

import com.serhii.monitor.dao.ResourceRequest;
import com.serhii.monitor.dao.ResourceResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;

@Service
@Slf4j
public class ResourceAnalyzingService {
    private final RestTemplate restTemplate;
    private ResourceResponse resourceResponse;

    @Autowired
    public ResourceAnalyzingService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public ResourceResponse analyze(ResourceRequest resourceRequest) throws ResourceAccessException{
        resourceResponse = new ResourceResponse();
        resourceResponse.setUrl(resourceRequest.getUrl());
        resourceResponse.setUser(resourceRequest.getUser());
        long start = System.currentTimeMillis();
        ResponseEntity httpEntity = makeRequest(resourceRequest);
        long end = System.currentTimeMillis();
        long responseTime = calculateResponseTime(start, end);
        log.info("Response time: {} ms", responseTime);
        int responseCode = getResponseCode(httpEntity);
        log.info("Response code: {}", responseCode);
        int responseSize = getResponseSize(httpEntity);
        log.info("Response size: {} byte", responseSize);
        if (resourceRequest.getExpectedSubstring() != null && !resourceRequest.getExpectedSubstring().isEmpty()) {
            boolean expectedSubstringAvailability = getSubstring(httpEntity, resourceRequest.getExpectedSubstring());
            log.info("Substring availability: {}", expectedSubstringAvailability);
            resourceResponse.setExpectedSubstringAvailability(expectedSubstringAvailability);
        }

        resourceResponse.setResponseSize(responseSize);
        resourceResponse.setResponseCode(responseCode);
        resourceResponse.setResponseTime(responseTime);

        return resourceResponse;
    }

    private ResponseEntity makeRequest(ResourceRequest resourceRequest) throws ResourceAccessException {
        System.setProperty("https.proxyHost", "wsproxy.alfa.bank.int");
        System.setProperty("https.proxyPort", "3128");
        RequestEntity request = null;
        try {
            request = new RequestEntity(HttpMethod.GET, new URI(resourceRequest.getUrl()));
        } catch (URISyntaxException e) {
            log.error("Invalid URL");
        }
        ResponseEntity<String> response = restTemplate.exchange(request, String.class);
        return response;
    }

    private long calculateResponseTime(long start, long end) {
        return end - start;
    }

    private int getResponseCode(ResponseEntity responseEntity) {
        return responseEntity.getStatusCodeValue();
    }

    private int getResponseSize(ResponseEntity responseEntity) {
        return responseEntity.getHeaders().toString().getBytes().length;
    }

    private boolean getSubstring(ResponseEntity responseEntity, String expectedSubstring) {
        return responseEntity.getHeaders().toString().contains(expectedSubstring);
    }
}
