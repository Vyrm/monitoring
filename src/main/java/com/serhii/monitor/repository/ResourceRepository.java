package com.serhii.monitor.repository;

import com.serhii.monitor.dao.Resource;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResourceRepository extends MongoRepository<Resource, String> {
    List<Resource> findByUrl(String url);
    Resource removeResourceByUrlAndUser (String url, String user);
}
