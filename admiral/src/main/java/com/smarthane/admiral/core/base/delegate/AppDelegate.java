package com.smarthane.admiral.core.base.delegate;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;

import com.smarthane.admiral.core.base.AppComponent;
import com.smarthane.admiral.core.base.module.GlobalConfigModule;
import com.smarthane.admiral.core.integration.ConfigModule;
import com.smarthane.admiral.core.integration.ManifestParser;
import com.smarthane.admiral.core.integration.cache.IntelligentCache;

import java.util.ArrayList;
import java.util.List;

/**
 * @author smarthane
 * @time 2019/10/13 16:16
 * @describe  AppDelegate 可以代理 Application 的生命周期,在对应的生命周期,执行对应的逻辑,因为 Java 只能单继承
 * 所以当遇到某些三方库需要继承于它的 Application 的时候,就只有自定义 Application 并继承于三方库的 Application
 * 这时就不用再继承 BaseApplication,只用在自定义Application中对应的生命周期调用AppDelegate对应的方法
 */
public class AppDelegate implements AppLifecycles {

    private Application mApplication;
    private AppComponent mAppComponent;
    private GlobalConfigModule mGlobalConfigModule;
    private Application.ActivityLifecycleCallbacks mActivityLifecycle;
    private Application.ActivityLifecycleCallbacks mActivityLifecycleForRxLifecycle;
    private List<ConfigModule> mModules;
    private List<AppLifecycles> mAppLifecycles = new ArrayList<>();
    private List<Application.ActivityLifecycleCallbacks> mActivityLifecycles = new ArrayList<>();

    public AppDelegate(@NonNull Context context) {
        // 用反射, 将 AndroidManifest.xml 中带有 ConfigModule 标签的 class 转成对象集合（List<ConfigModule>）
        this.mModules = new ManifestParser(context).parse();
        // 全局配置
        mGlobalConfigModule = getGlobalConfigModule(context, mModules);
        // 遍历之前获得的集合, 执行每一个 ConfigModule 实现类的某些方法
        if (mModules != null && !mModules.isEmpty()) {
            for (ConfigModule module : mModules) {
                // 将框架外部, 开发者实现的 Application 的生命周期回调 (AppLifecycles) 存入 mAppLifecycles 集合 (此时还未注册回调)
                module.injectAppLifecycle(context, mAppLifecycles);
                // 将框架外部, 开发者实现的 Activity 的生命周期回调 (ActivityLifecycleCallbacks) 存入 mActivityLifecycles 集合 (此时还未注册回调)
                module.injectActivityLifecycle(context, mActivityLifecycles);
            }
        }
    }

    @Override
    public void attachBaseContext(@NonNull Context base) {
        // 遍历 mAppLifecycles, 执行所有已注册的 AppLifecycles 的 attachBaseContext() 方法 (框架外部, 开发者扩展的逻辑)
        if (mActivityLifecycles != null && !mActivityLifecycles.isEmpty()) {
            for (AppLifecycles lifecycle : mAppLifecycles) {
                lifecycle.attachBaseContext(base);
            }
        }
    }

    @Override
    public void onCreate(@NonNull Application application) {
        mApplication = application;

        mAppComponent = AppComponent.get().init(application, mGlobalConfigModule);

        // 将 ConfigModule 的实现类的集合存放到缓存 Cache, 可以随时获取
        // 使用 IntelligentCache.KEY_KEEP 作为 key 的前缀, 可以使储存的数据永久存储在内存中
        // 否则存储在 LRU 算法的存储空间中 (大于或等于缓存所能允许的最大 size, 则会根据 LRU 算法清除之前的条目)
        // 前提是 extras 使用的是 IntelligentCache (框架默认使用)
        mAppComponent.fetchExtras().put(IntelligentCache.getKeyOfKeep(ConfigModule.class.getName()), mModules);

        mModules = null;

        // 注册框架内部已实现的 Activity 生命周期逻辑
        mActivityLifecycle = AppComponent.get().fetchAppModule().fetchActivityLifecycle();
        mApplication.registerActivityLifecycleCallbacks(mActivityLifecycle);

        // 注册框架内部已实现的 RxLifecycle 逻辑
        mActivityLifecycleForRxLifecycle = AppComponent.get().fetchAppModule().fetchActivityLifecycleForRxLifecycle();
        mApplication.registerActivityLifecycleCallbacks(mActivityLifecycleForRxLifecycle);

        // 注册框架外部, 开发者扩展的 Activity 生命周期逻辑
        // 每个 ConfigModule 的实现类可以声明多个 Activity 的生命周期回调
        // 也可以有 N 个 ConfigModule 的实现类 (完美支持组件化项目 各个 Module 的各种独特需求)
        if (mActivityLifecycles != null && !mActivityLifecycles.isEmpty()) {
            for (Application.ActivityLifecycleCallbacks lifecycle : mActivityLifecycles) {
                mApplication.registerActivityLifecycleCallbacks(lifecycle);
            }
        }

        // 执行框架外部, 开发者扩展的 App onCreate 逻辑
        if (mAppLifecycles != null && !mAppLifecycles.isEmpty()) {
            for (AppLifecycles lifecycle : mAppLifecycles) {
                lifecycle.onCreate(mApplication);
            }
        }
    }

    @Override
    public void onTerminate(@NonNull Application application) {
        if (mActivityLifecycle != null) {
            mApplication.unregisterActivityLifecycleCallbacks(mActivityLifecycle);
        }
        if (mActivityLifecycleForRxLifecycle != null) {
            mApplication.unregisterActivityLifecycleCallbacks(mActivityLifecycleForRxLifecycle);
        }
        if (mActivityLifecycles != null && !mActivityLifecycles.isEmpty()) {
            for (Application.ActivityLifecycleCallbacks lifecycle : mActivityLifecycles) {
                mApplication.unregisterActivityLifecycleCallbacks(lifecycle);
            }
        }
        if (mAppLifecycles != null && !mAppLifecycles.isEmpty()) {
            for (AppLifecycles lifecycle : mAppLifecycles) {
                lifecycle.onTerminate(mApplication);
            }
        }
        this.mAppComponent = null;
        this.mActivityLifecycle = null;
        this.mActivityLifecycleForRxLifecycle = null;
        this.mActivityLifecycles = null;
        this.mAppLifecycles = null;
        this.mApplication = null;
    }

    private GlobalConfigModule getGlobalConfigModule(Context context, List<ConfigModule> modules) {
        GlobalConfigModule.Builder builder = GlobalConfigModule.builder();
        builder.context(context);
        // 遍历 ConfigModule 集合, 给全局配置 GlobalConfigModule 添加参数
        if (modules != null && !modules.isEmpty()) {
            for (ConfigModule module : modules) {
                module.applyOptions(context, builder);
            }
        }
        return builder.build();
    }
}
