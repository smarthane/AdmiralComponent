package com.smarthane.admiral.core.base;

import android.app.Application;

import com.google.gson.Gson;
import com.smarthane.admiral.core.base.module.AppModule;
import com.smarthane.admiral.core.base.module.ClientModule;
import com.smarthane.admiral.core.base.module.GlobalConfigModule;
import com.smarthane.admiral.core.http.imageloader.ImageLoader;
import com.smarthane.admiral.core.integration.AppManager;
import com.smarthane.admiral.core.integration.IRepositoryManager;
import com.smarthane.admiral.core.integration.RepositoryManager;
import com.smarthane.admiral.core.integration.cache.Cache;
import com.smarthane.admiral.core.integration.cache.CacheType;

import java.io.File;
import java.util.concurrent.ExecutorService;

import com.smarthane.admiral.core.base.rx.errorhandler.RxErrorHandler;
import okhttp3.OkHttpClient;

/**
 * @author smarthane
 * @time 2019/10/13 16:45
 * @describe 应用组件集合
 */
public class AppComponent {

    private Application mApplication;

    private static volatile AppComponent appComponent;

    private GlobalConfigModule mGlobalConfigModule;
    private Cache<String, Object> mCacheExtras;

    private AppComponent() {
    }

    public static AppComponent get() {
        if (appComponent == null) {
            synchronized (AppComponent.class) {
                if (appComponent == null) {
                    appComponent = new AppComponent();
                }
            }
        }
        return appComponent;
    }

    /**
     * 入口必须最新执行初始化
     * @param application
     * @param globalConfigModule
     * @return
     */
    public AppComponent init(Application application, GlobalConfigModule globalConfigModule) {
        this.mApplication = application;
        this.mGlobalConfigModule = globalConfigModule;
        return appComponent;
    }

    public Application fetchApplication() {
        return this.mApplication;
    }

    /**
     * 用于管理所有 {Activity}
     * @return
     */
    public AppManager fetchAppManager() {
        return AppManager.getAppManager().init(this.mApplication);
    }

    /**
     * GlobalConfigModule
     * @return
     */
    public GlobalConfigModule fetchGlobalConfigModule() {
        return this.mGlobalConfigModule;
    }

    /**
     * ClientModule
     * @return
     */
    public ClientModule fetchClientModule() {
        return ClientModule.get();
    }

    /**
     * AppModule
     * @return
     */
    public AppModule fetchAppModule() {
        return AppModule.get();
    }

    /**
     * 用于管理网络请求层, 以及数据缓存层
     * @return
     */
    public IRepositoryManager fetchRepositoryManager() {
        return RepositoryManager.get();
    }

    /**
     * RxJava 错误处理管理类
     * @return
     */
    public RxErrorHandler fetchRxErrorHandler() {
        return fetchClientModule().provideRxErrorHandler();
    }

    /**
     * 图片加载管理器, 用于加载图片的管理类, 使用策略者模式, 可在运行时动态替换任何图片加载框架
     * @return
     */
    public ImageLoader fetchImageLoader() {
        return ImageLoader.get();
    }

    /**
     * 网络请求框架
     * @return
     */
    public OkHttpClient fetchOkHttpClient() {
        return fetchClientModule().provideOkHttpClient();
    }

    /**
     * Json 序列化库
     * @return
     */
    public Gson fetchGson() {
        return mGlobalConfigModule.provideGson();
    }

    /**
     * 缓存文件根目录 (RxCache 和 Glide 的缓存都已经作为子文件夹放在这个根目录下), 应该将所有缓存都统一放到这个根目录下
     * 便于管理和清理, 可在 { ConfigModule#applyOptions(Context, GlobalConfigModule.Builder)} 种配置
     *
     * @return {@link File}
     */
    public File fetchCacheFile() {
        return mGlobalConfigModule.provideCacheFile();
    }

    /**
     * 用来存取一些整个 App 公用的数据, 切勿大量存放大容量数据, 这里的存放的数据和 {@link Application} 的生命周期一致
     *
     * @return {@link Cache}
     */
    public Cache<String, Object> fetchExtras() {
        if (mCacheExtras == null) {
            mCacheExtras = fetchCacheFactory().build(CacheType.EXTRAS);
        }
        return mCacheExtras;
    }

    /**
     * 用于创建框架所需缓存对象的工厂
     *
     * @return {@link Cache.Factory}
     */
    public Cache.Factory fetchCacheFactory() {
        return mGlobalConfigModule.provideCacheFactory();
    }

    /**
     * 返回一个全局公用的线程池,适用于大多数异步需求。
     * 避免多个线程池创建带来的资源消耗。
     *
     * @return {@link ExecutorService}
     */
    public ExecutorService fetchExecutorService() {
        return mGlobalConfigModule.provideExecutorService();
    }
}
