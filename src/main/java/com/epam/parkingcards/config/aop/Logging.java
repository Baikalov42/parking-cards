package com.epam.parkingcards.config.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Aspect
@Profile("main")
public class Logging {

    private static final Logger LOGGER = LoggerFactory.getLogger(Logging.class);

    @Pointcut(value = "within (com.epam.parkingcards.service..*)")
    public void servicePointcut() {
    }

    @Pointcut(value = "within(com.epam.parkingcards.web..*) ")
    public void controllerPointcut() {
    }

    @Before(value = "servicePointcut()|| controllerPointcut()")
    public void logBefore(JoinPoint point) {

        String methodName = point.getSignature().getName();
        String className = point.getTarget().getClass().getSimpleName();
        Object[] args = point.getArgs();

        LOGGER.info("BEFORE {}.{}(), args: {}",
                className, methodName, args);
    }

    @AfterReturning(value = "servicePointcut()|| controllerPointcut()",
            returning = "result")
    public void logAfter(JoinPoint point, Object result) {

        String methodName = point.getSignature().getName();
        String className = point.getTarget().getClass().getSimpleName();

        LOGGER.info("AFTER {}.{} returning value: {}",
                className, methodName, result);
    }

    @AfterThrowing(value = "servicePointcut()||  controllerPointcut()", throwing = "ex")
    public void logException(JoinPoint point, Throwable ex) {

        String methodName = point.getSignature().getName();
        String className = point.getTarget().getClass().getSimpleName();

        LOGGER.info("THROWING: {}.{} exception: {}", className, methodName, ex.getMessage());
    }
}
