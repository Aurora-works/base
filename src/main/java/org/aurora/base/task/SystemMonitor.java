package org.aurora.base.task;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SystemMonitor {

    String time;
    BigDecimal cpuLoad;
    BigDecimal memoryInUse;
    BigDecimal[] spaceInUse;
}
