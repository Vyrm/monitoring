package com.serhii.monitor.dao;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Map;

@Data
@Document(collection = "resource")
public class ResourceResponse {
    @Id
    private String id;
    private String url;
    private String user;
    private int responseCode;
    private int responseSize;
    private long responseTime;
    private Boolean expectedSubstringAvailability;
    private Status status;
    private List<Map<Status, String>> statusDescription;
}
