package com.smarthane.admiral.core.http.retrofiturlmanager.parser;

import com.smarthane.admiral.core.http.retrofiturlmanager.RetrofitUrlManager;

import okhttp3.HttpUrl;

/**
 * @author smarthane
 * @time 2019/11/10 11:24
 * @describe Url解析器
 */
public interface UrlParser {

    /**
     * 这里可以做一些初始化操作
     *
     * @param retrofitUrlManager {RetrofitUrlManager}
     */
    void init(RetrofitUrlManager retrofitUrlManager);

    /**
     * 将 {RetrofitUrlManager#mDomainNameHub} 中映射的 URL 解析成完整的{HttpUrl}
     * 用来替换 @{Request#url} 达到动态切换 URL
     *
     * @param domainUrl 用于替换的 URL 地址
     * @param url       旧 URL 地址
     * @return
     */
    HttpUrl parseUrl(HttpUrl domainUrl, HttpUrl url);
    
}
