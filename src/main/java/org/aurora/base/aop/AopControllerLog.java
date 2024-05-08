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
import org.aurora.base.entity.sys.SysRequestLog;
import org.aurora.base.service.sys.SysErrorLogService;
import org.aurora.base.service.sys.SysRequestLogService;
import org.aurora.base.shiro.ShiroUtils;
import org.aurora.base.util.constant.CommonConstant;
import org.aurora.base.util.web.IPUtils;
import org.aurora.base.jackson.JSONUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

@Log4j2
@Aspect
@Component
public class AopControllerLog {
    @Autowired
    public AopControllerLog(SysRequestLogService logService, SysErrorLogService errorLogService) {
        this.logService = logService;
        this.errorLogService = errorLogService;
    }

    private final SysRequestLogService logService;
    private final SysErrorLogService errorLogService;

    @Pointcut("execution(* org.aurora.base.controller..*.*(..))" +
            " && !execution(* org.aurora.base.controller.dev.SysDictController.findByCode(..))")
    private void controllerAspect() {
    }

    @Around("controllerAspect()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        Instant startTime = Instant.now();
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        String requestUrl = String.valueOf(request.getRequestURL());
        String requestIp = IPUtils.getIpAddr(request);
        String requestMethod = request.getMethod();
        String requestController = joinPoint.getTarget().getClass().getName() + "." + joinPoint.getSignature().getName() + "()";
        String requestParameters = getParams(request);
        log.info("URL:{}", requestUrl);
        log.info("IP:{}", requestIp);
        log.info("请求方式:{}", requestMethod);
        log.info("请求方法:{}", requestController);
        log.info("参数列表:{}", requestParameters);
        newRequestLog(requestUrl, requestIp, requestMethod, requestController, requestParameters);
        Object result = joinPoint.proceed();
        Duration duration = Duration.between(startTime, Instant.now());
        log.info("方法结束:{}.{}() Time taken: {} ms", joinPoint.getTarget().getClass().getName(), joinPoint.getSignature().getName(), duration.toMillis());
        return result;
    }

    @AfterThrowing(pointcut = "controllerAspect()", throwing = "e")
    public void afterThrowing(JoinPoint joinPoint, Throwable e) {
        log.error("异常名称:{}", e.getClass().getName());
        // log.error("堆栈跟踪:{}", e.getMessage(), e);
        log.error("异常方法:{}.{}()", joinPoint.getTarget().getClass().getName(), joinPoint.getSignature().getName());
        newErrorLog(joinPoint, e);
    }

    private void newRequestLog(String requestUrl, String requestIp, String requestMethod, String requestController, String requestParameters) {
        SysRequestLog requestLog = new SysRequestLog();
        requestLog.setRequestController(requestController);
        requestLog.setRequestUrl(requestUrl);
        requestLog.setRequestMethod(requestMethod);
        requestLog.setRequestIp(requestIp);
        requestLog.setRequestParameters(requestParameters);
        requestLog.setCreateUserId(ShiroUtils.getCurrentUserId());
        Thread.startVirtualThread(() -> logService.silentCreate(requestLog));
    }

    private void newErrorLog(JoinPoint joinPoint, Throwable e) {
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        SysErrorLog errorLog = new SysErrorLog();
        errorLog.setErrorName(e.getClass().getName());
        errorLog.setErrorStackTrace(getStackTrace(e));
        errorLog.setErrorController(joinPoint.getTarget().getClass().getName() + "." + joinPoint.getSignature().getName() + "()");
        errorLog.setRequestUrl(String.valueOf(request.getRequestURL()));
        errorLog.setRequestMethod(request.getMethod());
        errorLog.setRequestIp(IPUtils.getIpAddr(request));
        errorLog.setRequestParameters(getParams(request));
        errorLog.setCreateUserId(ShiroUtils.getCurrentUserId());
        Thread.startVirtualThread(() -> errorLogService.silentCreate(errorLog));
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
        Map<String, String[]> map = new HashMap<>(request.getParameterMap());
        Set.of(CommonConstant.NO_LOG_REQUEST_PARAMS).forEach(param -> {
            if (map.containsKey(param)) {
                map.put(param, null);
            }
        });
        return JSONUtils.writeValueAsString(map);
    }
}
