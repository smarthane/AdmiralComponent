package com.smarthane.admiral.demo.app;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.smarthane.admiral.core.util.LogUtils;

/**
 * @author smarthane
 * @time 2019/10/20 13:52
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
    }
}
