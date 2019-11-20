package com.smarthane.admiral.component.common.sdk.core;

import android.app.Activity;
import android.app.Application;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.smarthane.admiral.component.common.sdk.http.eapi.EasyApiHelper;
import com.smarthane.admiral.core.util.AdmiralUtils;
import com.smarthane.admiral.core.util.LogUtils;

/**
 * @author smarthane
 * @time 2019/11/10 14:16
 * @describe
 */
public class ActivityLifecycleCallbacksImpl implements Application.ActivityLifecycleCallbacks {

    @Override
    public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {
        LogUtils.debugInfo("ActivityLifecycleCallbacksImpl onActivityCreated");
    }

    @Override
    public void onActivityStarted(@NonNull Activity activity) {
        LogUtils.debugInfo("ActivityLifecycleCallbacksImpl onActivityStarted");
        if (!activity.getIntent().getBooleanExtra("isInitToolbar", false)) {
            // 由于加强框架的兼容性,故将 setContentView 放到 onActivityCreated 之后,onActivityStarted 之前执行
            // 而 findViewById 必须在 Activity setContentView() 后才有效,所以将以下代码从之前的 onActivityCreated 中移动到 onActivityStarted 中执行
            activity.getIntent().putExtra("isInitToolbar", true);
            // 这里全局给Activity设置toolbar和title,你想象力有多丰富,这里就有多强大,以前放到BaseActivity的操作都可以放到这里
            if (AdmiralUtils.findViewByName(activity.getApplicationContext(), activity, "public_toolbar") != null) {
                if (activity instanceof AppCompatActivity) {
                    ((AppCompatActivity) activity).setSupportActionBar(AdmiralUtils.findViewByName(activity.getApplicationContext(), activity, "public_toolbar"));
                    ((AppCompatActivity) activity).getSupportActionBar().setDisplayShowTitleEnabled(false);
                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        activity.setActionBar(AdmiralUtils.findViewByName(activity.getApplicationContext(), activity, "public_toolbar"));
                        activity.getActionBar().setDisplayShowTitleEnabled(false);
                    }
                }
            }
            if (AdmiralUtils.findViewByName(activity.getApplicationContext(), activity, "public_toolbar_title") != null) {
                ((TextView) AdmiralUtils.findViewByName(activity.getApplicationContext(), activity, "public_toolbar_title")).setText(activity.getTitle());
            }
            if (AdmiralUtils.findViewByName(activity.getApplicationContext(), activity, "public_toolbar_back") != null) {
                AdmiralUtils.findViewByName(activity.getApplicationContext(), activity, "public_toolbar_back").setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        activity.onBackPressed();
                    }
                });
            }
        }
    }

    @Override
    public void onActivityResumed(@NonNull Activity activity) {
        LogUtils.debugInfo("ActivityLifecycleCallbacksImpl onActivityResumed");
    }

    @Override
    public void onActivityPaused(@NonNull Activity activity) {
        LogUtils.debugInfo("ActivityLifecycleCallbacksImpl onActivityPaused");
    }

    @Override
    public void onActivityStopped(@NonNull Activity activity) {
        LogUtils.debugInfo("ActivityLifecycleCallbacksImpl onActivityStopped");
    }

    @Override
    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {
        LogUtils.debugInfo("ActivityLifecycleCallbacksImpl onActivitySaveInstanceState");
    }

    @Override
    public void onActivityDestroyed(@NonNull Activity activity) {
        LogUtils.debugInfo("ActivityLifecycleCallbacksImpl onActivityDestroyed");
        EasyApiHelper.cancel(activity);
    }
}
