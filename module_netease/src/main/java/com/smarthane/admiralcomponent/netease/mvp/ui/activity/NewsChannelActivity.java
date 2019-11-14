package com.smarthane.admiralcomponent.netease.mvp.ui.activity;

import android.os.Bundle;

import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.smarthane.admiral.component.common.sdk.core.RouterHub;
import com.smarthane.admiral.core.base.BaseActivity;
import com.smarthane.admiralcomponent.netease.R;

/**
 * @author smarthane
 * @time 2019/11/10 16:55
 * @describe 选择关注频道
 */
@Route(path = RouterHub.NETEASE_NEWSCHANNELACTIVITY)
public class NewsChannelActivity extends BaseActivity {

    private RecyclerView rvNewsChannelMine;
    private RecyclerView rvNewsChannelMore;


    @Override
    public int setLayoutResouceId() {
        return R.layout.netease_activity_news_channel;
    }

    @Override
    protected void initData() {
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        rvNewsChannelMine = findViewById(R.id.rv_news_channel_mine);
        rvNewsChannelMore = findViewById(R.id.rv_news_channel_more);
    }

    @Override
    protected void bindListener() {
    }

    @Override
    protected void initPresenter() {
    }

    @Override
    protected void execBusiness() {
    }
}
