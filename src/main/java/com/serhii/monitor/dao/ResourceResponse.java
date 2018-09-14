package com.serhii.monitor.dao;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class ResourceResponse {
    private String url;
    private String user;
    private int responseCode;
    private int responseSize;
    private long responseTime;
    private Boolean expectedSubstringAvailability;
    private Status status;
    private List<Map<Status, String>> statusDescription;
}