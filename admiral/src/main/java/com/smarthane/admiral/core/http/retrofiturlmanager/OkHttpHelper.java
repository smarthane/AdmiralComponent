package com.smarthane.admiral.core.http.retrofiturlmanager;

import okhttp3.HttpUrl;

/**
 * @author smarthane
 * @time 2019/11/10 11:32
 * @describe OkHttp 工具类
 */
public class OkHttpHelper {

    private OkHttpHelper() {
        throw new IllegalStateException("do not instantiation me");
    }

    public static HttpUrl checkUrl(String url) {
        HttpUrl parseUrl = HttpUrl.parse(url);
        if (null == parseUrl) {
            throw new InvalidUrlException(url);
        } else {
            return parseUrl;
        }
    }

    public static <T> T checkNotNull(T object, String message) {
        if (object == null) {
            throw new NullPointerException(message);
        }
        return object;
    }
}
