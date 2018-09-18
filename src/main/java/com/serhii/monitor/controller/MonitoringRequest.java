package com.serhii.monitor.controller;

import com.serhii.monitor.dao.ResourceRequest;
import com.serhii.monitor.service.RepositoryService;
import com.serhii.monitor.service.URLValidation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@RestController("/resource")
public class MonitoringRequest {

    private final RepositoryService repositoryService;
    private final URLValidation urlValidation;

    @Autowired
    public MonitoringRequest(RepositoryService repositoryService, URLValidation urlValidation) {
        this.repositoryService = repositoryService;
        this.urlValidation = urlValidation;
    }

    @PostMapping
    public String setResource(@RequestBody ResourceRequest resourceRequest) {
        ConcurrentHashMap<String, String> concurrentHashMap = urlValidation.validate(resourceRequest);
        if (concurrentHashMap.keySet().contains(resourceRequest.getUrl())) {
            return concurrentHashMap.get(resourceRequest.getUrl());
        } else {
            repositoryService.addResource(resourceRequest);
            return "Ok";
        }
    }

    @DeleteMapping
    public void removeResource(@RequestBody ResourceRequest resourceRequest) {
        repositoryService.removeResource(resourceRequest);
    }

    @GetMapping
    public List getAllResource() {
        return repositoryService.findAll();
    }

    @GetMapping(value = "/resource/{user}")
    public List getResourceByUser(@PathVariable String user) {
        return repositoryService.findByUser(user);
    }

    @GetMapping(value = "/resource/{user}/{url}")
    public List getResourceByUser(@PathVariable String user, @PathVariable String url) {
        return repositoryService.findByUrlAndUser(url, user);
    }

/*    @DeleteMapping(value = "/resource/remove")
    public List<ResourceResponse> removeResourceByUrlAndUser(@RequestBody ResourceRequest resourceRequest) {
        log.info(resourceRequest.toString());
        return repositoryService.removeResource(resourceRequest.getUrl(), resourceRequest.getUser());
    }*/
}
