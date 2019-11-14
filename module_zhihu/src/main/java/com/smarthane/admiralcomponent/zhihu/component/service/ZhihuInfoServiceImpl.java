package com.smarthane.admiralcomponent.zhihu.component.service;

import android.content.Context;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.smarthane.admiral.component.common.sdk.core.RouterHub;
import com.smarthane.admiral.component.common.service.zhihu.model.ZhihuInfo;
import com.smarthane.admiral.component.common.service.zhihu.service.ZhihuInfoService;
import com.smarthane.admiral.core.util.AdmiralUtils;
import com.smarthane.admiral.core.util.LogUtils;
import com.smarthane.admiralcomponent.zhihu.R;

/**
 * @author smarthane
 * @time 2019/11/10 10:52
 * @describe 向外提供服务的接口实现类, 在此类中实现一些具有特定功能的方法提供给外部, 即可让一个组件与其他组件或宿主进行交互
 */
@Route(path = RouterHub.ZHIHU_SERVICE_ZHIHUINFOSERVICE, name = "知乎信息服务")
public class ZhihuInfoServiceImpl implements ZhihuInfoService {

    private Context mContext;

    @Override
    public ZhihuInfo getInfo() {
        return new ZhihuInfo(AdmiralUtils.getString(mContext, R.string.public_name_zhihu));
    }

    @Override
    public void init(Context context) {
        LogUtils.debugInfo("ZhihuInfoServiceImpl init ");
        mContext = context;
    }
}
