package com.smarthane.admiral.demo.mvp.model.entity;

import com.smarthane.admiral.demo.mvp.model.api.Api;

import java.io.Serializable;

/**
 * @author smarthane
 * @time 2019/10/27 16:08
 * @describe
 */
public class BaseResponse<T> implements Serializable {
    private T data;
    private String code;
    private String msg;

    public T getData() {
        return data;
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    /**
     * 请求是否成功
     *
     * @return
     */
    public boolean isSuccess() {
        if (code.equals(Api.RequestSuccess)) {
            return true;
        } else {
            return false;
        }
    }
}
