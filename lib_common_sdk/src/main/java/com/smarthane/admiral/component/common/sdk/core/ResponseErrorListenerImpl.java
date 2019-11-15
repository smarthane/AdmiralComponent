package com.smarthane.admiral.component.common.sdk.core;

import android.content.Context;
import android.net.ParseException;

import com.google.gson.JsonIOException;
import com.google.gson.JsonParseException;
import com.smarthane.admiral.core.integration.AppManager;
import com.smarthane.admiral.core.util.LogUtils;

import org.json.JSONException;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import com.smarthane.admiral.core.base.rx.errorhandler.ResponseErrorListener;
import retrofit2.HttpException;

/**
 * @author smarthane
 * @time 2019/11/10 14:18
 * @describe 全局异常处理
 */
public class ResponseErrorListenerImpl implements ResponseErrorListener {


    @Override
    public void handleResponseError(Context context, Throwable t) {
        LogUtils.debugInfo("ResponseErrorListenerImpl handleResponseError");
        // 这里不光只能打印错误, 还可以根据不同的错误做出不同的逻辑处理
        // 这里只是对几个常用错误进行简单的处理, 展示这个类的用法, 在实际开发中请您自行对更多错误进行更严谨的处理
        String msg = "未知错误";
        if (t instanceof UnknownHostException) {
            msg = "网络不可用";
        } else if (t instanceof SocketTimeoutException) {
            msg = "请求网络超时";
        } else if (t instanceof HttpException) {
            HttpException httpException = (HttpException) t;
            msg = convertStatusCode(httpException);
        } else if (t instanceof JsonParseException || t instanceof ParseException || t instanceof JSONException || t instanceof JsonIOException) {
            msg = "数据解析错误";
        }
        AppManager.getAppManager().showSnackbar(msg, false);
    }

    private String convertStatusCode(HttpException httpException) {
        String msg;
        if (httpException.code() == 500) {
            msg = "服务器发生错误";
        } else if (httpException.code() == 404) {
            msg = "请求地址不存在";
        } else if (httpException.code() == 403) {
            msg = "请求被服务器拒绝";
        } else if (httpException.code() == 307) {
            msg = "请求被重定向到其他页面";
        } else {
            msg = httpException.message();
        }
        return msg;
    }
}
