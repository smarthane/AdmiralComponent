package com.smarthane.admiralcomponent.wanandroid.component.service;

import android.content.Context;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.smarthane.admiral.component.common.sdk.core.RouterHub;
import com.smarthane.admiral.component.common.service.wanandroid.model.WanAndroidInfo;
import com.smarthane.admiral.component.common.service.wanandroid.service.WanAndroidInfoService;
import com.smarthane.admiral.core.util.AdmiralUtils;
import com.smarthane.admiralcomponent.wanandroid.R;

/**
 * @author smarthane
 * @time 2019/11/10 21:43
 * @describe
 */
@Route(path = RouterHub.WANANDROID_SERVICE_WANANDROIDINFOSERVICE, name = "玩Android信息服务")
public class WanAndroidInfoServiceImpl implements WanAndroidInfoService {

    private Context mContext;

    @Override
    public WanAndroidInfo getInfo() {
        return new WanAndroidInfo(AdmiralUtils.getString(mContext, R.string.public_name_wanandroid));
    }

    @Override
    public void init(Context context) {
        mContext = context;
    }
}