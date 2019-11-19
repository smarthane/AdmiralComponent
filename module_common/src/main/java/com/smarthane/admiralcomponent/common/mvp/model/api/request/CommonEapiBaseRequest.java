package com.smarthane.admiralcomponent.common.mvp.model.api.request;

import android.content.Context;

import com.smarthane.admiral.component.common.sdk.http.eapi.request.EapiBaseRequest;
import com.smarthane.admiralcomponent.common.mvp.model.api.Api;

/**
 * @author smarthane
 * @time 2019/11/17 14:31
 * @describe
 */
public abstract class CommonEapiBaseRequest extends EapiBaseRequest {

    public CommonEapiBaseRequest(Context mContext) {
        super(mContext);
    }

    @Override
    public String getBaseUrl() {
        return Api.COMMON_DOMAIN;
    }
}
