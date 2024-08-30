package org.aurora.base.aop;

import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;

@Log4j2
@Aspect
@Component
public class AopServiceLog {

    @Pointcut("execution(* org.aurora.base.service..*.*(..))" +
            " && !execution(* org.aurora.base.service..*.silent*(..))" +
            " && !execution(* org.aurora.base.service.sys.SysDictService.findByCode(..))" +
            " && !execution(* org.aurora.base.service.sys.SysUserService.getAuthorizationInfo(..))" +
            " && !execution(* org.aurora.base.service..*.getComboTree(..))")
    private void serviceAspect() {
    }

    @Before("serviceAspect()")
    public void before(JoinPoint joinPoint) {
        log.info("请求方法:{}.{}()", joinPoint.getTarget().getClass().getName(), joinPoint.getSignature().getName());
    }

    @Around("serviceAspect()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        Instant startTime = Instant.now();
        Object result = joinPoint.proceed();
        Duration duration = Duration.between(startTime, Instant.now());
        log.info("方法结束:{}.{}() Time taken: {} ms", joinPoint.getTarget().getClass().getName(), joinPoint.getSignature().getName(), duration.toMillis());
        return result;
    }
}
