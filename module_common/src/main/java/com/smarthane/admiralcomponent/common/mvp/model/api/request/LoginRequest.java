package com.smarthane.admiralcomponent.common.mvp.model.api.request;

import android.content.Context;

import com.smarthane.admiralcomponent.common.mvp.model.api.Api;

/**
 * @author smarthane
 * @time 2019/11/17 14:38
 * @describe
 */
public class LoginRequest extends CommonEapiBaseRequest {

    private String username;
    private String password;

    public LoginRequest(Context mContext) {
        super(mContext);
    }

    @Override
    public String getSuffixUrl() {
        return Api.COMMON_USER_LOGIN;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
