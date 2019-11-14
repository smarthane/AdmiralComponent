package com.smarthane.admiralcomponent.netease.mvp.ui.fragment.news;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.smarthane.admiral.core.base.BaseFragment;
import com.smarthane.admiral.core.util.LogUtils;
import com.smarthane.admiralcomponent.netease.R;
import com.smarthane.admiralcomponent.netease.app.NeteaseConstants;
import com.smarthane.admiralcomponent.netease.mvp.contract.NewsListContract;
import com.smarthane.admiralcomponent.netease.mvp.model.entity.NewsSummary;
import com.smarthane.admiralcomponent.netease.mvp.presenter.NewsListPresenter;
import com.smarthane.admiralcomponent.netease.mvp.ui.adapter.NewsListAdapter;

import java.util.Arrays;
import java.util.List;

/**
 * @author smarthane
 * @time 2019/11/10 15:29
 * @describe
 */
public class NewsListFrament extends BaseFragment<NewsListPresenter> implements NewsListContract.View {


    private SmartRefreshLayout mSmartRefreshLayout;
    private RecyclerView mRecyclerView;
    private NewsListAdapter mNewsListAdapter;


    private String mNewsId;
    private String mNewsType;
    private int mStartPage=0;

    @Override
    public int setLayoutResouceId() {
        return R.layout.netease_fragment_news_list;
    }

    @Override
    protected void initData(Bundle arguments) {
        if (arguments != null) {
            mNewsId = arguments.getString(NeteaseConstants.NEWS_ID);
            mNewsType = arguments.getString(NeteaseConstants.NEWS_TYPE);
        }
    }

    @Override
    protected void initView() {
        mSmartRefreshLayout = findViewById(R.id.refreshLayout);
        mRecyclerView = findViewById(R.id.recyclerView);

        mNewsListAdapter = new NewsListAdapter(getActivity());
        mRecyclerView.setAdapter(mNewsListAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        // 如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    protected void initPresenter() {
        mPresenter = NewsListPresenter.build(this);
        mPresenter.setNewsListAdapter(mNewsListAdapter);
    }

    @Override
    protected void bindListener() {
        mSmartRefreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {

            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                mPresenter.getNewsListDataRequest(false, mNewsType, mNewsId);
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                mPresenter.getNewsListDataRequest(true, mNewsType, mNewsId);
            }
        });
    }

    @Override
    protected void execBusiness() {
        mPresenter.getNewsListDataRequest(true, mNewsType, mNewsId);
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
    }
}
