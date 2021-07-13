package com.study.utils;

import lombok.Data;

import java.io.Serializable;

/**
 * @author zhangyutong
 */
@Data
public class Result implements Serializable {

    /**
     * 序列化标识
     */
    private static final long serialVersionUID = 1L;

    /**
     * 返回码，全局默认成功码为0，失败为-1
     */
    private int code = 0;

    /**
     * 返回信息描述
     */
    private String message = "";

    /**
     * 返回对象，可以是简单对象，封装处理后的对象，或者MAP
     */
    private Object data;

    /**
     * 方法调用成功，无返回码和描述，不需要返回对象
     *
     * @return Result
     */

    public static Result newSuccess() {
        return new Result();
    }

    /**
     * 方法调用成功，有返回码，有描述，不需要返回对象
     *
     * @param code    消息码
     * @param message 消息详情
     * @return Result
     */
    public static Result newSuccess(int code, String message) {
        Result result = new Result();
        result.setCode(code);
        result.setMessage(message);
        return result;
    }

    /**
     * 方法调用成功，无返回码和描述，有返回对象
     *
     * @param object 实体
     * @return Result
     */
    public static Result newSuccess(Object object) {
        Result result = new Result();
        result.setData(object);
        return result;
    }

    /**
     * 方法调用成功，有返回码和描述，有返回对象
     *
     * @param code    消息码
     * @param message 消息详情
     * @param object  实体
     * @return Result
     */
    public static Result newSuccess(int code, String message, Object object) {
        Result result = new Result();
        result.setCode(code);
        result.setMessage(message);
        result.setData(object);
        return result;
    }

    /**
     * 逻辑处理失败，有返回码和描述，没有返回对象
     *
     * @param code    消息码
     * @param message 消息详情
     * @return Result
     */
    public static Result newFailure(int code, String message) {
        Result result = new Result();
        result.setCode(code != 0 ? code : -1);
        result.setMessage(message);
        return result;
    }

    /**
     * 逻辑处理失败，有返回码和描述，没有返回对象
     *
     * @param message 消息详情
     * @return Result
     */
    public static Result newFailure(String message) {
        Result result = new Result();
        result.setCode(-1);
        result.setMessage(message);
        return result;
    }
}
