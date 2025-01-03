package org.aurora.base.aop;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aurora.base.entity.sys.SysErrorLog;
import org.aurora.base.entity.sys.SysRequestLog;
import org.aurora.base.service.sys.SysErrorLogService;
import org.aurora.base.service.sys.SysRequestLogService;
import org.aurora.base.shiro.ShiroUtils;
import org.aurora.base.util.IPUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.Duration;
import java.time.Instant;
import java.util.Objects;

@Slf4j
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

    @Pointcut("execution(* org.aurora.base.controller..*.*(..))")
    private void controllerAspect() {
    }

    @Pointcut("""
            execution(* org.aurora.base.controller..*.*(..)) &&
            !execution(* org.aurora.base.controller.dev.SysDictController.findByCode(..)) &&
            !execution(* org.aurora.base.controller..*.getComboTree(..)) &&
            !execution(* org.aurora.base.controller.IndexController.systemMonitor(..))""")
    private void controllerAspectRecord() {
    }

    @Before("controllerAspectRecord()")
    public void before(JoinPoint joinPoint) {
        newRequestLog(joinPoint);
    }

    @Around("controllerAspect()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        Instant startTime = Instant.now();
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        String requestUrl = String.valueOf(request.getRequestURL());
        String requestIp = IPUtils.getIpAddr(request);
        String requestMethod = request.getMethod();
        String requestController = joinPoint.getTarget().getClass().getName() + "." + joinPoint.getSignature().getName() + "()";
        String requestParameters = RequestUtils.getParams(request);
        log.info("URL:{}", requestUrl);
        log.info("IP:{}", requestIp);
        log.info("请求方式:{}", requestMethod);
        log.info("请求方法:{}", requestController);
        log.info("参数列表:{}", requestParameters);
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

    private void newRequestLog(JoinPoint joinPoint) {
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        SysRequestLog requestLog = SysRequestLog.builder()
                .requestController(joinPoint.getTarget().getClass().getName() + "." + joinPoint.getSignature().getName() + "()")
                .requestUrl(String.valueOf(request.getRequestURL()))
                .requestMethod(request.getMethod())
                .requestIp(IPUtils.getIpAddr(request))
                .requestParameters(RequestUtils.getParams(request))
                .build();
        requestLog.setCreateUserId(ShiroUtils.getCurrentUserId());
        Thread.startVirtualThread(() -> logService.silentCreate(requestLog));
    }

    private void newErrorLog(JoinPoint joinPoint, Throwable e) {
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        SysErrorLog errorLog = SysErrorLog.builder()
                .errorName(e.getClass().getName())
                .errorStackTrace(getStackTrace(e))
                .errorController(joinPoint.getTarget().getClass().getName() + "." + joinPoint.getSignature().getName() + "()")
                .requestUrl(String.valueOf(request.getRequestURL()))
                .requestMethod(request.getMethod())
                .requestIp(IPUtils.getIpAddr(request))
                .requestParameters(RequestUtils.getParams(request))
                .build();
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
}
