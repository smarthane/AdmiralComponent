package com.smarthane.admiralcomponent.zhihu.mvp.ui.activity;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.smarthane.admiral.component.common.res.dialog.ProgresDialog;
import com.smarthane.admiral.component.common.sdk.core.RouterHub;
import com.smarthane.admiral.core.base.BaseActivity;
import com.smarthane.admiralcomponent.zhihu.R;
import com.smarthane.admiralcomponent.zhihu.app.ZhihuConstants;
import com.smarthane.admiralcomponent.zhihu.mvp.contract.ZhihuHomeContract;
import com.smarthane.admiralcomponent.zhihu.mvp.model.entity.DailyListBean;
import com.smarthane.admiralcomponent.zhihu.mvp.presenter.ZhihuHomePresenter;
import com.smarthane.admiralcomponent.zhihu.mvp.ui.adapter.ZhihuHomeAdapter;

import java.util.List;

/**
 * @author smarthane
 * @time 2019/11/10 18:21
 * @describe
 */
@Route(path = RouterHub.ZHIHU_HOMEACTIVITY)
public class ZhihuHomeActivity extends BaseActivity<ZhihuHomePresenter> implements ZhihuHomeContract.View {

    private SmartRefreshLayout mSmartRefreshLayout;
    private RecyclerView mRecyclerView;
    private ZhihuHomeAdapter zhihuHomeAdapter;
    private Dialog mDialog;

    @Override
    public int setLayoutResouceId() {
        return R.layout.zhihu_activity_home;
    }


    @Override
    protected void initView(Bundle savedInstanceState) {
        mDialog = new ProgresDialog(this);
        mSmartRefreshLayout = findViewById(R.id.refreshLayout);
        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        // 如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        zhihuHomeAdapter = new ZhihuHomeAdapter(this);
        mRecyclerView.setAdapter(zhihuHomeAdapter);
    }


    @Override
    protected void bindListener() {
        zhihuHomeAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                DailyListBean.StoriesBean data = ((List<DailyListBean.StoriesBean>)adapter.getData()).get(position);
                ARouter.getInstance()
                        .build(RouterHub.ZHIHU_DETAILACTIVITY)
                        .withInt(ZhihuConstants.DETAIL_ID, data.getId())
                        .withString(ZhihuConstants.DETAIL_TITLE, data.getTitle())
                        .navigation(ZhihuHomeActivity.this);
            }
        });

        mSmartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                mPresenter.requestDailyList();
            }
        });
    }

    @Override
    protected void initPresenter() {
        mPresenter = ZhihuHomePresenter.build(this);
        mPresenter.setZhihuHomeAdapter(zhihuHomeAdapter);
    }


    @Override
    protected void execBusiness() {
        mDialog.show();
    }

    @Override
    public void showLoading() {
    }

    @Override
    public void hideLoading() {
        mSmartRefreshLayout.finishRefresh();
        mDialog.dismiss();
    }
}
