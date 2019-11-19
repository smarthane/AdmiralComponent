package com.smarthane.admiralcomponent.common.mvp.contract;

import android.content.Context;

import com.smarthane.admiral.component.common.sdk.http.eapi.EapiCallback;
import com.smarthane.admiral.core.mvp.IModel;
import com.smarthane.admiral.core.mvp.IView;
import com.smarthane.admiralcomponent.common.mvp.model.api.request.BannerRequest;
import com.smarthane.admiralcomponent.common.mvp.model.entity.BannerResponse;

/**
 * @author smarthane
 * @time 2019/11/17 14:43
 * @describe
 */
public interface HomeContract {

    /**
     * 对于经常使用的关于UI的方法可以定义到IView中,如显示隐藏进度条,和显示文字消息
     */
    interface View extends IView {
        Context getContext();
    }

    /**
     * Model层定义接口,外部只需关心Model返回的数据,无需关心内部细节,如是否使用缓存
     */
    interface Model extends IModel {
        void loadBanner(BannerRequest request, EapiCallback<BannerResponse> callback);
    }

}
