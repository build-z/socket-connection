package com.xiaolvche.cloudconnection.vo;

import lombok.Data;

/**
 * @author liaoxh
 * @create 2021-07-25-13:01
 */
@Data
public class ResponseVO <T>{
    private Integer code;

    private String msg;

    private T data;

    private ResponseVO() {

    }

    private ResponseVO(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    private ResponseVO(Integer code, String msg, T data){
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static <T> ResponseVO<T> ok() {
        return new ResponseVO<>(0, "success");
    }

    public static <T> ResponseVO<T> ok(T data) {
        return new ResponseVO<>(0,"success", data);
    }

    public static <T> ResponseVO<T> error(String message) {
        return new ResponseVO<>(-1, message);
    }

    public static <T> ResponseVO<T> error(Integer code, String msg) {
        return new ResponseVO<>(code, msg);
    }

}
