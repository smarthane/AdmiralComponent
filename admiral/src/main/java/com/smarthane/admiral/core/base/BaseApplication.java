package com.smarthane.admiral.core.base;

import android.app.Application;
import android.content.Context;

import com.smarthane.admiral.core.base.delegate.AppDelegate;
import com.smarthane.admiral.core.base.delegate.AppLifecycles;

/**
 * @author smarthane
 * @time 2019/10/13 16:06
 * @describe
 */
public class BaseApplication extends Application {

    private AppLifecycles mAppDelegate;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        if (mAppDelegate == null) {
            this.mAppDelegate = new AppDelegate(base);
        }
        this.mAppDelegate.attachBaseContext(base);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (mAppDelegate != null) {
            this.mAppDelegate.onCreate(this);
        }
    }

    /**
     * 在模拟环境中程序终止时会被调用
     */
    @Override
    public void onTerminate() {
        super.onTerminate();
        if (mAppDelegate != null) {
            this.mAppDelegate.onTerminate(this);
        }
    }
}
