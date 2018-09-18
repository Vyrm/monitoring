package com.serhii.monitor.repository;

import com.serhii.monitor.dao.ResourceResponse;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResourceRepository extends MongoRepository<ResourceResponse, String> {
    List<ResourceResponse> findByUrlAndUser(String url, String user);

    List<ResourceResponse> findByUser(String user);

    List<ResourceResponse> deleteAllByUrlAndUser(String url, String user);
}
