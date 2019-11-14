package com.smarthane.admiral.demo.app;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;

import com.smarthane.admiral.core.base.delegate.AppLifecycles;
import com.smarthane.admiral.core.util.LogUtils;

/**
 * @author smarthane
 * @time 2019/10/20 13:51
 * @describe
 */
public class AppLifecyclesImpl implements AppLifecycles {

    @Override
    public void attachBaseContext(@NonNull Context base) {
        LogUtils.debugInfo("AppLifecyclesImpl attachBaseContext");
    }

    @Override
    public void onCreate(@NonNull Application application) {
        LogUtils.debugInfo("AppLifecyclesImpl onCreate");
    }

    @Override
    public void onTerminate(@NonNull Application application) {
        LogUtils.debugInfo("AppLifecyclesImpl onTerminate");
    }
}
