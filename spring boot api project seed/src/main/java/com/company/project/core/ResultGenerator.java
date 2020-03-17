package com.company.project.core;

/**
 * @Author Lipanye_Arthur@163.com
 * @Date 2020/03/17 13:23
 * @Decription 响应结果生成工具
 */
public class ResultGenerator {
    private static final String DEFAULT_SUCCESS_MESSAGE = "SUCCESS";

    public static Result getSuccessResult(){
        return new Result().setCode(ResultCode.SUCCESS).setMessage(DEFAULT_SUCCESS_MESSAGE);
    }

    public static<T> Result<T> getSuccessResult(T data){
        return new Result<>().setCode(ResultCode.SUCCESS).setMessage(DEFAULT_SUCCESS_MESSAGE).setData(data);
    }

    public static Result getFailResult(String message){
        return new Result().setCode(ResultCode.FAIL).setMessage(message);
    }

}
