package org.aurora.base.aop;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aurora.base.entity.sys.SysErrorLog;
import org.aurora.base.entity.sys.SysOperationLog;
import org.aurora.base.service.sys.SysErrorLogService;
import org.aurora.base.service.sys.SysOperationLogService;
import org.aurora.base.util.constant.CommonConstant;
import org.aurora.base.util.web.IPUtils;
import org.aurora.base.util.web.JSONUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

@Log4j2
@Aspect
@Component
public class AopControllerLog {
    @Autowired
    public AopControllerLog(SysOperationLogService logService, SysErrorLogService errorLogService) {
        this.logService = logService;
        this.errorLogService = errorLogService;
    }

    private final SysOperationLogService logService;
    private final SysErrorLogService errorLogService;

    @Pointcut("execution(* org.aurora.base.controller..*.*(..))")
    private void controllerAspect() {
    }

    @Around("controllerAspect()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        log.info("URL:{}", request.getRequestURL());
        log.info("IP:{}", IPUtils.getIpAddr(request));
        log.info("请求方式:{}", request.getMethod());
        log.info("请求方法:{}.{}()", joinPoint.getTarget().getClass().getName(), joinPoint.getSignature().getName());
        log.info("参数列表:{}", getParams(request));
        Instant startTime = Instant.now();
        newLog(joinPoint, request);
        Object result = joinPoint.proceed();
        Duration duration = Duration.between(startTime, Instant.now());
        log.info("方法结束:{}.{}() Time taken: {} ms", joinPoint.getTarget().getClass().getName(), joinPoint.getSignature().getName(), duration.toMillis());
        return result;
    }

    @AfterThrowing(pointcut = "controllerAspect()", throwing = "e")
    public void afterThrowing(JoinPoint joinPoint, Throwable e) {
        log.error("异常名称:{}", e.getClass().getName());
        log.error("堆栈跟踪:{}", e.getMessage(), e);
        log.error("异常方法:{}.{}()", joinPoint.getTarget().getClass().getName(), joinPoint.getSignature().getName());
        newError(joinPoint, e);
    }

    private void newLog(JoinPoint joinPoint, HttpServletRequest request) {
        SysOperationLog log = new SysOperationLog();
        log.setOpController(joinPoint.getTarget().getClass().getName() + "." + joinPoint.getSignature().getName() + "()");
        log.setRequestUrl(String.valueOf(request.getRequestURL()));
        log.setRequestMethod(request.getMethod());
        log.setRequestIp(IPUtils.getIpAddr(request));
        log.setRequestParameters(getParams(request));
        logService.newOperationLog(log);
    }

    private void newError(JoinPoint joinPoint, Throwable e) {
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        SysErrorLog err = new SysErrorLog();
        err.setErrorName(e.getClass().getName());
        err.setErrorStackTrace(getStackTrace(e));
        err.setErrorController(joinPoint.getTarget().getClass().getName() + "." + joinPoint.getSignature().getName() + "()");
        err.setRequestUrl(String.valueOf(request.getRequestURL()));
        err.setRequestMethod(request.getMethod());
        err.setRequestIp(IPUtils.getIpAddr(request));
        err.setRequestParameters(getParams(request));
        errorLogService.newErrorLog(err);
    }

    private String getStackTrace(Throwable e) {
        String stackTrace;
        StringWriter sw = new StringWriter();
        try (PrintWriter pw = new PrintWriter(sw, true)) {
            e.printStackTrace(pw);
            stackTrace = sw.toString();
        }
        return stackTrace;
    }

    private String getParams(HttpServletRequest request) {
        Map<String, String[]> map = request.getParameterMap();
        Set.of(CommonConstant.NO_LOG_REQUEST_PARAMS).forEach(param -> {
            if (map.containsKey(param)) {
                map.put(param, null);
            }
        });
        return JSONUtils.writeValueAsString(map);
    }
}
