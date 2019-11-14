package com.smarthane.admiralcomponent.zhihu.app;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.smarthane.admiral.core.base.AppComponent;
import com.smarthane.admiral.core.base.delegate.AppLifecycles;
import com.smarthane.admiral.core.base.module.GlobalConfigModule;
import com.smarthane.admiral.core.integration.ConfigModule;
import com.smarthane.admiral.core.integration.cache.IntelligentCache;
import com.smarthane.admiralcomponent.zhihu.BuildConfig;
import com.squareup.leakcanary.RefWatcher;

import java.util.List;

/**
 * @author smarthane
 * @time 2019/10/27 9:48
 * @describe
 */
public class GlobalConfiguration implements ConfigModule {

    @Override
    public void applyOptions(@NonNull Context context, @NonNull GlobalConfigModule.Builder builder) {

    }

    @Override
    public void injectAppLifecycle(@NonNull Context context, @NonNull List<AppLifecycles> lifecycles) {
        // AppLifecycles 的所有方法都会在基类 Application 的对应的生命周期中被调用,所以在对应的方法中可以扩展一些自己需要的逻辑
        // 可以根据不同的逻辑添加多个实现类
        lifecycles.add(new AppLifecyclesImpl());
    }

    @Override
    public void injectActivityLifecycle(@NonNull Context context, @NonNull List<Application.ActivityLifecycleCallbacks> lifecycles) {

    }

    @Override
    public void injectFragmentLifecycle(@NonNull Context context, @NonNull List<FragmentManager.FragmentLifecycleCallbacks> lifecycles) {
        // 当所有模块集成到宿主 App 时, 在 App 中已经执行了以下代码, 所以不需要再执行
        if (BuildConfig.IS_BUILD_MODULE) {
            lifecycles.add(new FragmentManager.FragmentLifecycleCallbacks() {
                @Override
                public void onFragmentDestroyed(FragmentManager fm, Fragment f) {
                    ((RefWatcher) AppComponent.get().fetchExtras()
                            .get(IntelligentCache.getKeyOfKeep(RefWatcher.class.getName())))
                            .watch(f);
                }
            });
        }
    }
}
