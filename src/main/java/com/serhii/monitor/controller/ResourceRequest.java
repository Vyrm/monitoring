package com.serhii.monitor.controller;

import com.serhii.monitor.dao.Resource;
import com.serhii.monitor.repository.ResourceRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController("/resource")
public class ResourceRequest {
    private final MonitorExecutor monitorExecutor;
    private final ResourceRepository resourceRepository;

    @Autowired
    public ResourceRequest(MonitorExecutor monitorExecutor, ResourceRepository resourceRepository) {
        this.monitorExecutor = monitorExecutor;
        this.resourceRepository = resourceRepository;
    }

    @PostMapping
    public void setResource(@RequestBody Resource resource) {
        log.info("URL: " + resource.getUrl());
        monitorExecutor.executeMonitor(resource);
    }

    @DeleteMapping
    public void removeResource(@RequestBody Resource resource) {
        log.info("URL for remove: " + resource.getUrl());
        monitorExecutor.removeMonitor(resource.getUrl());
    }

    @GetMapping(value = "/resource/get")
    public List getResource(){
        return resourceRepository.findAll();
    }
}
