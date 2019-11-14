package com.smarthane.admiralcomponent.common.component.service;

/**
 * @author smarthane
 * @time 2019/11/10 15:49
 * @describe
 */

import android.content.Context;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.smarthane.admiral.component.common.sdk.core.RouterHub;
import com.smarthane.admiral.component.common.service.common.model.CommonInfo;
import com.smarthane.admiral.component.common.service.common.service.CommonInfoService;

/**
 * @author smarthane
 * @time 2019/11/10 10:50
 * @describe 向外提供服务的接口实现类, 在此类中实现一些具有特定功能的方法提供给外部, 即可让一个组件与其他组件或宿主进行交互
 */
@Route(path = RouterHub.COMMON_SERVICE_COMMONINFOSERVICE, name = "Common信息服务")
public class CommonInfoServiceImpl implements CommonInfoService {
    private Context mContext;

    @Override
    public CommonInfo getInfo() {
        return new CommonInfo("这是通用模块：如闪屏、登录、首页等等共有业务写在此模块");
    }

    @Override
    public void init(Context context) {
        mContext = context;
    }
}

