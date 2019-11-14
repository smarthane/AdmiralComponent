package com.smarthane.admiral.core.http.retrofiturlmanager;

import android.text.TextUtils;

/**
 * @author smarthane
 * @time 2019/11/10 11:27
 * @describe Url 无效的异常
 */
public class InvalidUrlException extends RuntimeException {

    public InvalidUrlException(String url) {
        super("You've configured an invalid url : " + (TextUtils.isEmpty(url) ? "EMPTY_OR_NULL_URL" : url));
    }

}
