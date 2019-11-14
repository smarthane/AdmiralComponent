package com.smarthane.admiralcomponent.netease.mvp.presenter;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.OnLifecycleEvent;

import com.smarthane.admiral.core.base.AppComponent;
import com.smarthane.admiral.core.mvp.BasePresenter;
import com.smarthane.admiral.core.util.LogUtils;
import com.smarthane.admiral.core.util.RxLifecycleUtils;
import com.smarthane.admiralcomponent.netease.mvp.contract.NewsMainContract;
import com.smarthane.admiralcomponent.netease.mvp.model.NewsMainModel;
import com.smarthane.admiralcomponent.netease.mvp.model.entity.NewsChannelTable;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import com.smarthane.admiral.core.base.rx.errorhandler.ErrorHandleSubscriber;
import com.smarthane.admiral.core.base.rx.errorhandler.RetryWithDelay;

/**
 * @author smarthane
 * @time 2019/11/10 16:57
 * @describe
 */
public class NewsMainPresenter extends BasePresenter<NewsMainContract.Model, NewsMainContract.View> {

    private NewsMainPresenter(NewsMainContract.Model model, NewsMainContract.View rootView) {
        super(model, rootView);
    }

    public static NewsMainPresenter build(NewsMainContract.View rootView) {
        return new NewsMainPresenter(new NewsMainModel(), rootView);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    void onCreate() {
        LogUtils.debugInfo("NewsMainPresenter 打开 App 时自动加载列表");
        // requestMineNewsChannels();
    }

    public void requestMineNewsChannels() {
        mModel.lodeMineNewsChannels()
                .subscribeOn(Schedulers.io())
                // 遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
                .retryWhen(new RetryWithDelay(3, 2))
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        // 显示下拉刷新的进度条
                        mRootView.showLoading();
                    }
                }).subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        // 隐藏下拉刷新的进度条
                        mRootView.hideLoading();
                    }
                })
                // 使用 Rxlifecycle,使 Disposable 和 Activity 一起销毁
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))
                .subscribe(new ErrorHandleSubscriber<List<NewsChannelTable>>(AppComponent
                        .get()
                        .fetchRxErrorHandler()) {
                    @Override
                    public void onNext(List<NewsChannelTable> newsChannelTables) {
                        mRootView.setMineNewsChannels(newsChannelTables);
                    }
                });
    }

}
