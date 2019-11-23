package com.smarthane.admiral.component.common.sdk.http.eapi.request;

import android.content.Context;

import com.smarthane.admiral.component.common.sdk.http.eapi.EasyApiHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * @author smarthane
 * @time 2019/11/10 13:54
 * @describe 通用的请求基类
 * TODO 缓存配置
 */
public abstract class EapiBaseRequest {

    protected Context mContext;
    protected Object mTag;
    protected List<String> mExcludes = new ArrayList<>();
    private StringBuilder mUrlParamsBuilder = new StringBuilder();

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
        String url = getBaseUrl() + getSuffixUrl();
        if (mUrlParamsBuilder.length() > 0) {
            url = url + mUrlParamsBuilder.toString();
        }
        return url;
    }

    public void setTag(Object mTag) {
        this.mTag = mTag;
    }

    public Object getTag() {
        if (mTag != null) {
            return mTag;
        }
        return mContext;
    }

    public EapiBaseRequest addUrlParam(String paramKey, String paramValue) {
        if (paramKey != null && paramValue != null) {
            if (mUrlParamsBuilder.length() == 0) {
                mUrlParamsBuilder.append("?");
            } else {
                mUrlParamsBuilder.append("&");
            }
            mUrlParamsBuilder.append(paramKey).append("=").append(paramValue);
        }
        return this;
    }

    public List<String> getExcludes() {
        mExcludes.add("mContext");
        mExcludes.add("mExcludes");
        mExcludes.add("mUrlParamsBuilder");
        mExcludes.add("mTag");
        return mExcludes;
    }

    public abstract String getSuffixUrl();

}
