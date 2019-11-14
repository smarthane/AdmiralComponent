package com.smarthane.admiralcomponent.gank.app;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;

import com.smarthane.admiral.core.base.AppComponent;
import com.smarthane.admiral.core.base.delegate.AppLifecycles;
import com.smarthane.admiral.core.integration.cache.IntelligentCache;
import com.smarthane.admiralcomponent.gank.BuildConfig;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

import com.smarthane.admiral.core.http.retrofiturlmanager.RetrofitUrlManager;

import static com.smarthane.admiralcomponent.gank.mvp.model.api.Api.GANK_DOMAIN;
import static com.smarthane.admiralcomponent.gank.mvp.model.api.Api.GANK_DOMAIN_NAME;

/**
 * @author smarthane
 * @time 2019/10/27 9:45
 * @describe
 */
public class AppLifecyclesImpl implements AppLifecycles {

    @Override
    public void attachBaseContext(@NonNull Context base) {

    }

    @Override
    public void onCreate(@NonNull Application application) {
        if (LeakCanary.isInAnalyzerProcess(application)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        // 使用 RetrofitUrlManager 切换 BaseUrl
        RetrofitUrlManager.getInstance().putDomain(GANK_DOMAIN_NAME, GANK_DOMAIN);
        //当所有模块集成到宿主 App 时, 在 App 中已经执行了以下代码
        if (BuildConfig.IS_BUILD_MODULE) {
            //leakCanary内存泄露检查
            AppComponent.get().fetchExtras()
                    .put(IntelligentCache.getKeyOfKeep(RefWatcher.class.getName())
                            , BuildConfig.USE_CANARY ? LeakCanary.install(application) : RefWatcher.DISABLED);
        }
    }

    @Override
    public void onTerminate(@NonNull Application application) {

    }
}
