package com.smarthane.admiral.core.http;

import androidx.annotation.NonNull;

import okhttp3.HttpUrl;

/**
 * @author smarthane
 * @time 2019/10/27 13:51
 * @describe 针对于 BaseUrl 在 App 启动时不能确定,需要请求服务器接口动态获取的应用场景
 */
public interface BaseUrl {
    /**
     * 在调用 Retrofit API 接口之前,使用 Okhttp 或其他方式,请求到正确的 BaseUrl 并通过此方法返回
     *
     * @return
     */
    @NonNull
    HttpUrl url();
}
