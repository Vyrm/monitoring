package com.serhii.monitor.controller;

import com.serhii.monitor.dao.Resource;
import com.serhii.monitor.dao.Status;
import com.serhii.monitor.repository.ResourceRepository;
import com.serhii.monitor.service.ResourceAnalyzingService;
import com.serhii.monitor.service.ResourceStatusService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE;

@Component
@Slf4j
@Scope(value = SCOPE_PROTOTYPE)
public class ResourceMonitor implements Runnable {
    private final ResourceAnalyzingService resourceAnalyzingService;
    private final ResourceStatusService resourceStatusService;
    private final ResourceRepository resourceRepository;
    private Resource resource;

    @Autowired
    public ResourceMonitor(ResourceAnalyzingService resourceAnalyzingService,
                           ResourceStatusService resourceStatusService,
                           ResourceRepository resourceRepository) {
        this.resourceAnalyzingService = resourceAnalyzingService;
        this.resourceStatusService = resourceStatusService;
        this.resourceRepository = resourceRepository;
    }

    @Override
    public void run() {
        Thread.currentThread().setName(resource.getUrl());
        while (!Thread.currentThread().isInterrupted()) {
            try {
                resource = resourceAnalyzingService.analyze(resource);
            } catch (URISyntaxException e) {
                log.error("Invalid URL");
                Thread.currentThread().interrupt();
                break;
            } catch (RestClientException e) {
                log.error("Timeout");
                resource.setStatus(Status.CRITICAL);
                Thread.currentThread().interrupt();
                break;
            }
            resource = resourceStatusService.checkStatus(resource);
            System.out.println(resource);
            resourceRepository.insert(resource);
            try {
                Thread.sleep(resource.getMonitoringPeriod());
            } catch (InterruptedException e) {
                log.info("Stopping thread: {}", Thread.currentThread().getName());
                Thread.currentThread().interrupt();
            }
        }
    }

    public void setResource(Resource resource) {
        this.resource = resource;
    }
}
