package com.smarthane.admiralcomponent.netease.mvp.ui.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentTransaction;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.smarthane.admiral.component.common.sdk.core.RouterHub;
import com.smarthane.admiral.core.base.BaseActivity;
import com.smarthane.admiral.core.util.LogUtils;
import com.smarthane.admiralcomponent.netease.R;
import com.smarthane.admiralcomponent.netease.app.NeteaseConstants;
import com.smarthane.admiralcomponent.netease.mvp.model.entity.HomeTabEntity;
import com.smarthane.admiralcomponent.netease.mvp.ui.fragment.home.CareMainFragment;
import com.smarthane.admiralcomponent.netease.mvp.ui.fragment.home.NewsMainFragment;
import com.smarthane.admiralcomponent.netease.mvp.ui.fragment.home.PhotosMainFragment;
import com.smarthane.admiralcomponent.netease.mvp.ui.fragment.home.VideoMainFragment;

import java.util.ArrayList;

/**
 * @author smarthane
 * @time 2019/11/10 14:17
 * @describe 首页
 */
@Route(path = RouterHub.NETEASE_HOMEACTIVITY)
public class NeteaseHomeActivity extends BaseActivity {

    private String[] mTitles = {
            "首页",
            "图片",
            "视频",
            "关注"
    };

    private int[] mIconUnselectIds = {
            R.drawable.netease_ic_home_normal,
            R.drawable.netease_ic_girl_normal,
            R.drawable.netease_ic_video_normal,
            R.drawable.netease_ic_care_normal
    };

    private int[] mIconSelectIds = {
            R.drawable.netease_ic_home_selected,
            R.drawable.netease_ic_girl_selected,
            R.drawable.netease_ic_video_selected,
            R.drawable.netease_ic_care_selected
    };

    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();

    private CommonTabLayout mTabLayout;

    private NewsMainFragment mNewsMainFragment;
    private PhotosMainFragment mPhotosMainFragment;
    private VideoMainFragment mVideoMainFragment;
    private CareMainFragment mCareMainFragment;

    @Override
    public int setLayoutResouceId() {
        return R.layout.netease_activity_home;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        mTabLayout = findViewById(R.id.tab_layout);
        initTab();
        initComponent(savedInstanceState);
    }

    /**
     * 初始化tab
     */
    private void initTab() {
        for (int i = 0; i < mTitles.length; i++) {
            mTabEntities.add(new HomeTabEntity(
                    mTitles[i],
                    mIconSelectIds[i],
                    mIconUnselectIds[i]
            ));
        }
        mTabLayout.setTabData(mTabEntities);
        // 点击监听
        mTabLayout.setOnTabSelectListener(new OnTabSelectListener() {

            @Override
            public void onTabSelect(int position) {
                navigationComponent(position);
            }

            @Override
            public void onTabReselect(int position) {
            }
        });
    }

    /**
     * 初始化Fragment
     */
    private void initComponent(Bundle savedInstanceState) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        int currentTabPosition = 0;
        if (savedInstanceState != null) {
            mNewsMainFragment = (NewsMainFragment) getSupportFragmentManager()
                    .findFragmentByTag(NeteaseConstants.HOME_FRAGMENT_TAG_NEWS);
            mPhotosMainFragment = (PhotosMainFragment) getSupportFragmentManager()
                    .findFragmentByTag(NeteaseConstants.HOME_FRAGMENT_TAG_PHOTOS);
            mVideoMainFragment = (VideoMainFragment) getSupportFragmentManager()
                    .findFragmentByTag(NeteaseConstants.HOME_FRAGMENT_TAG_VIDEO);
            mCareMainFragment = (CareMainFragment) getSupportFragmentManager()
                    .findFragmentByTag(NeteaseConstants.HOME_FRAGMENT_TAG_CARE);
            currentTabPosition = savedInstanceState.getInt(NeteaseConstants.HOME_CURRENT_TAB_POSITION);
        } else {
            mNewsMainFragment = new NewsMainFragment();
            mPhotosMainFragment = new PhotosMainFragment();
            mVideoMainFragment = new VideoMainFragment();
            mCareMainFragment = new CareMainFragment();
            transaction.add(
                    R.id.fl_body,
                    mNewsMainFragment,
                    NeteaseConstants.HOME_FRAGMENT_TAG_NEWS
            );
            transaction.add(
                    R.id.fl_body,
                    mPhotosMainFragment,
                    NeteaseConstants.HOME_FRAGMENT_TAG_PHOTOS
            );
            transaction.add(
                    R.id.fl_body,
                    mVideoMainFragment,
                    NeteaseConstants.HOME_FRAGMENT_TAG_VIDEO
            );
            transaction.add(
                    R.id.fl_body,
                    mCareMainFragment,
                    NeteaseConstants.HOME_FRAGMENT_TAG_CARE
            );
        }
        transaction.commit();
        navigationComponent(currentTabPosition);
        mTabLayout.setCurrentTab(currentTabPosition);
    }


    /**
     * 切换
     */
    private void navigationComponent(int position) {
        LogUtils.debugInfo("主页菜单position: " + position);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        switch (position) {
            // 首页
            case 0:
                transaction.hide(mPhotosMainFragment);
                transaction.hide(mVideoMainFragment);
                transaction.hide(mCareMainFragment);
                transaction.show(mNewsMainFragment);
                transaction.commitAllowingStateLoss();
                break;
            // 图片
            case 1:
                transaction.hide(mNewsMainFragment);
                transaction.hide(mVideoMainFragment);
                transaction.hide(mCareMainFragment);
                transaction.show(mPhotosMainFragment);
                transaction.commitAllowingStateLoss();
                break;
            // 视频
            case 2:
                transaction.hide(mNewsMainFragment);
                transaction.hide(mPhotosMainFragment);
                transaction.hide(mCareMainFragment);
                transaction.show(mVideoMainFragment);
                transaction.commitAllowingStateLoss();
                break;
            // 关注
            case 3:
                transaction.hide(mNewsMainFragment);
                transaction.hide(mPhotosMainFragment);
                transaction.hide(mVideoMainFragment);
                transaction.show(mCareMainFragment);
                transaction.commitAllowingStateLoss();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // 奔溃前保存TAB位置
        LogUtils.debugInfo("NeteaseHomeActivity onSaveInstanceState 1");
        if (mTabLayout != null) {
            LogUtils.debugInfo("NeteaseHomeActivity onSaveInstanceState 2");
            outState.putInt(NeteaseConstants.HOME_CURRENT_TAB_POSITION, mTabLayout.getCurrentTab());
        }
    }

}
