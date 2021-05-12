package com.qinjia.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class getBeanFields {

    public static <T> T getSingleRequest(Class<T> obj, String data) {
        String[] split = data.split(",");
    //创建实例
        T instance = null;
        try {
        //获取类中声明的所有字段
        Field[] fields = obj.getDeclaredFields();
        //创建新的实例对象
        instance = obj.newInstance();
        int j = 0;
        for (int i = 0; i < fields.length; i++) {
            //获取字段的名称
            String name = fields[i].getName();
            //把序列化id筛选掉
            if (name.equals("serialVersionUID")) {
                continue;
            }
            //获取字段的类型
            Class<?> type = obj.getDeclaredField(name).getType();
            // 首字母大写
            String replace = name.substring(0, 1).toUpperCase()
                    + name.substring(1);
            //获得setter方法
            Method setMethod = obj.getMethod("set" + replace, type);
            //获取到form表单的所有值
            String str = split[j].replace("\"", "");
            // System.out.println(name+"======="+str);
            //通过setter方法赋值给对应的成员变量
            if (str == null || str.equals("") || str.equals("null")) {
                if (type.isAssignableFrom(String.class)){
                    setMethod.invoke(instance, "");
                }
                j++;
                continue;
                }else if (type.isAssignableFrom(String.class)) {
                        setMethod.invoke(instance, str);
                } else if (type.isAssignableFrom(int.class) || type.isAssignableFrom(Integer.class)) {
                        setMethod.invoke(instance, Integer.parseInt(str));
                } else if (type.isAssignableFrom(Long.class)|| type.isAssignableFrom(long.class)) {
                        setMethod.invoke(instance, Long.valueOf(str));
                } else if (type.isAssignableFrom(BigDecimal.class)) {
                        setMethod.invoke(instance, new BigDecimal(str));
                } else if (type.isAssignableFrom(Double.class) || type.isAssignableFrom(double.class)) {
                        setMethod.invoke(instance, Double.parseDouble(str));
                } else if (type.isAssignableFrom(Boolean.class) || type.isAssignableFrom(boolean.class)) {
                        setMethod.invoke(instance, Boolean.parseBoolean(str));
                } else if (type.isAssignableFrom(Date.class)) {
                        String hms = "";
                        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//初始化Formatter的转换格式。
                        hms = formatter.format(Long.valueOf(str) - 8 * 60 * 60 * 1000L);
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        setMethod.invoke(instance, formatter.parseObject(hms));
                } else if (type.isAssignableFrom(Timestamp.class)) {
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        setMethod.invoke(instance, new Timestamp(dateFormat.parse(str).getTime()));
                }
            j++;
        }
    } catch (Exception e) {
        // TODO: handle exception
        e.printStackTrace();
    }
//返回实体类对象
        return instance;
}

    public static <T> T getSingleRequestTLoanRepayLog(Class<T> obj, String data) {
        Map<String, String> map = replaceJSON(data);
        String[] split = map.get("data").split(",");
        //创建实例
        T instance = null;
        try {
            //获取类中声明的所有字段
            Field[] fields = obj.getDeclaredFields();
            //创建新的实例对象
            instance = obj.newInstance();
            int j = 0;
            for (int i = 0; i < fields.length; i++) {
                //获取字段的名称
                String name = fields[i].getName();
                //把序列化id筛选掉
                if (name.equals("serialVersionUID")) {
                    continue;
                }
                //获取字段的类型
                Class<?> type = obj.getDeclaredField(name).getType();
                // 首字母大写
                String replace = name.substring(0, 1).toUpperCase()
                        + name.substring(1);
                //获得setter方法
                Method setMethod = obj.getMethod("set" + replace, type);
                //获取到form表单的所有值
                String str = split[j].replace("\"", "");
                // System.out.println(name+"======="+str);
                //通过setter方法赋值给对应的成员变量
                if (str == null || str.equals("") || str.equals("null")) {
                    if (type.isAssignableFrom(String.class)){
                        setMethod.invoke(instance, "");
                    }
                    j++;
                    continue;
                }else if (type.isAssignableFrom(String.class)) {
                    if(str.equals("tlrl_pay_detail")){
                        str=map.get("tlrl_pay_detail").replace("\\","");
                    }if(str.equals("tlrl_settle_ret_info")){
                        str=map.get("tlrl_settle_ret_info");
                    }
                    setMethod.invoke(instance, str);
                } else if (type.isAssignableFrom(int.class) || type.isAssignableFrom(Integer.class)) {
                    setMethod.invoke(instance, Integer.parseInt(str));
                } else if (type.isAssignableFrom(Long.class)|| type.isAssignableFrom(long.class)) {
                    setMethod.invoke(instance, Long.valueOf(str));
                } else if (type.isAssignableFrom(BigDecimal.class)) {
                    setMethod.invoke(instance, new BigDecimal(str));
                } else if (type.isAssignableFrom(Double.class) || type.isAssignableFrom(double.class)) {
                    setMethod.invoke(instance, Double.parseDouble(str));
                } else if (type.isAssignableFrom(Boolean.class) || type.isAssignableFrom(boolean.class)) {
                    setMethod.invoke(instance, Boolean.parseBoolean(str));
                } else if (type.isAssignableFrom(Date.class)) {
                    String hms = "";
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//初始化Formatter的转换格式。
                    hms = formatter.format(Long.valueOf(str) - 8 * 60 * 60 * 1000L);
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    setMethod.invoke(instance, formatter.parseObject(hms));
                } else if (type.isAssignableFrom(Timestamp.class)) {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    setMethod.invoke(instance, new Timestamp(dateFormat.parse(str).getTime()));
                }
                j++;
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
//返回实体类对象
        return instance;
    }
    public static Map<String,String> replaceJSON(String data) {
        Map<String, String> map=new HashMap<String,String>();
        //String pattern = "\\[\\{\\\\\\\"(\\w+)\\\\\\\":\\\\\\\"(.+?)\\\\\\\"},*\\]";
        String pattern = "\\[\\{\\\\\\\"(\\w+)\\\\\\\":(.+?)\\},*\\]";
        Pattern patten = Pattern.compile(pattern);
        Matcher matcher = patten.matcher(data);
        String group="";
        while (matcher.find()) {
             group = matcher.group();
             map.put("tlrl_pay_detail",group);
             data=data.replace(group,"tlrl_pay_detail");
             map.put("data",data.replace(group,"tlrl_pay_detail"));
             break;
        }
        if (group.equals("")){
            map.put("tlrl_pay_detail","");
            map.put("data",data);
        }
        pattern="\"fail\",\"(.*?)\",\"9999\"";
        patten = Pattern.compile(pattern);
        matcher = patten.matcher(data);
        while (matcher.find()) {
            group = matcher.group();
            String replace = group.replace("\"fail\",\"", "").replace("\",\"9999\"", "");
            map.put("tlrl_settle_ret_info",replace);
            map.put("data",data.replace(replace,"tlrl_settle_ret_info"));
            break;
        }
        return map;
    }

}
