package com.qinjia.utils;

import com.qinjia.pojo.TCustUser;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * @author TYJ
 * @note ****
 * @date 2021/1/20
 */
public class getRandomNum {
    /**
     * 返回随机数(0-5随机)*100的随机金额
     */
    public static double getRandomAmt() {
        Random ran = new Random();
        double x = ran.nextInt(6);
        return x * 100;
    }

    /**
     * 返回随机数(1-3随机),大于等于1小于等于2，返回true,大于2返回false
     */
    public static boolean getRandomNoLog() {
        Random ran = new Random();
        int x = ran.nextInt(3) + 1;
        if (x > 2) return false;
        else return true;
    }

    /**
     * 返回随机数(1-9随机),大于等于1小于等于6，返回true,大于6返回false
     */
    public static boolean getRandomNo() {
        Random ran = new Random();
        int x = ran.nextInt(9) + 1;
        if (x > 6) return false;
        else return true;
    }

    /**
     * 拒绝原因为资金方拒绝，返回true,亲家原因，返回false
     */
    public static boolean getRandomLenderFail() {
        Random ran = new Random();
        int x = ran.nextInt(100) + 1;
        if (x > 2) return true;
        else return false;
    }

    /**
     * 根据传入的数量，返回要随机插入的条数
     */
    public static int getRandom(int num) {
        Random ran = new Random();
        int[] numArr = {num-1,num, num + 1};
        int x = ran.nextInt(3);
        return numArr[x];
    }

    /**
     * 根据传入的数量，返回要随机插入的拒绝条数
     */
    public static int getRandomNo(int num) {
        Random ran = new Random();
        int[] numArr = {num * 7 - 1, num * 7, num * 7 + 1};
        int x = ran.nextInt(3);
        return numArr[x];
    }

    /**
     * 根据传入长度，返回要随机插入的下标
     */
    public static int getRandomIndex(int num) {
        Random ran = new Random();
        int x = ran.nextInt(num);
        return x;
    }

    /**
     * 根据传入的金额，返回要随机插入的金额
     */
    public static double getRandom(Double amt) {
        Random ran = new Random();
        double x = ran.nextInt(21) * 100;
        if (amt - x >= 1000) return amt - x;
        else return 1000;
    }

    /**
     * 根据传入的日期，返回要随机插入的日期
     */
    public static Date getRandom(Date date) {
        Random ran = new Random();
        int x = ran.nextInt(120) + 1;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.SECOND, x);

        return calendar.getTime();
    }

    /**
     * 根据传入的集合，返回要随机插入的id
     */
    public static int getRandomId(List<Integer> list) {
        Random ran = new Random();
        int x = ran.nextInt(list.size());
        return list.get(x);
    }

    /**
     * 根据传入的集合，返回提现失败拒绝的原因
     */
    public static String getStrByIntArr(List<Integer> list) {
        String str = "";
        for (int i = 0; i < list.size(); i++) {
            if (i == 0) {
                str = list.get(i) + "";
            } else {
                str = str + "," + list.get(i);
            }
        }
        return str;
    }


    /**
     * 返回授信拒绝用户的等级
     */
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
