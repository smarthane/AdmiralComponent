package com.smarthane.admiral.component.mvp.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.smarthane.admiral.component.common.sdk.core.RouterHub;
import com.smarthane.admiral.component.common.sdk.util.ARouterUtils;
import com.smarthane.admiral.component.common.service.gank.service.GankInfoService;
import com.smarthane.admiral.component.common.service.gold.service.GoldInfoService;
import com.smarthane.admiral.component.common.service.netease.service.NeteaseInfoService;
import com.smarthane.admiral.component.common.service.zhihu.service.ZhihuInfoService;
import com.smarthane.admiral.core.base.BaseActivity;
import com.smarthane.admiral.core.util.AdmiralUtils;
import com.smarthane.admiralcomponent.R;

/**
 * @author smarthane
 * @time 2019/11/10 17:22
 * @describe
 */
@Route(path = RouterHub.APP_MAINACTIVITY)
public class MainActivity extends BaseActivity {

    private ZhihuInfoService mZhihuInfoService;
    private NeteaseInfoService mNeteaseInfoService;

    @Autowired(name = RouterHub.GANK_SERVICE_GANKINFOSERVICE)
    GankInfoService mGankInfoService;
    //@Autowired(name = RouterHub.GOLD_SERVICE_GOLDINFOSERVICE)
    //GoldInfoService mGoldInfoService;

    private Button btnZhihu;
    private Button btnGank;
    private Button btnGold;
    private Button btnNetease;

    private long mPressedTime;

    @Override
    public int setLayoutResouceId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initData() {
        ARouter.getInstance().inject(this);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        btnZhihu = findViewById(R.id.bt_zhihu);
        btnGank = findViewById(R.id.bt_gank);
        btnGold = findViewById(R.id.bt_gold);
        btnNetease = findViewById(R.id.bt_netease);
    }

    @Override
    protected void bindListener() {
        btnZhihu.setOnClickListener(mOnClickListener);
        btnGank.setOnClickListener(mOnClickListener);
        btnGold.setOnClickListener(mOnClickListener);
        btnNetease.setOnClickListener(mOnClickListener);
    }

    @Override
    protected void execBusiness() {
        loadZhihuInfo();
        loadGankInfo();
        //loadGoldInfo();
        loadNeteaseInfo();
    }

    @Override
    public void onBackPressed() {
        //获取第一次按键时间
        long mNowTime = System.currentTimeMillis();
        //比较两次按键时间差
        if ((mNowTime - mPressedTime) > 2000) {
            AdmiralUtils.makeText(getApplicationContext(),
                    "再按一次退出:" + AdmiralUtils.getString(getApplicationContext(),
                            R.string.app_name));
            mPressedTime = mNowTime;
        } else {
            super.onBackPressed();
        }
    }

    private void loadZhihuInfo() {
        mZhihuInfoService = ARouter.getInstance().navigation(ZhihuInfoService.class);
        // 当非集成调试阶段, 宿主 App 由于没有依赖其他组件, 所以使用不了对应组件提供的服务
        if (mZhihuInfoService == null) {
            btnZhihu.setVisibility(View.GONE);
            return;
        }
        btnZhihu.setText(mZhihuInfoService.getInfo().getName());
    }

    private void loadGankInfo() {
        // 当非集成调试阶段, 宿主 App 由于没有依赖其他组件, 所以使用不了对应组件提供的服务
        if (mGankInfoService == null) {
            btnGank.setVisibility(View.GONE);
            return;
        }
        btnGank.setText(mGankInfoService.getInfo().getName());
    }

//    private void loadGoldInfo() {
//        //当非集成调试阶段, 宿主 App 由于没有依赖其他组件, 所以使用不了对应组件提供的服务
//        if (mGoldInfoService == null) {
//            btnGold.setVisibility(View.GONE);
//            return;
//        }
//        btnGold.setText(mGoldInfoService.getInfo().getName());
//    }

    private void loadNeteaseInfo() {
        mNeteaseInfoService = ARouter.getInstance().navigation(NeteaseInfoService.class);
        //当非集成调试阶段, 宿主 App 由于没有依赖其他组件, 所以使用不了对应组件提供的服务
        if (mNeteaseInfoService == null) {
            btnNetease.setVisibility(View.GONE);
            return;
        }
        btnNetease.setText(mNeteaseInfoService.getInfo().getName());
    }

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.bt_zhihu) {
                ARouterUtils.navigation(MainActivity.this, RouterHub.ZHIHU_HOMEACTIVITY);
            } else if (v.getId() == R.id.bt_gank) {
                ARouterUtils.navigation(MainActivity.this, RouterHub.GANK_HOMEACTIVITY2);
            } else if (v.getId() == R.id.bt_gold) {
                ARouterUtils.navigation(MainActivity.this, RouterHub.GANK_HOMEACTIVITY);
            } else if (v.getId() == R.id.bt_netease) {
                ARouterUtils.navigation(MainActivity.this, RouterHub.NETEASE_HOMEACTIVITY);
            }
        }
    };
}
