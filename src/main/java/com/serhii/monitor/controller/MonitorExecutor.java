package com.serhii.monitor.controller;

import com.serhii.monitor.dao.ResourceRequest;
import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.concurrent.ExecutorService;

@Component
public class MonitorExecutor {
    private final ExecutorService executorService;
    private ResourceMonitor resourceMonitor;

    public MonitorExecutor(ExecutorService executorService) {
        this.executorService = executorService;
    }

    public void executeMonitor(ResourceRequest resourceRequest) {
        resourceMonitor = getResourceMonitor();
        resourceMonitor.setResourceRequest(resourceRequest);
        executorService.submit(resourceMonitor);
    }

    public void removeMonitor(String url) {
        Set<Thread> setOfThread = Thread.getAllStackTraces().keySet();
        for (Thread thread : setOfThread) {
            if (thread.getName().equals(url)) {
                thread.interrupt();
            }
        }
    }

    @Lookup
    public ResourceMonitor getResourceMonitor() {
        return null;
    }
}
