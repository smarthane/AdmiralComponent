package com.smarthane.admiralcomponent.common.mvp.ui.activity;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.smarthane.admiral.component.common.sdk.core.RouterHub;
import com.smarthane.admiral.core.base.BaseActivity;
import com.smarthane.admiralcomponent.common.R;

/**
 * @author smarthane
 * @time 2019/11/10 16:00
 * @describe 首页
 */
@Route(path = RouterHub.COMMON_HOMEACTIVITY)
public class HomeActivity extends BaseActivity {
    @Override
    public int setLayoutResouceId() {
        return R.layout.common_activity_home;
    }
}
