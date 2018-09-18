package com.serhii.monitor.controller;

import com.serhii.monitor.dao.ResourceRequest;
import com.serhii.monitor.dao.ResourceResponse;
import com.serhii.monitor.service.RepositoryService;
import com.serhii.monitor.service.ResourceAnalyzingService;
import com.serhii.monitor.service.ResourceStatusService;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.internal.constraintvalidators.hv.URLValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;

import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE;

@Component
@Slf4j
@Scope(value = SCOPE_PROTOTYPE)
public class ResourceMonitor implements Runnable {
    private final ResourceAnalyzingService resourceAnalyzingService;
    private final ResourceStatusService resourceStatusService;
    private final RepositoryService repositoryService;
    private ResourceRequest resourceRequest;
    private ResourceResponse resourceResponse;

    @Autowired
    public ResourceMonitor(ResourceAnalyzingService resourceAnalyzingService,
                           ResourceStatusService resourceStatusService,
                           RepositoryService repositoryService) {
        this.resourceAnalyzingService = resourceAnalyzingService;
        this.resourceStatusService = resourceStatusService;
        this.repositoryService = repositoryService;
    }


    public void setResourceRequest(ResourceRequest resourceRequest) {
        this.resourceRequest = resourceRequest;
    }

    @Override
    public void run() {
        Thread.currentThread().setName(resourceRequest.getUrl());
        while (!Thread.currentThread().isInterrupted()) {
            try {
                resourceResponse = resourceAnalyzingService.analyze(resourceRequest);
            } catch (ResourceAccessException e) {
                log.error("Failed to connect to HOST, reason: {}", e.getMessage());
                resourceStatusService.setStatus(resourceResponse, e.getMessage());

                repositoryService.addResource(resourceResponse);
                Thread.currentThread().interrupt();
            }
            resourceResponse = resourceStatusService.checkStatus(resourceRequest, resourceResponse);
            repositoryService.addResource(resourceResponse);
            try {
                Thread.sleep(resourceRequest.getMonitoringPeriod());
            } catch (InterruptedException e) {
                log.info("Stopping thread: {}", Thread.currentThread().getName());
                Thread.currentThread().interrupt();
            }
        }
    }
}
