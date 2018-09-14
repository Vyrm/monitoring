package com.serhii.monitor.service;

import com.serhii.monitor.dao.Resource;
import com.serhii.monitor.dao.Status;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Service
public class ResourceStatusService {
    public Resource checkStatus(Resource resource) {
        List<Map<Status, String>> list = new ArrayList<>();
        boolean critical = false;
        boolean warning = false;

        //response code
        if (resource.getExpectedResponseCode() != resource.getResponseCode()) {
            Map<Status, String> map = new EnumMap<>(Status.class);
            map.put(Status.CRITICAL, "Invalid response code, expected: "
                    + resource.getExpectedResponseCode() +
                    " received code: "
                    + resource.getResponseCode());
            list.add(map);
            critical = true;
        }

        //response time
        if (resource.getResponseTime() >= resource.getResponseTimeWarning() &&
                resource.getResponseTime() < resource.getResponseTimeCritical()) {
            Map<Status, String> map = new EnumMap<>(Status.class);
            map.put(Status.WARNING, "Resource response time above warning line, response time: "
                    + resource.getResponseTime() + " ms");
            list.add(map);
            warning = true;
        }
        if (resource.getResponseTime() > resource.getResponseTimeCritical()) {
            Map<Status, String> map = new EnumMap<>(Status.class);
            map.put(Status.CRITICAL, "Resource response time above critical line, response time: "
                    + resource.getResponseTime() + " byte");
            list.add(map);
            critical = true;
        }

        //response size
        if (resource.getResponseSize() < resource.getExpectedMinResponseSize() &&
                resource.getResponseSize() > resource.getExpectedMaxResponseSize()) {
            Map<Status, String> map = new EnumMap<>(Status.class);
            map.put(Status.CRITICAL, "Resource response time above critical line, response time: "
                    + resource.getResponseSize() + " byte");
            list.add(map);
            critical = true;
        }

        //response substring
        if (resource.getExpectedSubstringAvailability() != null
                && !resource.getExpectedSubstringAvailability()
                && resource.getExpectedSubstring() != null) {
            Map<Status, String> map = new EnumMap<>(Status.class);
            map.put(Status.CRITICAL, "Resource response does not contains expected substring: "
                    + resource.getExpectedSubstring());
            list.add(map);
            critical = true;
        }

        if (list.isEmpty()) {
            resource.setStatus(Status.OK);
            return resource;
        } else {
            if (warning) {
                resource.setStatus(Status.WARNING);
            }
            if (critical) {
                resource.setStatus(Status.CRITICAL);
            }
            resource.setStatusDescription(list);
        }
        return resource;
    }
}
