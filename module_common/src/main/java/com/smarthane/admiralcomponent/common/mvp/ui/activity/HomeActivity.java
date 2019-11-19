package com.smarthane.admiralcomponent.common.mvp.ui.activity;

import android.content.Context;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.smarthane.admiral.component.common.sdk.core.RouterHub;
import com.smarthane.admiral.core.base.BaseActivity;
import com.smarthane.admiralcomponent.common.R;
import com.smarthane.admiralcomponent.common.mvp.contract.HomeContract;
import com.smarthane.admiralcomponent.common.mvp.presenter.HomePresenter;

/**
 * @author smarthane
 * @time 2019/11/10 16:00
 * @describe 首页
 */
@Route(path = RouterHub.COMMON_HOMEACTIVITY)
public class HomeActivity extends BaseActivity implements HomeContract.View {

    @Override
    public int setLayoutResouceId() {
        return R.layout.common_activity_home;
    }

    @Override
    protected void initPresenter() {
        mPresenter = HomePresenter.build(this);
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
