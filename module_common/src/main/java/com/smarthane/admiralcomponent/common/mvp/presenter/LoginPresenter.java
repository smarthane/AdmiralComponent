package com.smarthane.admiralcomponent.common.mvp.presenter;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.OnLifecycleEvent;

import com.smarthane.admiral.component.common.sdk.http.eapi.EapiCallback;
import com.smarthane.admiral.core.mvp.BasePresenter;
import com.smarthane.admiral.core.util.LogUtils;
import com.smarthane.admiralcomponent.common.mvp.contract.LoginContract;
import com.smarthane.admiralcomponent.common.mvp.model.LoginModel;
import com.smarthane.admiralcomponent.common.mvp.model.api.request.LoginRequest;
import com.smarthane.admiralcomponent.common.mvp.model.entity.LoginResponse;

/**
 * @author smarthane
 * @time 2019/11/10 16:38
 * @describe
 */
public class LoginPresenter extends BasePresenter<LoginContract.Model, LoginContract.View> {

    private LoginPresenter(LoginContract.Model model, LoginContract.View rootView) {
        super(model, rootView);
    }

    public static LoginPresenter build(LoginContract.View rootView) {
        return new LoginPresenter(new LoginModel(), rootView);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    void onCreate() {
        LogUtils.debugInfo("module_common LoginPresenter onCreate");
        doLogin("admiralcomponents", "admiralcomponents");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtils.debugInfo("module_common LoginPresenter onDestroy");
    }

    public void doLogin(String userName, String password) {
        LoginRequest request = new LoginRequest(mRootView.getContext());
        request.setUsername(userName);
        request.setPassword(password);
        mModel.login(request, new EapiCallback<LoginResponse>() {
            @Override
            public void onSuccess(LoginResponse data) {
                LogUtils.debugInfo("doLogin -----------> " + data.getErrorCode());
            }

            @Override
            public void onFail(Throwable t) {

            }
        });
    }
}
