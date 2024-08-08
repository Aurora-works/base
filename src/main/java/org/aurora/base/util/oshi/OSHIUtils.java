package org.aurora.base.util.oshi;

import oshi.SystemInfo;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.software.os.OSFileStore;
import oshi.software.os.OperatingSystem;
import oshi.util.FormatUtil;

import java.time.Instant;
import java.util.List;

public class OSHIUtils {

    private static final SystemInfo si;
    private static final HardwareAbstractionLayer hal;
    private static final OperatingSystem os;

    static {
        si = new SystemInfo();
        hal = si.getHardware();
        os = si.getOperatingSystem();
    }

    /**
     * 系统版本（字符串）
     */
    public static String getOSVersionStr() {
        return String.valueOf(os);
    }

    /**
     * 开机时间（字符串）
     */
    public static String getSystemBootTime() {
        return Instant.ofEpochSecond(os.getSystemBootTime()).toString();
    }

    /**
     * 正常运行时间（字符串）
     */
    public static String getSystemUptime() {
        return FormatUtil.formatElapsedSecs(os.getSystemUptime());
    }

    /**
     * cpu（字符串）
     */
    public static String getCpuStr() {
        String cpuName = hal.getProcessor().getProcessorIdentifier().getName();
        int physicalCpuCount = hal.getProcessor().getPhysicalProcessorCount();
        int logicalCpuCount = hal.getProcessor().getLogicalProcessorCount();
        String hertz = FormatUtil.formatHertz(hal.getProcessor().getMaxFreq());
        return cpuName + " " + physicalCpuCount + " physical CPU core(s) " + logicalCpuCount + " logical CPU(s) Max Frequency: " + hertz;
    }

    /**
     * 物理内存（可用）
     */
    public static long getMemoryAvailable() {
        return hal.getMemory().getAvailable();
    }

    /**
     * 物理内存（总计）
     */
    public static long getMemoryTotal() {
        return hal.getMemory().getTotal();
    }

    /**
     * cpu利用率
     */
    public static double getCpuLoad(long delay) {
        return hal.getProcessor().getSystemCpuLoad(delay);
    }

    /**
     * cpu利用率
     */
    public static double getCpuLoad(long[] oldTicks) {
        return hal.getProcessor().getSystemCpuLoadBetweenTicks(oldTicks);
    }

    /**
     * cpu负载计数器
     */
    public static long[] getCpuLoadTicks() {
        return hal.getProcessor().getSystemCpuLoadTicks();
    }

    /**
     * 获取指定元素数量的系统负载平均值
     */
    public static double[] getSystemLoadAverage(int numberOfElements) {
        return hal.getProcessor().getSystemLoadAverage(numberOfElements);
    }

    /**
     * 驱动器的总空间
     */
    public static long[] getTotalSpace() {
        List<OSFileStore> stores = os.getFileSystem().getFileStores();
        int len = stores.size();
        long[] totalSpaceArr = new long[len];
        for (int i = 0; i < len; i++) {
            totalSpaceArr[i] = stores.get(i).getTotalSpace();
        }
        return totalSpaceArr;
    }

    /**
     * 驱动器的可用空间
     */
    public static long[] getUsableSpace() {
        List<OSFileStore> stores = os.getFileSystem().getFileStores();
        int len = stores.size();
        long[] usableSpaceArr = new long[len];
        for (int i = 0; i < len; i++) {
            usableSpaceArr[i] = stores.get(i).getUsableSpace();
        }
        return usableSpaceArr;
    }

    /**
     * 获取驱动器的名称
     */
    public static String[] getFileStoreNameArr() {
        List<OSFileStore> stores = os.getFileSystem().getFileStores();
        int len = stores.size();
        String[] nameArr = new String[len];
        OSFileStore fs;
        for (int i = 0; i < len; i++) {
            fs = stores.get(i);
            nameArr[i] = fs.getName() + " (" + (fs.getDescription().isEmpty() ? "file system" : fs.getDescription()) + ")";
        }
        return nameArr;
    }
}
