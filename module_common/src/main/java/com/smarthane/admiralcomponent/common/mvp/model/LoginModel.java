package com.smarthane.admiralcomponent.common.mvp.model;

import com.smarthane.admiral.component.common.sdk.http.eapi.EapiCallback;
import com.smarthane.admiral.component.common.sdk.http.eapi.EasyApiHelper;
import com.smarthane.admiral.core.mvp.BaseModel;
import com.smarthane.admiral.core.util.LogUtils;
import com.smarthane.admiralcomponent.common.mvp.contract.LoginContract;
import com.smarthane.admiralcomponent.common.mvp.model.api.request.LoginRequest;
import com.smarthane.admiralcomponent.common.mvp.model.entity.LoginResponse;

/**
 * @author smarthane
 * @time 2019/11/10 16:38
 * @describe
 */
public class LoginModel extends BaseModel implements LoginContract.Model {

    public LoginModel() {
    }

    @Override
    public void login(LoginRequest request, EapiCallback<LoginResponse> eapiCallback) {
        EasyApiHelper.post(request, eapiCallback);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtils.debugInfo("module_common LoginModel onDestroy");
    }

}