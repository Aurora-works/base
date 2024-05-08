package org.aurora.base.aop;

import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;

@Log4j2
@Aspect
@Component
public class AopDaoLog {

    @Pointcut("execution(* org.aurora.base.dao..*.*(..))" +
            " && !execution(* org.aurora.base.dao..silent*(..))" +
            " && !execution(* org.aurora.base.dao.sys.SysDictDao.findByCode(..))" +
            " && !execution(* org.aurora.base.dao.sys.SysUserRoleDao.findByUserIdWithFetchGraph(..))" +
            " && !execution(* org.aurora.base.dao.sys.SysRoleMenuDao.findByRoleIdWithFetchGraph(..))")
    private void daoAspect() {
    }

    @Before("daoAspect()")
    public void before(JoinPoint joinPoint) {
        log.info("请求方法:{}.{}()", joinPoint.getTarget().getClass().getName(), joinPoint.getSignature().getName());
    }

    @Around("daoAspect()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        Instant startTime = Instant.now();
        Object result = joinPoint.proceed();
        Duration duration = Duration.between(startTime, Instant.now());
        log.info("方法结束:{}.{}() Time taken: {} ms", joinPoint.getTarget().getClass().getName(), joinPoint.getSignature().getName(), duration.toMillis());
        return result;
    }
}
