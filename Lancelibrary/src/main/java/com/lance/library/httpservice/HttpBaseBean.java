package com.lance.library.httpservice;

import com.alibaba.fastjson.JSON;

/**
 * Created by Tjcx on 2017/9/18.
 */

public class HttpBaseBean<T> {

    private int code;

    private String message;

    private boolean firstStepSuccess = true;

    private T data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isFirstStepSuccess() {
        return firstStepSuccess;
    }

    public void setFirstStepSuccess(boolean firstStepSuccess) {
        this.firstStepSuccess = firstStepSuccess;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "HttpBaseBean{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", firstStepSuccess=" + firstStepSuccess +
                ", data=" + JSON.toJSONString(data) +
                '}';
    }
}
