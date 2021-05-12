package com.qinjia;


import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import static java.lang.Thread.sleep;


/**
 * @author TYJ
 * @note ****
 * @date 2021/1/20
 */
public class randomtest {
    public static void main(String[] args) {
        long start= System.currentTimeMillis(); //获取结束时间
        long end= System.currentTimeMillis(); //获取结束时间
    }

    public static int getRandom(int num) {
        Random ran = new Random();
        int[] numArr = {num, num + 1};
        int x = ran.nextInt(2);
        return numArr[x];
    }
    public static String getYearMon(int num1, int num2) {
        NumberFormat numberFormat = NumberFormat.getInstance();
        // 设置bai精确到小数点后du2位  
        numberFormat.setMaximumFractionDigits(2);
        String result = numberFormat.format((float) num1 / (float) num2 * 100);
        return result;
    }

    public static String getCreditNoUser() {
        double A1 = 0.094431339;
        double A2 = 0.094453648;
        double A3 = 0.094547423;
        double A4 = 0.094417669;
        double B1 = 0.077800368;
        double B2 = 0.077804874;
        double B3 = 0.077782471;
        double B4 = 0.0777734;
        double C1 = 0.07769061;
        double C2 = 0.077801942;
        double C3 = 0.077708538;
        double C4 = 0.077787717;
        Random ran = new Random();
        double result = ran.nextDouble();
        if (result <= A1) return "A1";
        if (A1 < result && result <= A1 + A2) return "A2";
        if (A1 + A2 < result && result <= A1 + A2 + A3) return "A3";
        if (A1 + A2 + A3 < result && result <= A1 + A2 + A3 + A4) return "A4";
        if (A1 + A2 + A3 + A4 < result && result <= A1 + A2 + A3 + A4 + B1) return "B1";
        if (A1 + A2 + A3 + A4 + B1 < result && result <= A1 + A2 + A3 + A4 + B1 + B2) return "B2";
        if (A1 + A2 + A3 + A4 + B1 + B2 < result && result <= A1 + A2 + A3 + A4 + B1 + B2 + B3) return "B3";
        if (A1 + A2 + A3 + A4 + B1 + B2 + B3 < result && result <= A1 + A2 + A3 + A4 + B1 + B2 + B3 + B4) return "B4";
        if (A1 + A2 + A3 + A4 + B1 + B2 + B3 + B4 < result && result <= A1 + A2 + A3 + A4 + B1 + B2 + B3 + B4 + C1)
            return "C1";
        if (A1 + A2 + A3 + A4 + B1 + B2 + B3 + B4 + C1 < result && result <= A1 + A2 + A3 + A4 + B1 + B2 + B3 + B4 + C1 + C2)
            return "C2";
        if (A1 + A2 + A3 + A4 + B1 + B2 + B3 + B4 + C1 + C2 < result && result <= A1 + A2 + A3 + A4 + B1 + B2 + B3 + B4 + C1 + C2 + C3)
            return "C3";
        return "C4";
    }
}
