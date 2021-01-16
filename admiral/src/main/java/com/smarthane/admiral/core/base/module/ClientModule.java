package com.smarthane.admiral.core.base.module;

import android.content.Context;

import androidx.annotation.NonNull;

import com.google.gson.GsonBuilder;
import com.smarthane.admiral.core.base.AppComponent;
import com.smarthane.admiral.core.http.GlobalHttpHandler;
import com.smarthane.admiral.core.http.log.RequestInterceptor;
import com.smarthane.admiral.core.util.DataUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

import io.rx_cache3.runtime.internal.RxCache;
import io.victoralbertos.jolyglot.GsonSpeaker;
import com.smarthane.admiral.core.base.rx.errorhandler.RxErrorHandler;
import okhttp3.Dispatcher;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author smarthane
 * @time 2019/10/20 16:52
 * @describe okhttp+retrofit+rxjava
 */
public class ClientModule {

    private static final int TIME_OUT = 10;

    private Retrofit mRetrofit;
    private OkHttpClient mOkHttpClient;
    private RxCache mRxCache;
    private File mRxCacheDirectory;
    private RxErrorHandler mRxErrorHandler;

    private ClientModule() {
    }

    private static class Holder {
        private static ClientModule sInstance = new ClientModule();
    }

    public static ClientModule get() {
        return Holder.sInstance;
    }

    /**
     * 提供 {@link Retrofit}
     * @return
     */
    public Retrofit provideRetrofit() {
        if (mRetrofit == null) {
            GlobalConfigModule globalConfigModule = getGlobalConfigModule();
            HttpUrl httpUrl = globalConfigModule.provideBaseUrl();
            Retrofit.Builder builder = new Retrofit.Builder();
            builder
                    // 域名
                    .baseUrl(httpUrl)
                    // 设置 OkHttp
                    .client(provideOkHttpClient());
            RetrofitConfiguration configuration = globalConfigModule.provideRetrofitConfiguration();
            if (configuration != null) {
                configuration.configRetrofit(AppComponent.get().fetchApplication(), builder);
            }
            builder
                    // 使用 RxJava
                    .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                    // 使用 Gson addConverterFactory 只有先添加到addConverterFactory可以正常使用
                    .addConverterFactory(GsonConverterFactory.create(globalConfigModule.provideGson()));
            mRetrofit = builder.build();
        }
        return mRetrofit;
    }

    /**
     * 提供 {@link OkHttpClient}
     * @return
     */
    public OkHttpClient provideOkHttpClient() {
        if (mOkHttpClient == null) {
            GlobalConfigModule globalConfigModule = getGlobalConfigModule();
            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder
                    .connectTimeout(TIME_OUT, TimeUnit.SECONDS)
                    .readTimeout(TIME_OUT, TimeUnit.SECONDS)
                    .addNetworkInterceptor(RequestInterceptor.get());

            GlobalHttpHandler handler = globalConfigModule.provideGlobalHttpHandler();
            if (handler != null) {
                builder.addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        return chain.proceed(handler.onHttpRequestBefore(chain, chain.request()));
                    }
                });
            }

            List<Interceptor> interceptors = globalConfigModule.provideInterceptors();
            // 如果外部提供了 Interceptor 的集合则遍历添加
            if (interceptors != null) {
                for (Interceptor interceptor : interceptors) {
                    builder.addInterceptor(interceptor);
                }
            }

            ExecutorService executorService = globalConfigModule.provideExecutorService();
            // 为 OkHttp 设置默认的线程池
            if (executorService != null) {
                builder.dispatcher(new Dispatcher(executorService));
            }

            OkhttpConfiguration configuration = globalConfigModule.provideOkhttpConfiguration();
            if (configuration != null) {
                configuration.configOkhttp(AppComponent.get().fetchApplication(), builder);
            }

            mOkHttpClient = builder.build();
        }
        return mOkHttpClient;
    }

    /**
     * 提供 {@link RxCache}
     * @return
     */
    public RxCache provideRxCache() {
        if (mRxCache == null) {
            GlobalConfigModule globalConfigModule = getGlobalConfigModule();
            RxCache.Builder builder = new RxCache.Builder();
            RxCacheConfiguration configuration = globalConfigModule.provideRxCacheConfiguration();
            if (configuration != null) {
                mRxCache = configuration.configRxCache(AppComponent.get().fetchApplication(), builder);
            }
            if (mRxCache == null) {
                mRxCache = builder
                        .persistence(provideRxCacheDirectory(), new GsonSpeaker(globalConfigModule.provideGson()));
            }
        }
        return mRxCache;
    }

    /**
     * 需要单独给 {@link RxCache} 提供子缓存文件
     * @return
     */
    public File provideRxCacheDirectory() {
        if (mRxCacheDirectory == null) {
            GlobalConfigModule globalConfigModule = getGlobalConfigModule();
            File cacheDir = globalConfigModule.provideCacheFile();
            File cacheDirectory = new File(cacheDir, "RxCache");
            mRxCacheDirectory = DataUtils.makeDirs(cacheDirectory);
        }
        return mRxCacheDirectory;
    }

    /**
     * 提供处理 RxJava 错误的管理器
     * @return
     */
    public RxErrorHandler provideRxErrorHandler() {
        if (mRxErrorHandler == null) {
            mRxErrorHandler = RxErrorHandler
                    .builder()
                    .with(AppComponent.get().fetchApplication())
                    .responseErrorListener(getGlobalConfigModule().provideResponseErrorListener())
                    .build();
        }
        return mRxErrorHandler;
    }

    private GlobalConfigModule getGlobalConfigModule() {
        AppComponent appComponent = AppComponent.get();
        return appComponent.fetchGlobalConfigModule();
    }

    /**
     * {@link Retrofit} 自定义配置接口
     */
    public interface RetrofitConfiguration {
        void configRetrofit(@NonNull Context context, @NonNull Retrofit.Builder builder);
    }

    /**
     * {@link OkHttpClient} 自定义配置接口
     */
    public interface OkhttpConfiguration {
        void configOkhttp(@NonNull Context context, @NonNull OkHttpClient.Builder builder);
    }

    /**
     * GsonConfiguration
     */
    public interface GsonConfiguration {
        void configGson(@NonNull Context context, @NonNull GsonBuilder builder);
    }

    /**
     * {@link RxCache} 自定义配置接口
     */
    public interface RxCacheConfiguration {
        /**
         * 若想自定义 RxCache 的缓存文件夹或者解析方式, 如改成 FastJson
         * 请 {@code return rxCacheBuilder.persistence(cacheDirectory, new FastJsonSpeaker());}, 否则请 {@code return null;}
         *
         * @param context {@link Context}
         * @param builder {@link RxCache.Builder}
         * @return {@link RxCache}
         */
        RxCache configRxCache(@NonNull Context context, @NonNull RxCache.Builder builder);
    }
}
