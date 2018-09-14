package com.serhii.monitor.service;

import com.serhii.monitor.dao.ResourceRequest;
import com.serhii.monitor.dao.ResourceResponse;
import com.serhii.monitor.dao.Status;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Service
public class ResourceStatusService {
    public ResourceResponse checkStatus(ResourceRequest resourceRequest, ResourceResponse resourceResponse) {
        List<Map<Status, String>> list = new ArrayList<>();
        boolean critical = false;
        boolean warning = false;

        //response code
        if (resourceRequest.getExpectedResponseCode() != resourceResponse.getResponseCode()) {
            Map<Status, String> map = new EnumMap<>(Status.class);
            map.put(Status.CRITICAL, "Invalid response code, expected: "
                    + resourceRequest.getExpectedResponseCode() +
                    " received code: "
                    + resourceResponse.getResponseCode());
            list.add(map);
            critical = true;
        }

        //response time
        if (resourceResponse.getResponseTime() >= resourceRequest.getResponseTimeWarning() &&
                resourceResponse.getResponseTime() < resourceRequest.getResponseTimeCritical()) {
            Map<Status, String> map = new EnumMap<>(Status.class);
            map.put(Status.WARNING, "ResourceRequest response time above warning line, response time: "
                    + resourceResponse.getResponseTime() + " ms");
            list.add(map);
            warning = true;
        }
        if (resourceResponse.getResponseTime() > resourceRequest.getResponseTimeCritical()) {
            Map<Status, String> map = new EnumMap<>(Status.class);
            map.put(Status.CRITICAL, "ResourceRequest response time above critical line, response time: "
                    + resourceResponse.getResponseTime() + " ms");
            list.add(map);
            critical = true;
        }

        //response size
        if (resourceResponse.getResponseSize() < resourceRequest.getExpectedMinResponseSize() &&
                resourceResponse.getResponseSize() > resourceRequest.getExpectedMaxResponseSize()) {
            Map<Status, String> map = new EnumMap<>(Status.class);
            map.put(Status.CRITICAL, "ResourceRequest response time above critical line, response time: "
                    + resourceResponse.getResponseSize() + " byte");
            list.add(map);
            critical = true;
        }

        //response substring
        if (resourceResponse.getExpectedSubstringAvailability() != null && !resourceResponse.getExpectedSubstringAvailability()) {
            Map<Status, String> map = new EnumMap<>(Status.class);
            map.put(Status.CRITICAL, "ResourceRequest response does not contains expected substring: "
                    + resourceRequest.getExpectedSubstring());
            list.add(map);
            critical = true;
        }

        if (list.isEmpty()) {
            resourceResponse.setStatus(Status.OK);
            return resourceResponse;
        } else {
            if (warning) {
                resourceResponse.setStatus(Status.WARNING);
            }
            if (critical) {
                resourceResponse.setStatus(Status.CRITICAL);
            }
            resourceResponse.setStatusDescription(list);
        }
        return resourceResponse;
    }
}
