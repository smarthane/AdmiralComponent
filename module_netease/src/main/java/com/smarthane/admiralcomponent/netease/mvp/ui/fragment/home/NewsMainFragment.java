package com.smarthane.admiralcomponent.netease.mvp.ui.fragment.home;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.smarthane.admiral.component.common.sdk.core.RouterHub;
import com.smarthane.admiral.component.common.sdk.util.ARouterUtils;
import com.smarthane.admiral.core.base.BaseFragment;
import com.smarthane.admiral.core.base.BaseFragmentAdapter;
import com.smarthane.admiralcomponent.netease.R;
import com.smarthane.admiralcomponent.netease.app.NeteaseConstants;
import com.smarthane.admiralcomponent.netease.mvp.contract.NewsMainContract;
import com.smarthane.admiralcomponent.netease.mvp.model.entity.NewsChannelTable;
import com.smarthane.admiralcomponent.netease.mvp.presenter.NewsMainPresenter;
import com.smarthane.admiralcomponent.netease.mvp.ui.fragment.news.NewsListFrament;

import java.util.ArrayList;
import java.util.List;

/**
 * @author smarthane
 * @time 2019/11/10 16:43
 * @describe 新闻首页
 */
public class NewsMainFragment extends BaseFragment<NewsMainPresenter> implements NewsMainContract.View {

    private Toolbar mToolbar;
    private TabLayout mTabLayout;
    private ImageView ivAddChannel;
    private ViewPager mViewPager;
    private FloatingActionButton mFab;

    private BaseFragmentAdapter mBaseFragmentAdapter;

    @Override
    public int setLayoutResouceId() {
        return R.layout.netease_fragment_main_news;
    }

    @Override
    protected void initData(Bundle arguments) {
    }

    @Override
    public void initPresenter() {
        mPresenter = NewsMainPresenter.build(this);
    }

    @Override
    public void initView() {
        mToolbar = findViewById(R.id.toolbar);
        mTabLayout = findViewById(R.id.tabs);
        ivAddChannel = findViewById(R.id.iv_add_channel);
        mViewPager = findViewById(R.id.viewPager);
        mFab = findViewById(R.id.fab);
    }

    @Override
    public void bindListener() {
        ivAddChannel.setOnClickListener(mOnClickListener);
        mFab.setOnClickListener(mOnClickListener);
    }

    @Override
    public void execBusiness() {
        mPresenter.requestMineNewsChannels();
    }

    private void createNewsChannels(List<NewsChannelTable> newsChannelsMine) {
        if(newsChannelsMine != null) {
            List<String> channelNames = new ArrayList<>();
            List<Fragment> mNewsFragmentList = new ArrayList<>();
            for (int i = 0; i < newsChannelsMine.size(); i++) {
                channelNames.add(newsChannelsMine.get(i).getNewsChannelName());
                mNewsFragmentList.add(createNewsFrament(newsChannelsMine.get(i)));
            }
            if(mBaseFragmentAdapter == null) {
                mBaseFragmentAdapter = new BaseFragmentAdapter(
                        getChildFragmentManager(),
                        mNewsFragmentList,
                        channelNames
                );
            } else {
                mBaseFragmentAdapter.setFragments(
                        getChildFragmentManager(),
                        mNewsFragmentList,channelNames
                );
            }
            mViewPager.setAdapter(mBaseFragmentAdapter);
            mTabLayout.setupWithViewPager(mViewPager);
        }
    }

    private NewsListFrament createNewsFrament(NewsChannelTable newsChannel) {
        NewsListFrament fragment = new NewsListFrament();
        Bundle bundle = new Bundle();
        bundle.putString(NeteaseConstants.NEWS_ID, newsChannel.getNewsChannelId());
        bundle.putString(NeteaseConstants.NEWS_TYPE, newsChannel.getNewsChannelType());
        bundle.putInt(NeteaseConstants.CHANNEL_POSITION, newsChannel.getNewsChannelIndex());
        fragment.setArguments(bundle);
        return fragment;
    }

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.fab) {
            } else if (v.getId() == R.id.iv_add_channel) {
                ARouterUtils.navigation(getActivity(), RouterHub.NETEASE_NEWSCHANNELACTIVITY);
            }
        }
    };

    @Override
    public void setMineNewsChannels(List<NewsChannelTable> newsChannelsMine) {
        createNewsChannels(newsChannelsMine);
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }
}
