package com.smarthane.admiral.component.common.sdk.tinker;

import android.annotation.TargetApi;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.multidex.MultiDex;

import com.smarthane.admiral.core.base.delegate.AppDelegate;
import com.smarthane.admiral.core.base.delegate.AppLifecycles;
import com.smarthane.admiral.core.util.LogUtils;
import com.tencent.tinker.entry.DefaultApplicationLike;

/**
 * @author smarthane
 * @time 2019/11/10 19:29
 * @describe /**
 * because you can not use any other class in your application, we need to
 * move your implement of Application to {link ApplicationLifeCycle}
 * As Application, all its direct reference class should be in the main dex.
 *
 * We use tinker-android-anno to make sure all your classes can be patched.
 *
 * application: if it is start with '.', we will add SampleApplicationLifeCycle's package name
 *
 * flags:
 * TINKER_ENABLE_ALL: support dex, lib and resource
 * TINKER_DEX_MASK: just support dex
 * TINKER_NATIVE_LIBRARY_MASK: just support lib
 * TINKER_RESOURCE_MASK: just support resource
 *
 * loaderClass: define the tinker loader class, we can just use the default TinkerLoader
 *
 * loadVerifyFlag: whether check files' md5 on the load time, defualt it is false.
 */
public class TinkerApplicationLike extends DefaultApplicationLike {

    private static final String TAG = "Tinker.TinkerApplicationLike";
    private AppLifecycles mAppDelegate;

    public TinkerApplicationLike(Application application, int tinkerFlags, boolean tinkerLoadVerifyFlag, long applicationStartElapsedTime, long applicationStartMillisTime, Intent tinkerResultIntent) {
        super(application, tinkerFlags, tinkerLoadVerifyFlag, applicationStartElapsedTime, applicationStartMillisTime, tinkerResultIntent);
        LogUtils.debugInfo(TAG, "TinkerApplicationLike: created");
    }

    @Override
    public void onCreate() {
        // Admiral 框架必须先初始化
        if (mAppDelegate != null) {
            this.mAppDelegate.onCreate(this.getApplication());
        }
        super.onCreate();
    }

    /**
     * install multiDex before install tinker
     * so we don't need to put the tinker lib classes in the main dex
     *
     * @param base
     */
    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @Override
    public void onBaseContextAttached(Context base) {
        // Admiral 框架必须先初始化
        if (mAppDelegate == null) {
            this.mAppDelegate = new AppDelegate(base);
        }
        this.mAppDelegate.attachBaseContext(base);
        super.onBaseContextAttached(base);
        LogUtils.debugInfo(TAG, "TinkerApplicationLike: onBaseContextAttached");
        // you must install multiDex whatever tinker is installed!
        MultiDex.install(base);
        TinkerManager.setTinkerApplicationLike(this);
        // should set before tinker is installed
        TinkerManager.setUpgradeRetryEnable(true);
        // installTinker after load multiDex
        // or you can put com.tencent.tinker.** to main dex
        TinkerManager.installTinker(this);
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public void registerActivityLifecycleCallbacks(Application.ActivityLifecycleCallbacks callback) {
        LogUtils.debugInfo(TAG, "TinkerApplicationLike: registerActivityLifecycleCallbacks");
        getApplication().registerActivityLifecycleCallbacks(callback);
    }

    /**
     * 在模拟环境中程序终止时会被调用
     */
    @Override
    public void onTerminate() {
        // Admiral 框架必须先初始化
        if (mAppDelegate != null) {
            this.mAppDelegate.onTerminate(this.getApplication());
        }
        super.onTerminate();
    }
}
