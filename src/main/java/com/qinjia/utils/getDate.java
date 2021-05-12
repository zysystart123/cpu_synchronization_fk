package com.qinjia.utils;

import java.util.Calendar;
import java.util.Date;

/**
 * @author TYJ
 * @note ****
 * @date 2021/1/21
 */
public class getDate {
    public static String getYearMon(){
        Calendar cal = Calendar.getInstance();
        int month = cal.get(Calendar.MONTH) + 1;
        int year = cal.get(Calendar.YEAR);
        String monthstr = month < 10 ? "0" + month : month + "";
        return year + "-" + monthstr;
    }
}
