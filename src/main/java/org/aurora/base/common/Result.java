package org.aurora.base.common;

import lombok.Getter;

/**
 * 自定义返回结果
 */
public record Result<T>(Integer code, String message, T data) {

    public static <T> Result<T> success(T data) {
        RequestStatus status = RequestStatus.SUCCESS;
        return new Result<>(status.getCode(), status.getDefaultMessage(), data);
    }

    public static Result<Object> success() {
        return success(null);
    }

    public static Result<Object> fail(String message) {
        return new Result<>(RequestStatus.FAIL.getCode(), message, null);
    }

    public static Result<Object> fail() {
        RequestStatus status = RequestStatus.FAIL;
        return new Result<>(status.getCode(), status.getDefaultMessage(), null);
    }

    /**
     * 自定义返回状态
     */
    @Getter
    private enum RequestStatus {
        /**
         * 请求成功
         */
        SUCCESS(0, ""),
        /**
         * 请求失败
         */
        FAIL(-1, "系统错误");

        private final Integer code;
        private final String defaultMessage;

        RequestStatus(Integer code, String defaultMessage) {
            this.code = code;
            this.defaultMessage = defaultMessage;
        }
    }
}
