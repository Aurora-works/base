package org.aurora.base.task;

import lombok.*;

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
