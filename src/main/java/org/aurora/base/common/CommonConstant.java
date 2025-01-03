package org.aurora.base.common;

import org.aurora.base.task.ScheduledTasks;

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
     * 定时任务 SystemMonitor 在 Redis 中的 Key
     */
    String TASK_REDIS_KEY_SYSTEM_MONITOR = "task:system:monitor:" + ScheduledTasks.SystemMonitorHelper.HOST_NAME;

    /**
     * 是否保存 IP 地址
     */
    boolean SAVE_IP = false;
}
