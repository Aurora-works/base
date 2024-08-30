package org.aurora.base.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class NumberUtils {

    public static BigDecimal setScale(double d) {
        return BigDecimal.valueOf(d).setScale(2, RoundingMode.HALF_UP);
    }

    public static BigDecimal setScale(double d, int scale, RoundingMode roundingMode) {
        return BigDecimal.valueOf(d).setScale(scale, roundingMode);
    }

    public static BigDecimal divide(double d1, double d2) {
        BigDecimal b1 = BigDecimal.valueOf(d1);
        BigDecimal b2 = BigDecimal.valueOf(d2);
        return b1.divide(b2, 2, RoundingMode.HALF_UP);
    }

    public static BigDecimal divide(double d1, double d2, int scale, RoundingMode roundingMode) {
        BigDecimal b1 = BigDecimal.valueOf(d1);
        BigDecimal b2 = BigDecimal.valueOf(d2);
        return b1.divide(b2, scale, roundingMode);
    }

    public static BigDecimal multiply(double d1, double d2) {
        BigDecimal b1 = BigDecimal.valueOf(d1);
        BigDecimal b2 = BigDecimal.valueOf(d2);
        return b1.multiply(b2);
    }

    public static BigDecimal movePointRight(double d, int n) {
        return BigDecimal.valueOf(d).movePointRight(n);
    }
}
