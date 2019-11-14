package com.smarthane.admiral.core.util;

import com.smarthane.admiral.core.mvp.IView;

import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import com.smarthane.admiral.core.base.rx.errorhandler.RetryWithDelay;

/**
 * @author smarthane
 * @time 2019/11/10 15:01
 * @describe
 */
public class RxUtils {


    private RxUtils() {
        throw new IllegalStateException("you can't instantiate me!");
    }


    /**
     * 执行io线程, 取消注册在io, 回掉主线程
     *
     * @param <T>
     * @return
     */
    public static <T> ObservableTransformer<T, T> io_main() {
        return upstream -> upstream
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 重试
     * @param <T>
     * @return 重试次数
     */
    public static <T> ObservableTransformer<T, T> retry2() {
        return upstream -> upstream.retryWhen(new RetryWithDelay(2, 2));
    }

    /**
     * 都在io线程
     *
     * @param <T>
     * @return
     */
    public static <T> ObservableTransformer<T, T> all_io() {
        return upstream -> upstream
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io());
    }

    public static <T> ObservableTransformer<T, T> applySchedulers(final IView view) {
        return new ObservableTransformer<T, T>() {
            @Override
            public Observable<T> apply(Observable<T> observable) {
                return observable.subscribeOn(Schedulers.io())
                        .doOnSubscribe(new Consumer<Disposable>() {
                            @Override
                            public void accept(@NonNull Disposable disposable) throws Exception {
                                view.showLoading();//显示进度条
                            }
                        })
                        .subscribeOn(AndroidSchedulers.mainThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doFinally(new Action() {
                            @Override
                            public void run() {
                                view.hideLoading();//隐藏进度条
                            }
                        }).compose(RxLifecycleUtils.bindToLifecycle(view));
            }
        };
    }
}
