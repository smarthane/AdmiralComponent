package com.smarthane.admiralcomponent.gank.component.service;

import android.content.Context;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.smarthane.admiral.component.common.sdk.core.RouterHub;
import com.smarthane.admiral.component.common.service.gank.model.GankInfo;
import com.smarthane.admiral.component.common.service.gank.service.GankInfoService;
import com.smarthane.admiral.core.util.AdmiralUtils;
import com.smarthane.admiralcomponent.gank.R;

/**
 * @author smarthane
 * @time 2019/11/10 10:50
 * @describe 向外提供服务的接口实现类, 在此类中实现一些具有特定功能的方法提供给外部, 即可让一个组件与其他组件或宿主进行交互
 */
@Route(path = RouterHub.GANK_SERVICE_GANKINFOSERVICE, name = "干货集中营信息服务")
public class GankInfoServiceImpl implements GankInfoService {
    private Context mContext;

    @Override
    public GankInfo getInfo() {
        return new GankInfo(AdmiralUtils.getString(mContext, R.string.public_name_gank));
    }

    @Override
    public void init(Context context) {
        mContext = context;
    }
}
