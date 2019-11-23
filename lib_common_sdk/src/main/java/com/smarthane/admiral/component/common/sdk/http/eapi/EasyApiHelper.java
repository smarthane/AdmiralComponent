package com.smarthane.admiral.component.common.sdk.http.eapi;

import androidx.annotation.NonNull;

import com.smarthane.admiral.component.common.sdk.http.eapi.request.EapiBaseRequest;
import com.smarthane.admiral.component.common.sdk.http.eapi.request.EapiDownloadRequest;
import com.smarthane.admiral.component.common.sdk.http.eapi.request.EapiUploadRequest;
import com.smarthane.admiral.component.common.sdk.util.BeanUtils;
import com.smarthane.admiral.core.base.AppComponent;
import com.smarthane.admiral.core.base.rx.errorhandler.ErrorHandleSubscriber;
import com.smarthane.admiral.core.base.rx.errorhandler.RetryWithDelay;
import com.smarthane.admiral.core.util.LogUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.lang.reflect.ParameterizedType;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
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
        if (request == null) {
            return;
        }
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


    /**
     * 下载
     * TODO 断点续传 以及 后台下载
     * @param request
     * @param callback
     */
    public static void downloadFile(final EapiDownloadRequest request, final EapiDownloadCallback callback) {
        if (request == null) {
            return;
        }
        AppComponent
                .get()
                .fetchRepositoryManager()
                .obtainRetrofitService(EapiService.class)
                .downloadFile("bytes=" + request.getReadLength() + "-", request.getUrl())
                .subscribeOn(Schedulers.io())
                .retryWhen(new RetryWithDelay(
                        request.getRetryCount(),
                        request.getRetryDelayMillis()
                ))
                .doOnNext(new Consumer<ResponseBody>() {
                    @Override
                    public void accept(ResponseBody responseBody) throws Exception {
                        File file = new File(request.getSavePath());
                        LogUtils.debugInfo("EasyApiHelper DownloadFile " + Thread.currentThread() + " File Path " + file.getAbsolutePath());
                        if (!file.getParentFile().exists()) {
                            if (!file.getParentFile().mkdirs()) {
                                LogUtils.errorInfo("EasyApiHelper DownloadFile Create File Error " + Thread.currentThread());
                                throw new FileNotFoundException("Create File Error");
                            }
                        }
                        long allLength = request.getCountLength();
                        if (allLength == 0) {
                            allLength = responseBody.contentLength();
                            request.setCountLength(allLength);
                        } else {
                            allLength = request.getCountLength();
                        }
                        RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rwd");
                        FileChannel channelOut = randomAccessFile.getChannel();
                        MappedByteBuffer mappedBuffer = channelOut.map(FileChannel.MapMode.READ_WRITE,
                                request.getReadLength(), allLength - request.getReadLength());
                        byte[] buffer = new byte[1024 * 8];
                        int len;
                        while ((len = responseBody.byteStream().read(buffer)) != -1) {
                            request.setReadLength(request.getReadLength() + len);
                            mappedBuffer.put(buffer, 0, len);
                            if (request.isEnableProgres()) {
                                //TODO 优化
                                Observable
                                        .just(request.getReadLength())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(new Consumer<Long>() {
                                            @Override
                                            public void accept(Long readLength) {
                                                int percent = (int) ((100 * request.getReadLength()) / request.getCountLength());
                                                if (callback != null) {
                                                    callback.onProgress(request.getReadLength()
                                                            , request.getCountLength()
                                                            , request.getReadLength() == request.getCountLength()
                                                    );
                                                    callback.onProgress(percent);
                                                }
                                                LogUtils.debugInfo("EasyApiHelper DownloadFile CountLength: " + request.getCountLength()
                                                        + " ReadLength: " + request.getReadLength()
                                                        + " Percent: " + percent);
                                            }
                                        });
                            }
                        }
                        responseBody.byteStream().close();
                        channelOut.close();
                        randomAccessFile.close();
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ErrorHandleSubscriber<ResponseBody>(AppComponent
                        .get()
                        .fetchRxErrorHandler()) {

                    private Disposable mDisposable;

                    @Override
                    public void onSubscribe(@NonNull Disposable disposable) {
                        super.onSubscribe(disposable);
                        LogUtils.debugInfo("EasyApiHelper DownloadFile onSubscribe " + Thread.currentThread());
                        LogUtils.debugInfo("Request Tag --->　" + request.getTag().toString());
                        if (callback != null) {
                            callback.onStart();
                        }
                        addDisposable(request.getTag(), disposable);
                        mDisposable = disposable;
                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {
                        LogUtils.debugInfo("EasyApiHelper DownloadFile onNext " + Thread.currentThread());
                        // do nothing
                    }

                    @Override
                    public void onError(@NonNull Throwable t) {
                        super.onError(t);
                        LogUtils.debugInfo("EasyApiHelper DownloadFile onError " + Thread.currentThread() + " " + t.getMessage());
                        if (callback != null) {
                            callback.onFail(t);
                        }
                        removeDisposable(request.getTag(), mDisposable);
                    }

                    @Override
                    public void onComplete() {
                        super.onComplete();
                        LogUtils.debugInfo("EasyApiHelper DownloadFile onComplete " + Thread.currentThread());
                        if (callback != null) {
                            callback.onComplete(request.getSavePath());
                        }
                        removeDisposable(request.getTag(), mDisposable);
                    }
                });
    }

    /**
     * 上传文件
     *
     * @param request
     * @param callback
     */
    public static void uploadFile(final EapiUploadRequest request, final EapiUploadCallback callback) {
        if (request == null) {
            return;
        }
        if (callback != null && request.isEnableProgres()) {
            request.setCallback(callback);
        }
        AppComponent.get()
                .fetchRepositoryManager()
                .obtainRetrofitService(EapiService.class)
                .uploadFiles(request.getUrl(), request.getParts())
                .subscribeOn(Schedulers.io())
                .retryWhen(new RetryWithDelay(
                        request.getRetryCount(),
                        request.getRetryDelayMillis()
                ))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ErrorHandleSubscriber<ResponseBody>(AppComponent
                        .get()
                        .fetchRxErrorHandler()) {

                    private Disposable mDisposable;

                    @Override
                    public void onSubscribe(@NonNull Disposable disposable) {
                        super.onSubscribe(disposable);
                        LogUtils.debugInfo("EasyApiHelper UploadFile onSubscribe " + Thread.currentThread());
                        LogUtils.debugInfo("Request Tag --->　" + request.getTag().toString());
                        if (callback != null) {
                            callback.onStart();
                        }
                        addDisposable(request.getTag(), disposable);
                        mDisposable = disposable;
                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {
                        LogUtils.debugInfo("EasyApiHelper UploadFile onNext " + Thread.currentThread());
                        if (callback != null) {
                            try {
                                callback.onComplete(responseBody.string());
                            } catch (IOException e) {
                                if (callback != null) {
                                    callback.onFail(e);
                                }
                            }
                        }
                        removeDisposable(request.getTag(), mDisposable);
                    }

                    @Override
                    public void onError(@NonNull Throwable t) {
                        super.onError(t);
                        LogUtils.debugInfo("EasyApiHelper UploadFile onError " + Thread.currentThread() + " " + t.getMessage());
                        if (callback != null) {
                            callback.onFail(t);
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
