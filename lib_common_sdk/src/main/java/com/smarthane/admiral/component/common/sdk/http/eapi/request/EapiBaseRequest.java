package com.smarthane.admiral.component.common.sdk.http.eapi.request;

import android.content.Context;

import com.smarthane.admiral.component.common.sdk.http.eapi.EasyApiHelper;

/**
 * @author smarthane
 * @time 2019/11/10 13:54
 * @describe 通用的请求基类
 */
public abstract class EapiBaseRequest {

    protected Context mContext;

    public EapiBaseRequest(Context mContext) {
        this.mContext = mContext;
    }

    public String getBaseUrl() {
        return EasyApiHelper.config().getBaseUrl();
    }

    public int getRetryDelayMillis() {
        return EasyApiHelper.config().getRetryDelayMillis();
    }

    public int getRetryCount() {
        return EasyApiHelper.config().getRetryCount();
    }

    public String getUrl() {
        return getBaseUrl() + getSuffixUrl();
    }

    public Object getTag() {
        return mContext;
    }

    public abstract String getSuffixUrl();

}
