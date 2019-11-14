package com.smarthane.admiralcomponent.netease.component.service;

import android.content.Context;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.smarthane.admiral.component.common.sdk.core.RouterHub;
import com.smarthane.admiral.component.common.service.netease.model.NeteaseInfo;
import com.smarthane.admiral.component.common.service.netease.service.NeteaseInfoService;
import com.smarthane.admiral.core.util.AdmiralUtils;
import com.smarthane.admiralcomponent.netease.R;

/**
 * @author smarthane
 * @time 2019/11/10 13:43
 * @describe 向外提供服务的接口实现类, 在此类中实现一些具有特定功能的方法提供给外部, 即可让一个组件与其他组件或宿主进行交互
 */
@Route(path = RouterHub.NETEASE_SERVICE_NETEASEINFOSERVICE, name = "网易新闻信息服务")
public class NeteaseInfoServiceImpl implements NeteaseInfoService {

    private Context mContext;

    @Override
    public NeteaseInfo getInfo() {
        return new NeteaseInfo(AdmiralUtils.getString(mContext, R.string.public_name_netease));
    }

    @Override
    public void init(Context context) {
        mContext = context;
    }
}
