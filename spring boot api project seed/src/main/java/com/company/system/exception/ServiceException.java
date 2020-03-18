package com.company.system.exception;

/**
 * @Author Lipanye_Arthur@163.com
 * @Date 2020/3/11 0011 9:54
 * @Decription 服务(业务)异常，该异常只做info 级别的日志记录，详情请看 WebMvcConfigurer
 */
public class ServiceException extends RuntimeException{
    public ServiceException(){

    }

    public ServiceException(String message) {
        super(message);
    }

    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
