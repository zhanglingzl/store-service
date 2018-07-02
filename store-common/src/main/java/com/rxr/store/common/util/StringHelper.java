package com.rxr.store.common.util;

import java.text.DecimalFormat;

public class StringHelper {
    //流水号加1后返回，流水号长度为4
    private static final String STR_FORMAT = "000000";

    public static String numberAddOne(String serialNo){
        Integer intHao = Integer.parseInt(serialNo);
        intHao++;
        DecimalFormat df = new DecimalFormat(STR_FORMAT);
        return df.format(intHao);
    }

    public static void main(String[] args) {
        int i = 10;
        String aa = "0";
        while(i> 0) {
            aa = numberAddOne(aa);
            i--;
            System.out.println(aa);
        }
    }
}
