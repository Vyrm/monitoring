package com.serhii.monitor.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class AspectLogging {
    @Before("execution(* com.serhii.monitor.controller.MonitoringRequest.*(..))")
    public void helloAdvice(JoinPoint joinPoint) {
        log.info("Method calling: {}", joinPoint.getSignature().getName());
        Object[] arguments = joinPoint.getArgs();
        for (Object argument : arguments) {
            int count = 1;
            log.info("Argument {}: {}", count, argument.toString());
        }
    }
}
