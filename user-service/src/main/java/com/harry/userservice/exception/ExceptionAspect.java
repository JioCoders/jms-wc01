package com.harry.userservice.exception;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.AfterThrowing;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.sql.SQLException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Aspect
@Component
public class ExceptionAspect {

    @AfterThrowing(pointcut = "execution(* com.harry..*.*(..))", throwing = "ex")
    public void handleExceptions(Exception ex) {
        logErrorBasedOnExceptionType(ex);
    }

    private void logErrorBasedOnExceptionType(Exception ex) {
        if (ex instanceof IOException ioEx) {
            log.error("IOException occurred: {}", ioEx.getMessage());
        } else if (ex instanceof SQLException sqlEx) {
            log.error("SQLException occurred: {}", sqlEx.getMessage());
        } else {
            log.error("Exception occurred: {}", ex.getMessage());
        }
    }
}