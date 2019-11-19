package com.smarthane.admiralcomponent.common.mvp.presenter;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.OnLifecycleEvent;

import com.smarthane.admiral.component.common.sdk.http.eapi.EapiCallback;
import com.smarthane.admiral.core.mvp.BasePresenter;
import com.smarthane.admiral.core.util.LogUtils;
import com.smarthane.admiralcomponent.common.mvp.contract.HomeContract;
import com.smarthane.admiralcomponent.common.mvp.model.HomeModel;
import com.smarthane.admiralcomponent.common.mvp.model.api.request.BannerRequest;
import com.smarthane.admiralcomponent.common.mvp.model.entity.BannerResponse;

/**
 * @author smarthane
 * @time 2019/11/17 14:43
 * @describe
 */
public class HomePresenter extends BasePresenter<HomeContract.Model, HomeContract.View> {

    private HomePresenter(HomeContract.Model model, HomeContract.View rootView) {
        super(model, rootView);
    }

    public static HomePresenter build(HomeContract.View rootView) {
        return new HomePresenter(new HomeModel(), rootView);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    void onCreate() {
        LogUtils.debugInfo("module_common HomePresenter onCreate");
        loadBanner();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtils.debugInfo("module_common HomePresenter onDestroy");
    }

    public void loadBanner() {
        BannerRequest request = new BannerRequest(mRootView.getContext());
        mModel.loadBanner(request, new EapiCallback<BannerResponse>() {
            @Override
            public void onSuccess(BannerResponse data) {
                LogUtils.debugInfo("loadBanner -----------> " + data.getData().size());
            }

            @Override
            public void onFail(Throwable t) {

            }
        });
    }

}
