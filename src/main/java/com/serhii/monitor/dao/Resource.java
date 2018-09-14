package com.serhii.monitor.dao;

import lombok.Data;
import org.springframework.data.annotation.Transient;

import java.util.List;
import java.util.Map;

@Data
public class Resource {
    private String url;
    @Transient
    private int monitoringPeriod; // ms
    @Transient
    private int expectedResponseCode;
    @Transient
    private int expectedMinResponseSize; //symbols
    @Transient
    private int expectedMaxResponseSize; //symbols
    @Transient
    private String expectedSubstring;
    @Transient
    private int responseTimeOk;
    @Transient
    private int responseTimeWarning;
    @Transient
    private int responseTimeCritical;
    private String user;

    // after analyzing
    private int responseCode;
    private int responseSize;
    private long responseTime;
    private Boolean expectedSubstringAvailability;
    private Status status;
    private List<Map<Status, String>> statusDescription;
}
