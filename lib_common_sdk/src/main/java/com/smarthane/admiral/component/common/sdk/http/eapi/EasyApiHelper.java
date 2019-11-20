package com.smarthane.admiral.component.common.sdk.http.eapi;

import androidx.annotation.NonNull;

import com.smarthane.admiral.component.common.sdk.http.eapi.request.EapiBaseRequest;
import com.smarthane.admiral.component.common.sdk.util.BeanUtils;
import com.smarthane.admiral.core.base.AppComponent;
import com.smarthane.admiral.core.base.rx.errorhandler.ErrorHandleSubscriber;
import com.smarthane.admiral.core.base.rx.errorhandler.RetryWithDelay;
import com.smarthane.admiral.core.util.LogUtils;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

/**
 * @author smarthane
 * @time 2019/11/10 13:46
 * @describe 简单的网络请求工具
 * TODO 封装简单网络请求 让网络操作更Easy
 */
public class EasyApiHelper {

    private static final EapiGlobalConfig NET_GLOBAL_CONFIG = EapiGlobalConfig.get();

    private static ConcurrentHashMap<Object, CompositeDisposable> arrayMaps = new ConcurrentHashMap<>();



    public static EapiGlobalConfig config() {
        return NET_GLOBAL_CONFIG;
    }

    private EasyApiHelper() {
        throw new IllegalStateException("you can't instantiate me!");
    }

    /**
     * GET请求
     *
     * @param request
     * @param eapiCallback
     */
    public static <T extends Object> void get(final EapiBaseRequest request, final EapiCallback<T> eapiCallback) {
        if (request == null) {
            return;
        }
        AppComponent
                .get()
                .fetchRepositoryManager()
                .obtainRetrofitService(EapiService.class)
                .get(request.getUrl(), BeanUtils.describe(request, request.getExcludes()))
                .subscribeOn(Schedulers.io())
                // 遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
                .retryWhen(new RetryWithDelay(
                        request.getRetryCount(),
                        request.getRetryDelayMillis()
                ))
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        LogUtils.debugInfo("EasyApiHelper Get doOnSubscribe");
                        if (eapiCallback != null) {
                            eapiCallback.onStart();
                        }
                    }
                })
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ErrorHandleSubscriber<ResponseBody>(AppComponent
                        .get()
                        .fetchRxErrorHandler()) {

                    private Disposable mDisposable;

                    @Override
                    public void onSubscribe(@NonNull Disposable disposable) {
                        super.onSubscribe(disposable);
                        LogUtils.debugInfo("EasyApiHelper Get onSubscribe");
                        LogUtils.debugInfo("Request Tag --->　" + request.getTag().toString());
                        addDisposable(request.getTag(), disposable);
                        mDisposable = disposable;
                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {
                        try {
                            LogUtils.debugInfo("EasyApiHelper Get onNext");
                            ParameterizedType type = (ParameterizedType) eapiCallback.getClass()
                                    .getGenericSuperclass();
                            T resultData = AppComponent
                                    .get()
                                    .fetchGson()
                                    .fromJson(responseBody.string(), type.getActualTypeArguments()[0]);
                            if (eapiCallback != null) {
                                eapiCallback.onSuccess(resultData);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                            LogUtils.errorInfo(e.getMessage());
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable t) {
                        super.onError(t);
                        LogUtils.debugInfo("EasyApiHelper Get onError " + t.getMessage());
                        if (eapiCallback != null) {
                            eapiCallback.onFail(t);
                        }
                        removeDisposable(request.getTag(), mDisposable);
                    }

                    @Override
                    public void onComplete() {
                        super.onComplete();
                        LogUtils.debugInfo("EasyApiHelper Get onComplete");
                        if (eapiCallback != null) {
                            eapiCallback.onComplete();
                        }
                        removeDisposable(request.getTag(), mDisposable);
                    }
                });
    }

    /**
     * post请求
     *
     * @param request
     * @param eapiCallback
     */
    public static <T extends Object> void post(final EapiBaseRequest request, final EapiCallback<T> eapiCallback) {
        AppComponent
                .get()
                .fetchRepositoryManager()
                .obtainRetrofitService(EapiService.class)
                .post(request.getUrl(), BeanUtils.describe(request, request.getExcludes()))
                .subscribeOn(Schedulers.io())
                // 遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
                .retryWhen(new RetryWithDelay(
                        request.getRetryCount(),
                        request.getRetryDelayMillis()
                ))
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        LogUtils.debugInfo("EasyApiHelper Post doOnSubscribe");
                        if (eapiCallback != null) {
                            eapiCallback.onStart();
                        }
                    }
                })
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ErrorHandleSubscriber<ResponseBody>(AppComponent
                        .get()
                        .fetchRxErrorHandler()) {

                    private Disposable mDisposable;

                    @Override
                    public void onSubscribe(@NonNull Disposable disposable) {
                        super.onSubscribe(disposable);
                        LogUtils.debugInfo("EasyApiHelper Post onSubscribe");
                        LogUtils.debugInfo("Request Tag --->　" + request.getTag().toString());
                        addDisposable(request.getTag(), disposable);
                        mDisposable = disposable;
                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {
                        try {
                            LogUtils.debugInfo("EasyApiHelper Post onNext");
                            ParameterizedType type = (ParameterizedType) eapiCallback.getClass()
                                    .getGenericSuperclass();
                            T resultData = AppComponent
                                    .get()
                                    .fetchGson()
                                    .fromJson(responseBody.string(), type.getActualTypeArguments()[0]);
                            if (eapiCallback != null) {
                                eapiCallback.onSuccess(resultData);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                            LogUtils.errorInfo(e.getMessage());
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable t) {
                        super.onError(t);
                        LogUtils.debugInfo("EasyApiHelper Post onError " + t.getMessage());
                        if (eapiCallback != null) {
                            eapiCallback.onFail(t);
                        }
                        removeDisposable(request.getTag(), mDisposable);
                    }

                    @Override
                    public void onComplete() {
                        super.onComplete();
                        LogUtils.debugInfo("EasyApiHelper Post onComplete");
                        if (eapiCallback != null) {
                            eapiCallback.onComplete();
                        }
                        removeDisposable(request.getTag(), mDisposable);
                    }
                });
    }

    private static void addDisposable(Object tag, Disposable disposable) {
        if (tag == null) {
            return;
        }
        CompositeDisposable compositeDisposable = arrayMaps.get(tag);
        if (compositeDisposable == null) {
            compositeDisposable = new CompositeDisposable();
            arrayMaps.put(tag, compositeDisposable);
        }
        compositeDisposable.add(disposable);
    }

    private static void removeDisposable(Object tag, Disposable disposable) {
        if (tag == null) {
            return;
        }
        CompositeDisposable compositeDisposable = arrayMaps.get(tag);
        if (compositeDisposable != null) {
            compositeDisposable.remove(disposable);
            if (compositeDisposable.size() == 0) {
                compositeDisposable.clear();
                arrayMaps.remove(tag);
            }
        }
    }

    public static void cancel(Object tag) {
        if (tag == null) {
            return;
        }
        CompositeDisposable compositeDisposable = arrayMaps.remove(tag);
        if (compositeDisposable != null) {
            compositeDisposable.dispose();
            compositeDisposable.clear();
        }
    }

    public static void cancelAll() {
        if (arrayMaps.isEmpty()) {
            return;
        }
        Set<Object> keys = arrayMaps.keySet();
        for (Object apiKey : keys) {
            cancel(apiKey);
        }
    }

}
