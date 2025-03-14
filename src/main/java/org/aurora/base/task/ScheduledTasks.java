package org.aurora.base.task;

import lombok.extern.slf4j.Slf4j;
import org.aurora.base.common.CommonConstant;
import org.aurora.base.util.NumberUtils;
import org.aurora.base.util.OSHIUtils;
import org.aurora.base.util.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class ScheduledTasks {
    @Autowired
    public ScheduledTasks(RedisUtils redisUtils) {
        this.redisUtils = redisUtils;
    }

    public final RedisUtils redisUtils;

    public static class SystemMonitorHelper {

        private static long[] OLD_TICKS;
        public final static String HOST_NAME;

        static {
            OLD_TICKS = OSHIUtils.getCpuLoadTicks();
            try {
                HOST_NAME = InetAddress.getLocalHost().getHostName();
            } catch (UnknownHostException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Scheduled(cron = "0 * * * * *")
    public void SystemMonitor() {
        // 当前时间
        String time = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm").format(LocalDateTime.now());
        // cpu
        BigDecimal cpuLoad = NumberUtils.movePointRight(OSHIUtils.getCpuLoad(SystemMonitorHelper.OLD_TICKS), 2).setScale(2, RoundingMode.HALF_UP);
        SystemMonitorHelper.OLD_TICKS = OSHIUtils.getCpuLoadTicks();
        // 内存
        BigDecimal memoryInUse = NumberUtils.divide(OSHIUtils.getMemoryAvailable(), OSHIUtils.getMemoryTotal(), 4, RoundingMode.HALF_UP)
                .negate()
                .add(BigDecimal.valueOf(1))
                .movePointRight(2);
        // 磁盘
        long[] totalSpaceArr = OSHIUtils.getTotalSpace();
        long[] usableSpaceArr = OSHIUtils.getUsableSpace();
        int spaceLen = totalSpaceArr.length;
        BigDecimal[] spaceInUse = new BigDecimal[spaceLen];
        for (int i = 0; i < spaceLen; i++) {
            spaceInUse[i] = NumberUtils.divide(usableSpaceArr[i], totalSpaceArr[i], 4, RoundingMode.HALF_UP)
                    .negate()
                    .add(BigDecimal.valueOf(1))
                    .movePointRight(2);
        }
        SystemMonitor dto = new SystemMonitor(time, cpuLoad, memoryInUse, spaceInUse);
        if (redisUtils.rightPush(CommonConstant.TASK_REDIS_KEY_SYSTEM_MONITOR, dto) > 60) {
            redisUtils.trim(CommonConstant.TASK_REDIS_KEY_SYSTEM_MONITOR, -60, -1);
        }
        redisUtils.expire(CommonConstant.TASK_REDIS_KEY_SYSTEM_MONITOR, 1, TimeUnit.HOURS);
    }
}
