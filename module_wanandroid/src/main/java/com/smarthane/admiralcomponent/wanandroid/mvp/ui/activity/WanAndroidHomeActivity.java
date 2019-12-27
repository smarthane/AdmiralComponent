package com.smarthane.admiralcomponent.wanandroid.mvp.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.smarthane.admiral.component.common.sdk.core.RouterHub;
import com.smarthane.admiral.core.base.BaseActivity;
import com.smarthane.admiralcomponent.wanandroid.R;
import com.taobao.idlefish.flutterboostexample.MainActivity;
import com.taobao.idlefish.flutterboostexample.PageRouter;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

/**
 * @author smarthane
 * @time 2019/11/10 19:22
 * @describe
 */
@Route(path = RouterHub.WANANDROID_HOMEACTIVITY)
public class WanAndroidHomeActivity extends BaseActivity implements View.OnClickListener {

    public static WeakReference<MainActivity> sRef;

    private TextView mOpenNative;
    private TextView mOpenFlutter;
    private TextView mOpenFlutterFragment;

    @Override
    public int setLayoutResouceId() {
        return R.layout.wanandroid_native_page;
    }

    @Override
    protected void initData() {
        super.initData();
        sRef = new WeakReference(this);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        mOpenNative = findViewById(R.id.open_native);
        mOpenFlutter = findViewById(R.id.open_flutter);
        mOpenFlutterFragment = findViewById(R.id.open_flutter_fragment);
    }

    @Override
    protected void bindListener() {
        super.bindListener();
        mOpenNative.setOnClickListener(this);
        mOpenFlutter.setOnClickListener(this);
        mOpenFlutterFragment.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Map params = new HashMap();
        params.put("test1", "v_test1");
        params.put("test2", "v_test2");
        //Add some params if needed.
        if (v == mOpenNative) {
            PageRouter.openPageByUrl(this, PageRouter.NATIVE_PAGE_URL, params);
        } else if (v == mOpenFlutter) {
            PageRouter.openPageByUrl(this, PageRouter.FLUTTER_PAGE_URL, params);
        } else if (v == mOpenFlutterFragment) {
            PageRouter.openPageByUrl(this, PageRouter.FLUTTER_FRAGMENT_PAGE_URL, params);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sRef.clear();
        sRef = null;
    }
}
