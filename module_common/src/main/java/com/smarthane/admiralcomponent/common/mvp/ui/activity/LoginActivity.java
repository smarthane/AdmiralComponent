package com.smarthane.admiralcomponent.common.mvp.ui.activity;

import android.content.Context;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.smarthane.admiral.component.common.sdk.core.RouterHub;
import com.smarthane.admiral.core.base.BaseActivity;
import com.smarthane.admiralcomponent.common.R;
import com.smarthane.admiralcomponent.common.mvp.contract.LoginContract;
import com.smarthane.admiralcomponent.common.mvp.presenter.LoginPresenter;

/**
 * @author smarthane
 * @time 2019/11/10 16:27
 * @describe 登录
 */
@Route(path = RouterHub.COMMON_LOGINACTIVITY)
public class LoginActivity extends BaseActivity implements LoginContract.View {

    @Override
    public int setLayoutResouceId() {
        return R.layout.common_activity_login;
    }

    @Override
    protected void initPresenter() {
        mPresenter = LoginPresenter.build(this);
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }
}
