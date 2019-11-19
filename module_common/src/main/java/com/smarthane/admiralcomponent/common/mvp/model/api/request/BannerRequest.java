package com.smarthane.admiralcomponent.common.mvp.model.api.request;

import android.content.Context;

import com.smarthane.admiralcomponent.common.mvp.model.api.Api;

/**
 * @author smarthane
 * @time 2019/11/17 14:37
 * @describe
 */
public class BannerRequest extends CommonEapiBaseRequest {

    public BannerRequest(Context mContext) {
        super(mContext);
    }

    @Override
    public String getSuffixUrl() {
        return Api.COMMON_BANNER;
    }
}
