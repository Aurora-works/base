package org.aurora.base.util;

/**
 * 公共常量
 */
public interface CommonConstant {

    /**
     * 不记录日志的请求参数
     */
    String[] NO_LOG_REQUEST_PARAMS = {"password", "oldPassword", "newPassword"};

    /**
     * <a href="https://developer.mozilla.org/zh-CN/docs/Web/HTTP/Basics_of_HTTP/MIME_types/Common_types">常见 MIME 类型列表</a>
     */
    String MIME_TYPE_XLSX = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";

    /**
     * 定时任务 SystemMonitor 使用的 Redis Key
     */
    String TASK_REDIS_KEY_SYSTEM_MONITOR = "task:system:monitor";
}
