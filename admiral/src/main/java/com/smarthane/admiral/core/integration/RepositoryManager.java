package com.smarthane.admiral.core.integration;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;

import com.smarthane.admiral.core.base.AppComponent;
import com.smarthane.admiral.core.integration.cache.Cache;
import com.smarthane.admiral.core.integration.cache.CacheType;
import com.smarthane.admiral.core.util.PreconditionUtils;

import java.lang.reflect.Proxy;

import io.rx_cache3.runtime.internal.RxCache;
import retrofit2.Retrofit;

/**
 * @author smarthane
 * @time 2019/10/20 14:52
 * @describe 管理网络请求层
 */
public class RepositoryManager implements IRepositoryManager {

    private Retrofit mRetrofit;
    private RxCache mRxCache;
    private Application mApplication;
    private Cache.Factory mCacheFactory;
    private ObtainServiceDelegate mObtainServiceDelegate;
    private Cache<String, Object> mRetrofitServiceCache;
    private Cache<String, Object> mCacheServiceCache;

    private RepositoryManager() {
        mRetrofit = AppComponent.get().fetchClientModule().provideRetrofit();
        mRxCache = AppComponent.get().fetchClientModule().provideRxCache();
        mApplication = AppComponent.get().fetchApplication();
        mCacheFactory = AppComponent.get().fetchCacheFactory();
        mObtainServiceDelegate = AppComponent.get().fetchGlobalConfigModule().provideObtainServiceDelegate();
    }

    private static class Holder {
        private static RepositoryManager sInstance = new RepositoryManager();
    }

    public static RepositoryManager get() {
        return Holder.sInstance;
    }

    /**
     * 根据传入的 Class 获取对应的 Retrofit service
     * @param serviceClass
     * @param <T>
     * @return
     */
    @NonNull
    @Override
    public <T> T obtainRetrofitService(@NonNull Class<T> serviceClass) {
        if (mRetrofitServiceCache == null) {
            mRetrofitServiceCache = mCacheFactory.build(CacheType.RETROFIT_SERVICE_CACHE);
        }
        PreconditionUtils.checkNotNull(mRetrofitServiceCache,
                "Cannot return null from a Cache.Factory#build(int) method");
        T retrofitService = (T) mRetrofitServiceCache.get(serviceClass.getCanonicalName());
        if (retrofitService == null) {
            if (mObtainServiceDelegate != null) {
                retrofitService = mObtainServiceDelegate.createRetrofitService(
                        mRetrofit, serviceClass);
            }
            if (retrofitService == null) {
                retrofitService = (T) Proxy.newProxyInstance(
                        serviceClass.getClassLoader(),
                        new Class[]{serviceClass},
                        new RetrofitServiceProxyHandler(mRetrofit, serviceClass));
            }
            mRetrofitServiceCache.put(serviceClass.getCanonicalName(), retrofitService);
        }
        return retrofitService;
    }

    /**
     * 根据传入的 Class 获取对应的 RxCache service
     * @param cacheClass RxCache service class
     * @param <T>
     * @return
     */
    @NonNull
    @Override
    public <T> T obtainCacheService(@NonNull Class<T> cacheClass) {
        PreconditionUtils.checkNotNull(cacheClass, "cacheClass == null");
        if (mCacheServiceCache == null) {
            mCacheServiceCache = mCacheFactory.build(CacheType.CACHE_SERVICE_CACHE);
        }
        PreconditionUtils.checkNotNull(mCacheServiceCache,
                "Cannot return null from a Cache.Factory#build(int) method");
        T cacheService = (T) mCacheServiceCache.get(cacheClass.getCanonicalName());
        if (cacheService == null) {
            cacheService = mRxCache.using(cacheClass);
            mCacheServiceCache.put(cacheClass.getCanonicalName(), cacheService);
        }
        return cacheService;
    }

    @Override
    public void clearAllCache() {
        mRxCache.evictAll().subscribe();
    }

    @NonNull
    @Override
    public Context getContext() {
        return mApplication;
    }

}
