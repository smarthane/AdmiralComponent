package com.smarthane.admiral.demo.mvp.ui.activity;

import com.smarthane.admiral.core.base.AppComponent;
import com.smarthane.admiral.core.base.BaseActivity;
import com.smarthane.admiral.demo.mvp.contract.UserContract;
import com.smarthane.admiral.demo.mvp.model.entity.User;
import com.smarthane.admiral.demo.mvp.presenter.UserPresenter;
import com.smarthane.admiralcomponent.R;

import java.util.List;

/**
 * @author smarthane
 * @time 2019/10/27 16:15
 * @describe
 */
public class UserActivity extends BaseActivity<UserPresenter> implements UserContract.View {

    @Override
    public int setLayoutResouceId() {
        return R.layout.activity_user;
    }

    @Override
    protected void initPresenter() {
        mPresenter = UserPresenter.build(this);
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void shonContent(List<User> userList) {
        AppComponent.get().fetchAppManager().showSnackbar("userSize: " + userList.size(), false);
    }
}
