package com.smarthane.admiral.core.base.module;

import android.content.Context;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.smarthane.admiral.core.http.BaseUrl;
import com.smarthane.admiral.core.http.GlobalHttpHandler;
import com.smarthane.admiral.core.http.imageloader.BaseImageLoaderStrategy;
import com.smarthane.admiral.core.http.log.DefaultFormatPrinter;
import com.smarthane.admiral.core.http.log.FormatPrinter;
import com.smarthane.admiral.core.http.log.RequestInterceptor;
import com.smarthane.admiral.core.integration.IRepositoryManager;
import com.smarthane.admiral.core.integration.cache.Cache;
import com.smarthane.admiral.core.integration.cache.CacheType;
import com.smarthane.admiral.core.integration.cache.IntelligentCache;
import com.smarthane.admiral.core.integration.cache.LruCache;
import com.smarthane.admiral.core.util.DataUtils;
import com.smarthane.admiral.core.util.PreconditionUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.smarthane.admiral.core.base.rx.errorhandler.ResponseErrorListener;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.internal.Util;

/**
 * @author smarthane
 * @time 2019/10/20 16:32
 * @describe 框架独创的建造者模式,可向框架中注入外部配置的自定义参数
 */
public class GlobalConfigModule {

    private Context mContext;
    private HttpUrl mApiUrl;
    private BaseUrl mBaseUrl;
    private BaseImageLoaderStrategy mLoaderStrategy;
    private GlobalHttpHandler mGlobalHttpHandler;
    private List<Interceptor> mInterceptors;
    private ResponseErrorListener mErrorListener;
    private File mCacheFile;
    private ClientModule.RetrofitConfiguration mRetrofitConfiguration;
    private ClientModule.OkhttpConfiguration mOkhttpConfiguration;
    private ClientModule.RxCacheConfiguration mRxCacheConfiguration;
    private ClientModule.GsonConfiguration mGsonConfiguration;
    private RequestInterceptor.Level mPrintHttpLogLevel;
    private FormatPrinter mFormatPrinter;
    private Cache.Factory mCacheFactory;
    private ExecutorService mExecutorService;
    private IRepositoryManager.ObtainServiceDelegate mObtainServiceDelegate;

    private Gson gson;

    private GlobalConfigModule(Builder builder) {
        this.mContext = builder.mContext;
        this.mApiUrl = builder.apiUrl;
        this.mBaseUrl = builder.baseUrl;
        this.mLoaderStrategy = builder.loaderStrategy;
        this.mGlobalHttpHandler = builder.globalHttpHandler;
        this.mInterceptors = builder.interceptors;
        this.mErrorListener = builder.responseErrorListener;
        this.mCacheFile = builder.cacheFile;
        this.mRetrofitConfiguration = builder.retrofitConfiguration;
        this.mOkhttpConfiguration = builder.okhttpConfiguration;
        this.mRxCacheConfiguration = builder.rxCacheConfiguration;
        this.mGsonConfiguration = builder.gsonConfiguration;
        this.mPrintHttpLogLevel = builder.printHttpLogLevel;
        this.mFormatPrinter = builder.formatPrinter;
        this.mCacheFactory = builder.cacheFactory;
        this.mExecutorService = builder.executorService;
        this.mObtainServiceDelegate = builder.obtainServiceDelegate;
    }

    public HttpUrl provideBaseUrl() {
        if (mBaseUrl != null) {
            HttpUrl httpUrl = mBaseUrl.url();
            if (httpUrl != null) {
                return httpUrl;
            }
        }
        return mApiUrl;
    }

    @Nullable
    public BaseImageLoaderStrategy provideImageLoaderStrategy() {
        return mLoaderStrategy;
    }

    public GlobalHttpHandler provideGlobalHttpHandler() {
        return mGlobalHttpHandler;
    }

    public List<Interceptor> provideInterceptors() {
        return mInterceptors;
    }

    public ResponseErrorListener provideResponseErrorListener() {
        return mErrorListener == null ? ResponseErrorListener.EMPTY : mErrorListener;
    }

    public File provideCacheFile() {
        return mCacheFile == null ? DataUtils.getCacheFile(mContext) : mCacheFile;
    }

    @Nullable
    public ClientModule.RetrofitConfiguration provideRetrofitConfiguration() {
        return mRetrofitConfiguration;
    }

    @Nullable
    public ClientModule.OkhttpConfiguration provideOkhttpConfiguration() {
        return mOkhttpConfiguration;
    }

    @Nullable
    public ClientModule.RxCacheConfiguration provideRxCacheConfiguration() {
        return mRxCacheConfiguration;
    }

    @Nullable
    public ClientModule.GsonConfiguration provideGsonConfiguration() {
        return mGsonConfiguration;
    }

    public Gson provideGson() {
        if (gson == null) {
            GsonBuilder builder = new GsonBuilder();
            if (mGsonConfiguration != null) {
                mGsonConfiguration.configGson(this.mContext, builder);
            }
            gson = builder.create();
        }
        return gson;
    }

    public RequestInterceptor.Level providePrintHttpLogLevel() {
        return mPrintHttpLogLevel == null ? RequestInterceptor.Level.ALL : mPrintHttpLogLevel;
    }

    public FormatPrinter provideFormatPrinter() {
        if (mFormatPrinter == null) {
            mFormatPrinter = new DefaultFormatPrinter();
        }
        return mFormatPrinter;
    }

    public Cache.Factory provideCacheFactory() {
        if (mCacheFactory == null) {
            mCacheFactory = new Cache.Factory() {
                @NonNull
                @Override
                public Cache build(CacheType type) {
                    // 若想自定义 LruCache 的 size, 或者不想使用 LruCache, 想使用自己自定义的策略
                    // 使用 GlobalConfigModule.Builder#cacheFactory() 即可扩展
                    switch (type.getCacheTypeId()) {
                        // Activity、Fragment 以及 Extras 使用 IntelligentCache (具有 LruCache 和 可永久存储数据的 Map)
                        case CacheType.EXTRAS_TYPE_ID:
                        case CacheType.ACTIVITY_CACHE_TYPE_ID:
                        case CacheType.FRAGMENT_CACHE_TYPE_ID:
                            return new IntelligentCache(type.calculateCacheSize(mContext));
                        // 其余使用 LruCache (当达到最大容量时可根据 LRU 算法抛弃不合规数据)
                        default:
                            return new LruCache(type.calculateCacheSize(mContext));
                    }
                }
            };
        }
        return mCacheFactory;
    }

    public ExecutorService provideExecutorService() {
        if (mExecutorService == null) {
            mExecutorService = new ThreadPoolExecutor(0, Integer.MAX_VALUE, 60, TimeUnit.SECONDS,
                    new SynchronousQueue<Runnable>(), Util.threadFactory("Admiral Executor", false));
        }
        return mExecutorService;
    }

    @Nullable
    public IRepositoryManager.ObtainServiceDelegate provideObtainServiceDelegate() {
        return mObtainServiceDelegate;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private Context mContext;
        private HttpUrl apiUrl;
        private BaseUrl baseUrl;
        private BaseImageLoaderStrategy loaderStrategy;
        private GlobalHttpHandler globalHttpHandler;
        private List<Interceptor> interceptors;
        private ResponseErrorListener responseErrorListener;
        private File cacheFile;
        private ClientModule.RetrofitConfiguration retrofitConfiguration;
        private ClientModule.OkhttpConfiguration okhttpConfiguration;
        private ClientModule.RxCacheConfiguration rxCacheConfiguration;
        private ClientModule.GsonConfiguration gsonConfiguration;
        private RequestInterceptor.Level printHttpLogLevel;
        private FormatPrinter formatPrinter;
        private Cache.Factory cacheFactory;
        private ExecutorService executorService;
        private IRepositoryManager.ObtainServiceDelegate obtainServiceDelegate;

        private Builder() {
        }

        public Builder context(Context mContext) {
            this.mContext = mContext;
            return this;
        }

        /**
         * 基础url
         * @param baseUrl
         * @return
         */
        public Builder baseurl(String baseUrl) {
            if (TextUtils.isEmpty(baseUrl)) {
                throw new NullPointerException("BaseUrl can not be empty");
            }
            this.apiUrl = HttpUrl.parse(baseUrl);
            return this;
        }

        public Builder baseurl(BaseUrl baseUrl) {
            this.baseUrl = PreconditionUtils.checkNotNull(baseUrl, BaseUrl.class.getCanonicalName() + "can not be null.");
            return this;
        }

        /**
         * 用来请求网络图片
         * @param loaderStrategy
         * @return
         */
        public Builder imageLoaderStrategy(BaseImageLoaderStrategy loaderStrategy) {
            this.loaderStrategy = loaderStrategy;
            return this;
        }

        /**
         * 用来处理http响应结果
         * @param globalHttpHandler
         * @return
         */
        public Builder globalHttpHandler(GlobalHttpHandler globalHttpHandler) {
            this.globalHttpHandler = globalHttpHandler;
            return this;
        }

        /**
         * 动态添加任意个interceptor
         * @param interceptor
         * @return
         */
        public Builder addInterceptor(Interceptor interceptor) {
            if (interceptors == null) {
                interceptors = new ArrayList<>();
            }
            this.interceptors.add(interceptor);
            return this;
        }

        /**
         * 处理所有RxJava的onError逻辑
         * @param listener
         * @return
         */
        public Builder responseErrorListener(ResponseErrorListener listener) {
            this.responseErrorListener = listener;
            return this;
        }

        public Builder cacheFile(File cacheFile) {
            this.cacheFile = cacheFile;
            return this;
        }

        public Builder retrofitConfiguration(ClientModule.RetrofitConfiguration retrofitConfiguration) {
            this.retrofitConfiguration = retrofitConfiguration;
            return this;
        }

        public Builder okhttpConfiguration(ClientModule.OkhttpConfiguration okhttpConfiguration) {
            this.okhttpConfiguration = okhttpConfiguration;
            return this;
        }

        public Builder rxCacheConfiguration(ClientModule.RxCacheConfiguration rxCacheConfiguration) {
            this.rxCacheConfiguration = rxCacheConfiguration;
            return this;
        }

        public Builder gsonConfiguration(ClientModule.GsonConfiguration gsonConfiguration) {
            this.gsonConfiguration = gsonConfiguration;
            return this;
        }

        /**
         * 是否让框架打印 Http 的请求和响应信息
         * @param printHttpLogLevel
         * @return
         */
        public Builder printHttpLogLevel(RequestInterceptor.Level printHttpLogLevel) {
            this.printHttpLogLevel = PreconditionUtils.checkNotNull(printHttpLogLevel, "The printHttpLogLevel can not be null, use RequestInterceptor.Level.NONE instead.");
            return this;
        }

        public Builder formatPrinter(FormatPrinter formatPrinter) {
            this.formatPrinter = PreconditionUtils.checkNotNull(formatPrinter, FormatPrinter.class.getCanonicalName() + "can not be null.");
            return this;
        }

        public Builder cacheFactory(Cache.Factory cacheFactory) {
            this.cacheFactory = cacheFactory;
            return this;
        }

        public Builder executorService(ExecutorService executorService) {
            this.executorService = executorService;
            return this;
        }

        public Builder obtainServiceDelegate(IRepositoryManager.ObtainServiceDelegate obtainServiceDelegate) {
            this.obtainServiceDelegate = obtainServiceDelegate;
            return this;
        }

        public GlobalConfigModule build() {
            return new GlobalConfigModule(this);
        }
    }
}
