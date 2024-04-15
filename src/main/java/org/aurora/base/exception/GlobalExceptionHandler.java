package org.aurora.base.exception;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.authz.UnauthorizedException;
import org.aurora.base.util.Result;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UnauthenticatedException.class)
    public String unauthenticatedException() {
        return "forward:/login";
    }

    @ExceptionHandler(UnauthorizedException.class)
    public Result<Object> unauthorizedException(UnauthorizedException e) {
        return Result.fail();
    }

    @ExceptionHandler(AuthenticationException.class)
    public Result<Object> authenticationException(AuthenticationException e) {
        return Result.fail();
    }

    @ExceptionHandler(BusinessException.class)
    public Result<Object> businessException(BusinessException e) {
        return Result.fail(e.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    public Result<Object> runtimeException(RuntimeException e) {
        return Result.fail();
    }

    @ExceptionHandler(Exception.class)
    public Result<Object> exception(Exception e) {
        return Result.fail();
    }
}
