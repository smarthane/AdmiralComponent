package com.smarthane.admiral.component.common.sdk.http.eapi;

import androidx.annotation.NonNull;

import com.smarthane.admiral.component.common.sdk.http.eapi.request.EapiBaseRequest;
import com.smarthane.admiral.component.common.sdk.util.BeanUtils;
import com.smarthane.admiral.core.base.AppComponent;
import com.smarthane.admiral.core.base.rx.errorhandler.ErrorHandleSubscriber;
import com.smarthane.admiral.core.base.rx.errorhandler.RetryWithDelay;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import io.reactivex.android.schedulers.AndroidSchedulers;
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

    private static ConcurrentHashMap<Object, Disposable> arrayMaps = new ConcurrentHashMap<>();

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
    public static void get(final EapiBaseRequest request, final EapiCallback eapiCallback) {
        AppComponent
                .get()
                .fetchRepositoryManager()
                .obtainRetrofitService(EapiService.class)
                .get(request.getUrl(), BeanUtils.describe(request))
                .subscribeOn(Schedulers.io())
                // 遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
                .retryWhen(new RetryWithDelay(
                        request.getRetryCount(),
                        request.getRetryDelayMillis()
                ))
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        addDisposable(request.getTag(), disposable);
                        if (eapiCallback != null) {
                            eapiCallback.onStart(disposable);
                        }
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        removeDisposable(request.getTag());
                        if (eapiCallback != null) {
                            eapiCallback.onComplete();
                        }
                    }
                })
                .subscribe(new ErrorHandleSubscriber<ResponseBody>(AppComponent
                        .get()
                        .fetchRxErrorHandler()) {

                    @Override
                    public void onNext(ResponseBody responseBody) {
                        //TODO T转换
                        eapiCallback.onSuccess(responseBody);
                    }

                    @Override
                    public void onError(@NonNull Throwable t) {
                        super.onError(t);
                        removeDisposable(request.getTag());
                        if (eapiCallback != null) {
                            eapiCallback.onFail(t);
                        }
                    }

                    @Override
                    public void onComplete() {
                        super.onComplete();
                        removeDisposable(request.getTag());
                    }
                });
    }

    private static void addDisposable(Object tag, Disposable disposable) {
        arrayMaps.put(tag, disposable);
    }

    private static void removeDisposable(Object tag) {
        if (!arrayMaps.isEmpty()) {
            arrayMaps.remove(tag);
        }
    }

    private static void removeAllDisposable() {
        if (!arrayMaps.isEmpty()) {
            arrayMaps.clear();
        }
    }

    public static void cancel(Object tag) {
        if (arrayMaps.isEmpty()) {
            return;
        }
        if (arrayMaps.get(tag) == null) {
            return;
        }
        if (!arrayMaps.get(tag).isDisposed()) {
            arrayMaps.get(tag).dispose();
            arrayMaps.remove(tag);
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
