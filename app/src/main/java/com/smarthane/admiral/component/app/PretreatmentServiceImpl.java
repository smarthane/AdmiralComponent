package com.smarthane.admiral.component.app;

import android.content.Context;

import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.facade.service.PretreatmentService;
import com.smarthane.admiral.component.common.sdk.core.RouterHub;
import com.smarthane.admiral.core.util.LogUtils;

/**
 * @author smarthane
 * @time 2019/11/10 11:22
 * @describe
 */
@Route(path = RouterHub.APP_PRETREATMENTSERVICE)
public class PretreatmentServiceImpl implements PretreatmentService {

    @Override
    public boolean onPretreatment(Context context, Postcard postcard) {
        // 跳转前预处理，如果需要自行处理跳转，该方法返回 false 即可
        LogUtils.debugInfo("PretreatmentServiceImpl onPretreatment " + postcard.getPath());
        return true;
    }

    @Override
    public void init(Context context) {
        LogUtils.debugInfo("PretreatmentServiceImpl init ");
    }
}
