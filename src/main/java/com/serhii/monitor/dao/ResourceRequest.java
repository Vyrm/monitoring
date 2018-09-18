package com.serhii.monitor.dao;

import lombok.Data;
import org.hibernate.validator.constraints.URL;

@Data
public class ResourceRequest {

    private String url;
    private int monitoringPeriod; // ms
    private int expectedResponseCode;
    private int expectedMinResponseSize; //symbols
    private int expectedMaxResponseSize; //symbols
    private String expectedSubstring;
    private int responseTimeOk;
    private int responseTimeWarning;
    private int responseTimeCritical;
    private String user;
}
