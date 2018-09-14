package com.serhii.monitor.service;

import com.serhii.monitor.dao.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;

@Service
@Slf4j
public class ResourceAnalyzingService {
    private final RestTemplate restTemplate;

    @Autowired
    public ResourceAnalyzingService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Resource analyze(Resource resource) throws URISyntaxException {
        long start = System.currentTimeMillis();
        ResponseEntity httpEntity = makeRequest(resource);
        long end = System.currentTimeMillis();
        long responseTime = calculateResponseTime(start, end);
        log.info("Response time: {} ms", responseTime);
        int responseCode = getResponseCode(httpEntity);
        log.info("Response code: {}", responseCode);
        int responseSize = getResponseSize(httpEntity);
        log.info("Response size: {} byte", responseSize);
        if (resource.getExpectedSubstring() != null && resource.getExpectedSubstring().isEmpty()) {
            boolean expectedSubstringAvailability = getSubstring(httpEntity, resource.getExpectedSubstring());
            log.info("Substring availability: {}", expectedSubstringAvailability);
            resource.setExpectedSubstringAvailability(expectedSubstringAvailability);
        }

        resource.setResponseSize(responseSize);
        resource.setResponseCode(responseCode);
        resource.setResponseTime(responseTime);

        return resource;
    }

    private ResponseEntity makeRequest(Resource resource) throws URISyntaxException, RestClientException {
        System.setProperty("https.proxyHost", "wsproxy.alfa.bank.int");
        System.setProperty("https.proxyPort", "3128");
        RequestEntity request = new RequestEntity(HttpMethod.GET, new URI(resource.getUrl()));
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
