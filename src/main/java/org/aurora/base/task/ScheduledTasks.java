package org.aurora.base.task;

import lombok.extern.log4j.Log4j2;
import org.aurora.base.util.CommonConstant;
import org.aurora.base.util.math.NumberUtils;
import org.aurora.base.util.oshi.OSHIUtils;
import org.aurora.base.util.redis.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import oshi.SystemInfo;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Log4j2
@Component
public class ScheduledTasks {
    @Autowired
    public ScheduledTasks(RedisUtils redisUtils) {
        this.redisUtils = redisUtils;
    }

    public final RedisUtils redisUtils;

    private static class SystemMonitorHelper {

        private static long[] OLD_TICKS;

        static {
            OLD_TICKS = new SystemInfo().getHardware().getProcessor().getSystemCpuLoadTicks();
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
    }
}
