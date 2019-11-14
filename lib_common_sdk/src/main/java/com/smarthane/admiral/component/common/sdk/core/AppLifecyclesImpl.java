package com.smarthane.admiral.component.common.sdk.core;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;

import com.alibaba.android.arouter.launcher.ARouter;
import com.smarthane.admiral.core.BuildConfig;
import com.smarthane.admiral.core.base.delegate.AppLifecycles;
import com.smarthane.admiral.core.http.retrofiturlmanager.RetrofitUrlManager;
import com.smarthane.admiral.core.util.LogUtils;

/**
 * @author smarthane
 * @time 2019/11/10 14:39
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
        if (BuildConfig.DEBUG) {
            // 打印日志
            ARouter.openLog();
            // 开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
            ARouter.openDebug();
            RetrofitUrlManager.getInstance().setDebug(true);
        }
        // 尽可能早,推荐在Application中初始化
        ARouter.init(application);
    }

    @Override
    public void onTerminate(@NonNull Application application) {
        LogUtils.debugInfo("AppLifecyclesImpl onTerminate");
    }
}
