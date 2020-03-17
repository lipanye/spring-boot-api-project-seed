package com.company.project.core;

/**
 * @Author Lipanye_Arthur@163.com
 * @Date 2020/3/11 0011 9:47
 * @Decription 响应码枚举，参考Http响应状态吗
 */
public enum ResultCode {
    SUCCESS(200),//成功
    FAIL(400),//失败
    UNAUTHORIZED(401),//未认证（签名错误）
    NOT_FOUND(404),//接口不存在
    INTERNAL_SERVER_ERROR(500);//服务器内部错误

    private final int code;

    ResultCode(int code){
        this.code = code;
    }
    public int code(){
        return code;
    }
}
