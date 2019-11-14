package com.smarthane.admiralcomponent.gank.mvp.ui.activity;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.smarthane.admiral.component.common.res.dialog.ProgresDialog;
import com.smarthane.admiral.component.common.sdk.core.RouterHub;
import com.smarthane.admiral.core.base.BaseActivity;
import com.smarthane.admiralcomponent.gank.R;
import com.smarthane.admiralcomponent.gank.mvp.contract.GankHomeContract;
import com.smarthane.admiralcomponent.gank.mvp.presenter.GankHomePresenter;
import com.smarthane.admiralcomponent.gank.mvp.ui.adapter.GankHomeAdapter;

/**
 * @author smarthane
 * @time 2019/10/27 9:54
 * @describe 展示 View 的用法
 */
@Route(path = RouterHub.GANK_HOMEACTIVITY)
public class GankHomeActivity extends BaseActivity<GankHomePresenter> implements GankHomeContract.View {

    private SmartRefreshLayout mSmartRefreshLayout;
    private RecyclerView mRecyclerView;
    private GankHomeAdapter mGankHomeAdapter;

    private Dialog mDialog;

    @Override
    public int setLayoutResouceId() {
        return R.layout.gank_activity_home;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        mDialog = new ProgresDialog(this);
        mSmartRefreshLayout = findViewById(R.id.refreshLayout);
        mRecyclerView = findViewById(R.id.recyclerView);
        mGankHomeAdapter = new GankHomeAdapter(this);
        mRecyclerView.setAdapter(mGankHomeAdapter);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        // 如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    protected void bindListener() {
        mSmartRefreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {

            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                mPresenter.requestGirls(false);
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                mPresenter.requestGirls(true);
            }
        });
    }

    @Override
    protected void initPresenter() {
        mPresenter = GankHomePresenter.build(this);
        mPresenter.setGankHomeAdapter(mGankHomeAdapter);
    }

    @Override
    protected void execBusiness() {
        mDialog.show();
    }

    @Override
    public void startLoadMore() {
    }

    @Override
    public void endLoadMore() {
        mSmartRefreshLayout.finishLoadMore();
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
