package com.smarthane.admiralcomponent.zhihu.mvp.presenter;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.OnLifecycleEvent;

import com.smarthane.admiral.core.base.AppComponent;
import com.smarthane.admiral.core.mvp.BasePresenter;
import com.smarthane.admiral.core.util.RxLifecycleUtils;
import com.smarthane.admiralcomponent.zhihu.mvp.contract.ZhihuHomeContract;
import com.smarthane.admiralcomponent.zhihu.mvp.model.ZhihuModel;
import com.smarthane.admiralcomponent.zhihu.mvp.model.entity.DailyListBean;
import com.smarthane.admiralcomponent.zhihu.mvp.ui.adapter.ZhihuHomeAdapter;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import com.smarthane.admiral.core.base.rx.errorhandler.ErrorHandleSubscriber;
import com.smarthane.admiral.core.base.rx.errorhandler.RetryWithDelay;

/**
 * @author smarthane
 * @time 2019/11/10 9:41
 * @describe
 */
public class ZhihuHomePresenter extends BasePresenter<ZhihuHomeContract.Model, ZhihuHomeContract.View> {

    private ZhihuHomeAdapter zhihuHomeAdapter;

    private ZhihuHomePresenter(ZhihuHomeContract.Model model, ZhihuHomeContract.View rootView) {
        super(model, rootView);
    }

    public static ZhihuHomePresenter build(ZhihuHomeContract.View rootView) {
        return new ZhihuHomePresenter(new ZhihuModel(), rootView);
    }

    public void setZhihuHomeAdapter(ZhihuHomeAdapter zhihuHomeAdapter) {
        this.zhihuHomeAdapter = zhihuHomeAdapter;
    }

    /**
     * 使用 2017 Google IO 发布的 Architecture Components 中的 Lifecycles 的新特性 (此特性已被加入 Support library)
     * 使 {@code Presenter} 可以与 { SupportActivity} 和 { Fragment} 的部分生命周期绑定
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    void onCreate() {
        // 打开 App 时自动加载列表
        requestDailyList();
    }

    public void requestDailyList() {
        mModel.getDailyList()
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
                        // 隐藏上拉加载更多的进度条
                        mRootView.hideLoading();
                    }
                })
                // 使用 Rxlifecycle,使 Disposable 和 Activity 一起销毁
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))
                .subscribe(new ErrorHandleSubscriber<DailyListBean>(AppComponent
                        .get()
                        .fetchRxErrorHandler()) {
                    @Override
                    public void onNext(DailyListBean dailyListBean) {
                        if (zhihuHomeAdapter != null) {
                            zhihuHomeAdapter.setNewData(dailyListBean.getStories());
                        }
                    }
                });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        zhihuHomeAdapter = null;
    }
}
