package com.smarthane.admiralcomponent.wanandroid.app;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;

import com.idlefish.flutterboost.NewFlutterBoost;
import com.idlefish.flutterboost.Platform;
import com.idlefish.flutterboost.Utils;
import com.idlefish.flutterboost.interfaces.INativeRouter;
import com.smarthane.admiral.core.base.AppComponent;
import com.smarthane.admiral.core.base.delegate.AppLifecycles;
import com.smarthane.admiral.core.http.retrofiturlmanager.RetrofitUrlManager;
import com.smarthane.admiral.core.integration.cache.IntelligentCache;
import com.smarthane.admiral.core.util.LogUtils;
import com.smarthane.admiralcomponent.wanandroid.BuildConfig;
import com.smarthane.admiralcomponent.wanandroid.mvp.model.api.Api;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;
import com.taobao.idlefish.flutterboostexample.PageRouter;

import java.util.Map;

import io.flutter.embedding.android.FlutterView;

/**
 * @author smarthane
 * @time 2019/11/10 19:50
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
        RetrofitUrlManager.getInstance().putDomain(Api.WANANDROID_DOMAIN_NAME, Api.WANANDROID_DOMAIN);
        // 当所有模块集成到宿主 App 时, 在 App 中已经执行了以下代码
        if (BuildConfig.IS_BUILD_MODULE) {
            //leakCanary内存泄露检查
            AppComponent.get().fetchExtras()
                    .put(IntelligentCache.getKeyOfKeep(RefWatcher.class.getName())
                            , BuildConfig.USE_CANARY ? LeakCanary.install(application) : RefWatcher.DISABLED);
        }

        // 初始化FlutterBoost
        INativeRouter router = new INativeRouter() {
            @Override
            public void openContainer(Context context, String url, Map<String, Object> urlParams, int requestCode, Map<String, Object> exts) {
                String assembleUrl = Utils.assembleUrl(url, urlParams);
                LogUtils.debugInfo("openContainer----->" + assembleUrl);
                PageRouter.openPageByUrl(context, assembleUrl, urlParams);
            }
        };
        Platform platform = new NewFlutterBoost
                .ConfigBuilder(application, router)
                .isDebug(BuildConfig.DEBUG)
                .whenEngineStart(NewFlutterBoost.ConfigBuilder.ANY_ACTIVITY_CREATED)
                .renderMode(FlutterView.RenderMode.texture)
                .build();
        NewFlutterBoost.instance().init(platform);
    }

    @Override
    public void onTerminate(@NonNull Application application) {

    }
}