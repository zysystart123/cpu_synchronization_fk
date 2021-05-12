package com.qinjia.utils;

import java.util.ArrayList;
import java.util.List;

public class getData {
    public static <T> List<Object> getUpdateDate(Class<T> obj,String data) {
        String[] split = data.replace("[{[", "").replace("]}]", "").split("]},\\{\\[");
        List<Object> dataList=new ArrayList<>();
        for (int i = 0; i < split.length; i++) {
            String s = split[i].split("]:\\[")[1];
            Object singleRequest = getBeanFields.getSingleRequest(obj, s);
            dataList.add(singleRequest);
        }
        return dataList;
    }

    public static <T> List<Object> getInsertDate(Class<T> obj,String data) {
        String[] split = data.replace("[[", "").replace("]]", "").split("],\\[");
        List<Object> dataList=new ArrayList<>();
        for (int i = 0; i < split.length; i++) {
            Object singleRequest = getBeanFields.getSingleRequest(obj, split[i]);
            dataList.add(singleRequest);
        }
        return dataList;
    }
    public static <T> List<Object> getDeleteDate(Class<T> obj,String data) {
        String[] split = data.replace("[[", "").replace("]]", "").split("],\\[");
        List<Object> dataList=new ArrayList<>();
        for (int i = 0; i < split.length; i++) {
            Object singleRequest = getBeanFields.getSingleRequest(obj, split[i]);
            dataList.add(singleRequest);
        }
        return dataList;
    }

    public static <T> List<Object> getUpdateDateTLoanRepayLog(Class<T> obj,String data) {
        String[] split = data.replace("[{[", "").replace("]}]", "").split("]},\\{\\[");
        List<Object> dataList=new ArrayList<>();
        for (int i = 0; i < split.length; i++) {
            String s = split[i].split("]:\\[")[1];
            Object singleRequest = getBeanFields.getSingleRequestTLoanRepayLog(obj, s);
            dataList.add(singleRequest);
        }
        return dataList;
    }
    public static <T> List<Object> getInsertDateTLoanRepayLog(Class<T> obj,String data) {
        String[] split = data.replace("[[", "").replace("]]", "").split("],\\[");
        List<Object> dataList=new ArrayList<>();
        for (int i = 0; i < split.length; i++) {
            Object singleRequest = getBeanFields.getSingleRequestTLoanRepayLog(obj, split[i]);
            dataList.add(singleRequest);
        }
        return dataList;
    }
    public static <T> List<Object> getDeleteDateTLoanRepayLog(Class<T> obj,String data) {
        String[] split = data.replace("[[", "").replace("]]", "").split("],\\[");
        List<Object> dataList=new ArrayList<>();
        for (int i = 0; i < split.length; i++) {
            Object singleRequest = getBeanFields.getSingleRequestTLoanRepayLog(obj, split[i]);
            dataList.add(singleRequest);
        }
        return dataList;
    }
}
