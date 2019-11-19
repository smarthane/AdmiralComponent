package com.smarthane.admiralcomponent.common.mvp.model;

import com.smarthane.admiral.component.common.sdk.http.eapi.EapiCallback;
import com.smarthane.admiral.component.common.sdk.http.eapi.EasyApiHelper;
import com.smarthane.admiral.core.mvp.BaseModel;
import com.smarthane.admiral.core.util.LogUtils;
import com.smarthane.admiralcomponent.common.mvp.contract.HomeContract;
import com.smarthane.admiralcomponent.common.mvp.model.api.request.BannerRequest;
import com.smarthane.admiralcomponent.common.mvp.model.entity.BannerResponse;

/**
 * @author smarthane
 * @time 2019/11/17 14:43
 * @describe
 */
public class HomeModel extends BaseModel implements HomeContract.Model {

    private Object tag;

    public HomeModel() {
    }

    @Override
    public void loadBanner(BannerRequest request, EapiCallback<BannerResponse> callback) {
        if (request != null) {
            tag = request.getTag();
        }
        EasyApiHelper.get(request, callback);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtils.debugInfo("module_common HomeModel onDestroy");
        if (tag != null) {
            EasyApiHelper.cancel(tag);
        }
    }
}
