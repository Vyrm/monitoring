package com.serhii.monitor.service;

import com.serhii.monitor.controller.MonitorExecutor;
import com.serhii.monitor.dao.ResourceRequest;
import com.serhii.monitor.dao.ResourceResponse;
import com.serhii.monitor.repository.ResourceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RepositoryService {
    private final ResourceRepository resourceRepository;
    private final MonitorExecutor monitorExecutor;

    @Autowired
    public RepositoryService(ResourceRepository resourceRepository,
                             MonitorExecutor monitorExecutor) {
        this.resourceRepository = resourceRepository;
        this.monitorExecutor = monitorExecutor;
    }

    public List<ResourceResponse> findByUrlAndUser(String url, String user) {
        return resourceRepository.findByUrlAndUser(url, user);
    }

    public List<ResourceResponse> findByUser(String user) {
        return resourceRepository.findByUser(user);
    }

    public List<ResourceResponse> findAll() {
        return resourceRepository.findAll();
    }

    public List<ResourceResponse> removeResource(ResourceRequest resourceRequest) {
        monitorExecutor.removeMonitor(resourceRequest.getUrl());
        return resourceRepository.deleteAllByUrlAndUser(resourceRequest.getUrl(), resourceRequest.getUser());
    }

    public void addResource(ResourceRequest resourceRequest) {
        monitorExecutor.executeMonitor(resourceRequest);
    }

    public ResourceResponse addResource(ResourceResponse resourceResponse) {
        return resourceRepository.insert(resourceResponse);
    }
}
