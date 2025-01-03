package org.aurora.base.exception;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.authz.UnauthorizedException;
import org.aurora.base.common.Result;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 全局异常处理
 */
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UnauthenticatedException.class)
    public String unauthenticatedException() {
        return "forward:/login";
    }

    @ExceptionHandler(AuthenticationException.class)
    @ResponseBody
    public Result<Object> authenticationException(AuthenticationException e) {
        log.error(e.getMessage(), e);
        return Result.fail(null);
    }

    @ExceptionHandler(UnauthorizedException.class)
    @ResponseBody
    public Result<Object> unauthorizedException(UnauthorizedException e) {
        log.error(e.getMessage(), e);
        return Result.fail(null);
    }

    @ExceptionHandler(BusinessException.class)
    @ResponseBody
    public Result<Object> businessException(BusinessException e) {
        log.error(e.getMessage(), e);
        return Result.fail(e.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseBody
    public Result<Object> illegalArgumentException(IllegalArgumentException e) {
        log.error(e.getMessage(), e);
        return Result.fail(e.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseBody
    public Result<Object> runtimeException(RuntimeException e) {
        log.error(e.getMessage(), e);
        return Result.fail(e.getClass().getSimpleName());
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Result<Object> exception(Exception e) {
        log.error(e.getMessage(), e);
        return Result.fail();
    }
}
