package com.smarthane.admiral.core.base.module;

import android.app.Application;

import androidx.fragment.app.FragmentManager;

import com.smarthane.admiral.core.integration.ActivityLifecycle;
import com.smarthane.admiral.core.integration.FragmentLifecycle;
import com.smarthane.admiral.core.integration.lifecycle.ActivityLifecycleForRxLifecycle;

/**
 * @author smarthane
 * @time 2019/10/20 16:51
 * @describe 提供一些框架必须的实例
 */
public class AppModule {

    /**
     * com.smarthane.admiral.core.integration.ActivityLifecycle
     */
    private Application.ActivityLifecycleCallbacks mActivityLifecycle;
    /**
     * com.smarthane.admiral.core.integration.lifecycle.ActivityLifecycleForRxLifecycle
     */
    private Application.ActivityLifecycleCallbacks mActivityLifecycleForRxLifecycle;
    /**
     * com.smarthane.admiral.core.integration.FragmentLifecycle
     */
    private FragmentManager.FragmentLifecycleCallbacks mFragmentLifecycle;

    private AppModule() {
    }

    private static class Holder {
        private static AppModule sInstance = new AppModule();
    }

    public static AppModule get() {
        return Holder.sInstance;
    }

    public Application.ActivityLifecycleCallbacks fetchActivityLifecycle() {
        if (mActivityLifecycle == null) {
            mActivityLifecycle = new ActivityLifecycle();
        }
        return mActivityLifecycle;
    }

    public Application.ActivityLifecycleCallbacks fetchActivityLifecycleForRxLifecycle() {
        if (mActivityLifecycleForRxLifecycle == null) {
            mActivityLifecycleForRxLifecycle = new ActivityLifecycleForRxLifecycle();
        }
        return mActivityLifecycleForRxLifecycle;
    }

    public FragmentManager.FragmentLifecycleCallbacks fetchFragmentLifecycle() {
        if (mFragmentLifecycle == null) {
            mFragmentLifecycle = new FragmentLifecycle();
        }
        return mFragmentLifecycle;
    }
}
