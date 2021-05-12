package com.qinjia.common;

import lombok.Data;

/**
 * @Classname Result
 * @Description TODO
 * @Date 2021/5/11 11:58
 * @Created by 86153
 */
@Data
public class Result<T> {

    private String code;
    private String msg;
    private T result;

    private static final String SUCCESS_MSG = "成功";
    private static final String SUCCESS_code = "0000";


    public Result(String code, String msg){
        this.code = code;
        this.msg =msg;
    }

    public Result(){
        this.code = SUCCESS_code;
        this.msg = SUCCESS_MSG;
    }

    public Result(T res){
        this.code = SUCCESS_code;
        this.msg = SUCCESS_MSG;
        this.result = res;
    }


    public static Result<Object> resultFunction(Object res){
        return new Result<Object>(res);
    }
}
