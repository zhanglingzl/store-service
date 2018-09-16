package com.rxr.store.common.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class NumberHelper {
    public static double divide(double double1, double double2) {
        BigDecimal b1 = new BigDecimal(String.valueOf(double1));
        BigDecimal b2 = new BigDecimal(String.valueOf(double2));
        return b1.divide(b2,4,BigDecimal.ROUND_HALF_UP).doubleValue();
    }
}
