package com.smarthane.admiral.demo.app;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;

import com.google.gson.GsonBuilder;
import com.smarthane.admiral.core.base.delegate.AppLifecycles;
import com.smarthane.admiral.core.base.module.ClientModule;
import com.smarthane.admiral.core.base.module.GlobalConfigModule;
import com.smarthane.admiral.core.http.imageloader.glide.GlideImageLoaderStrategy;
import com.smarthane.admiral.core.integration.ConfigModule;
import com.smarthane.admiral.core.util.LogUtils;
import com.smarthane.admiral.demo.mvp.model.api.Api;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.rx_cache3.runtime.internal.RxCache;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

/**
 * @author smarthane
 * @time 2019/10/20 13:51
 * @describe  * App 的全局配置信息在此配置, 需要将此实现类声明到 AndroidManifest 中
 *  * ConfigModule 的实现类可以有无数多个, 在 Application 中只是注册回调, 并不会影响性能 (多个 ConfigModule 在多 Module 环境下尤为受用)
 *  * ConfigModule 接口的实现类对象是通过反射生成的, 这里会有些性能损耗
 */
public final class GlobalConfiguration implements ConfigModule {


    @Override
    public void applyOptions(@NonNull Context context, @NonNull GlobalConfigModule.Builder builder) {
        LogUtils.debugInfo("GlobalConfiguration applyOptions");
        builder.baseurl(Api.APP_DOMAIN)
                .imageLoaderStrategy(new GlideImageLoaderStrategy())
                // 这里提供一个全局处理 Http 请求和响应结果的处理类, 可以比客户端提前一步拿到服务器返回的结果, 可以做一些操作, 比如 Token 超时后, 重新获取 Token
                .globalHttpHandler(new GlobalHttpHandlerImpl(context))
                // 用来处理 RxJava 中发生的所有错误, RxJava 中发生的每个错误都会回调此接口
                // RxJava 必须要使用 ErrorHandleSubscriber (默认实现 Subscriber 的 onError 方法), 此监听才生效
                .responseErrorListener(new ResponseErrorListenerImpl())
                // 这里可以自己自定义配置 Gson 的参数
                .gsonConfiguration(new ClientModule.GsonConfiguration() {
                    @Override
                    public void configGson(@NonNull Context context, @NonNull GsonBuilder builder) {
                        LogUtils.debugInfo("GlobalConfiguration applyOptions configGson");
                        builder
                                // 支持序列化值为 null 的参数
                                .serializeNulls()
                                // 支持将序列化 key 为 Object 的 Map, 默认只能序列化 key 为 String 的 Map
                                .enableComplexMapKeySerialization();
                    }
                })
                // 这里可以自己自定义配置 Retrofit 的参数, 甚至您可以替换框架配置好的 OkHttpClient 对象 (但是不建议这样做, 这样做您将损失框架提供的很多功能)
                .retrofitConfiguration(new ClientModule.RetrofitConfiguration() {
                    @Override
                    public void configRetrofit(@NonNull Context context, @NonNull Retrofit.Builder builder) {
                        LogUtils.debugInfo("GlobalConfiguration applyOptions configRetrofit");
                        // 比如使用 FastJson 替代 Gson
                        // builder.addConverterFactory(FastJsonConverterFactory.create());
                    }
                })
                // 这里可以自己自定义配置 Okhttp 的参数
                .okhttpConfiguration(new ClientModule.OkhttpConfiguration() {
                    @Override
                    public void configOkhttp(@NonNull Context context, @NonNull OkHttpClient.Builder builder) {
                        LogUtils.debugInfo("GlobalConfiguration applyOptions okhttpConfiguration");
                        // builder.sslSocketFactory(); //支持 Https, 详情请百度
                        builder.writeTimeout(10, TimeUnit.SECONDS);
                        // 使用一行代码监听 Retrofit／Okhttp 上传下载进度监听,以及 Glide 加载进度监听, 详细使用方法请查看 https://github.com/JessYanCoding/ProgressManager
                        // ProgressManager.getInstance().with(okhttpBuilder);
                        // 让 Retrofit 同时支持多个 BaseUrl 以及动态改变 BaseUrl, 详细使用方法请查看 https://github.com/JessYanCoding/RetrofitUrlManager
                        //RetrofitUrlManager.getInstance().with(builder);
                    }
                })
                // 可以自定义一个单例的线程池供全局使用
                //.executorService(Executors.newCachedThreadPool())
                // 可根据当前项目的情况以及环境为框架某些部件提供自定义的缓存策略, 具有强大的扩展性
                /*.cacheFactory(new Cache.Factory() {
                    @NonNull
                    @Override
                    public Cache build(CacheType type) {
                        switch (type.getCacheTypeId()){
                            case CacheType.EXTRAS_TYPE_ID:
                                return new IntelligentCache(500);
                            case CacheType.CACHE_SERVICE_CACHE_TYPE_ID:
                                return new Cache(type.calculateCacheSize(context));//自定义 Cache
                            default:
                                return new LruCache(200);
                        }
                    }
                })*/
                // 这里可以自己自定义配置 RxCache 的参数
                .rxCacheConfiguration(new ClientModule.RxCacheConfiguration() {
                    @Override
                    public RxCache configRxCache(@NonNull Context context, @NonNull RxCache.Builder builder) {
                        LogUtils.debugInfo("GlobalConfiguration applyOptions rxCacheConfiguration");
                        builder.useExpiredDataIfLoaderNotAvailable(true);
                        // 想自定义 RxCache 的缓存文件夹或者解析方式, 如改成 FastJson, 请 return rxCacheBuilder.persistence(cacheDirectory, new FastJsonSpeaker());
                        // 否则请 return null;
                        return null;
                    }
                });
    }

    @Override
    public void injectAppLifecycle(@NonNull Context context, @NonNull List<AppLifecycles> lifecycles) {
        LogUtils.debugInfo("GlobalConfiguration injectAppLifecycle");
        // AppLifecycles 中的所有方法都会在基类 Application 的对应生命周期中被调用, 所以在对应的方法中可以扩展一些自己需要的逻辑
        // 可以根据不同的逻辑添加多个实现类
        lifecycles.add(new AppLifecyclesImpl());
    }

    @Override
    public void injectActivityLifecycle(@NonNull Context context, @NonNull List<Application.ActivityLifecycleCallbacks> lifecycles) {
        LogUtils.debugInfo("GlobalConfiguration injectActivityLifecycle");
        // ActivityLifecycleCallbacks 中的所有方法都会在 Activity (包括三方库) 的对应生命周期中被调用, 所以在对应的方法中可以扩展一些自己需要的逻辑
        // 可以根据不同的逻辑添加多个实现类
        lifecycles.add(new ActivityLifecycleCallbacksImpl());
    }

    @Override
    public void injectFragmentLifecycle(@NonNull Context context, @NonNull List<FragmentManager.FragmentLifecycleCallbacks> lifecycles) {
        LogUtils.debugInfo("GlobalConfiguration injectFragmentLifecycle");
        // FragmentLifecycleCallbacks 中的所有方法都会在 Fragment (包括三方库) 的对应生命周期中被调用, 所以在对应的方法中可以扩展一些自己需要的逻辑
        // 可以根据不同的逻辑添加多个实现类
        lifecycles.add(new FragmentLifecycleCallbacksImpl());
    }
}
