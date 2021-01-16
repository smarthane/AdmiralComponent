package com.smarthane.admiralcomponent.zhihu.mvp.presenter;

import com.smarthane.admiral.core.base.AppComponent;
import com.smarthane.admiral.core.base.rx.errorhandler.ErrorHandleSubscriber;
import com.smarthane.admiral.core.base.rx.errorhandler.RetryWithDelay;
import com.smarthane.admiral.core.mvp.BasePresenter;
import com.smarthane.admiral.core.util.RxLifecycleUtils;
import com.smarthane.admiralcomponent.zhihu.mvp.contract.DetailContract;
import com.smarthane.admiralcomponent.zhihu.mvp.model.ZhihuModel;
import com.smarthane.admiralcomponent.zhihu.mvp.model.entity.ZhihuDetailBean;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Action;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;

/**
 * @author smarthane
 * @time 2019/11/10 9:41
 * @describe
 */
public class DetailPresenter extends BasePresenter<DetailContract.Model, DetailContract.View> {

    private DetailPresenter(DetailContract.Model model, DetailContract.View rootView) {
        super(model, rootView);
    }

    public static DetailPresenter build(DetailContract.View rootView) {
        return new DetailPresenter(new ZhihuModel(), rootView);
    }

    public void requestDetailInfo(int id) {
        mModel.getDetailInfo(id)
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
                .subscribe(new ErrorHandleSubscriber<ZhihuDetailBean>(AppComponent
                        .get()
                        .fetchRxErrorHandler()) {
                    @Override
                    public void onNext(ZhihuDetailBean detailBean) {
                        mRootView.shonContent(detailBean);
                    }
                });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
