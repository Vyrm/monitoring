package com.serhii.monitor;

import com.serhii.monitor.repository.ResourceRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;


@SpringBootApplication
@Slf4j
@EnableAspectJAutoProxy
@Component
public class MonitorApplication implements CommandLineRunner {
    @Autowired
    public ResourceRepository resourceRepository;

    public static void main(String[] args) {
        SpringApplication.run(MonitorApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
    }
}
