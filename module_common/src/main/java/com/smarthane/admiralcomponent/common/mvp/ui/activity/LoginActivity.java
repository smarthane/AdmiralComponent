package com.smarthane.admiralcomponent.common.mvp.ui.activity;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.smarthane.admiral.component.common.sdk.core.RouterHub;
import com.smarthane.admiral.core.base.BaseActivity;
import com.smarthane.admiralcomponent.common.R;

/**
 * @author smarthane
 * @time 2019/11/10 16:27
 * @describe 登录
 */
@Route(path = RouterHub.COMMON_LOGINACTIVITY)
public class LoginActivity extends BaseActivity {
    @Override
    public int setLayoutResouceId() {
        return R.layout.common_activity_login;
    }
}
