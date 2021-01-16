package com.smarthane.admiralcomponent.gank.mvp.presenter;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.OnLifecycleEvent;

import com.smarthane.admiral.core.base.AppComponent;
import com.smarthane.admiral.core.mvp.BasePresenter;
import com.smarthane.admiral.core.util.LogUtils;
import com.smarthane.admiral.core.util.RxLifecycleUtils;
import com.smarthane.admiralcomponent.gank.mvp.contract.GankHomeContract;
import com.smarthane.admiralcomponent.gank.mvp.model.GankModel;
import com.smarthane.admiralcomponent.gank.mvp.model.entity.GankBaseResponse;
import com.smarthane.admiralcomponent.gank.mvp.model.entity.GankItemBean;
import com.smarthane.admiralcomponent.gank.mvp.ui.adapter.GankHomeAdapter;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Action;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;
import com.smarthane.admiral.core.base.rx.errorhandler.ErrorHandleSubscriber;
import com.smarthane.admiral.core.base.rx.errorhandler.RetryWithDelay;

import static com.smarthane.admiralcomponent.gank.app.GankConstants.NUMBER_OF_PAGE;

/**
 * @author smarthane
 * @time 2019/11/10 17:19
 * @describe
 */
public class GankHomePresenter extends BasePresenter<GankHomeContract.Model, GankHomeContract.View> {


    private GankHomeAdapter gankHomeAdapter;
    private int lastPage = 3;
    private int preEndIndex;

    private GankHomePresenter(GankHomeContract.Model model, GankHomeContract.View rootView) {
        super(model, rootView);
    }

    public static GankHomePresenter build(GankHomeContract.View rootView) {
        return new GankHomePresenter(new GankModel(), rootView);
    }

    public void setGankHomeAdapter(GankHomeAdapter gankHomeAdapter) {
        this.gankHomeAdapter = gankHomeAdapter;
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    void onCreate() {
        LogUtils.debugInfo("打开 App 时自动加载列表");
        // 打开 App 时自动加载列表
        requestGirls(true);
    }

    public void requestGirls(final boolean pullToRefresh) {
        if (pullToRefresh) {
            //下拉刷新默认只请求第一页
            lastPage = 3;
        }

        mModel.getGirlList(NUMBER_OF_PAGE, lastPage)
                .subscribeOn(Schedulers.io())
                // 遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
                .retryWhen(new RetryWithDelay(3, 2))
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        if (pullToRefresh) {
                            // 显示下拉刷新的进度条
                            mRootView.showLoading();
                        } else {
                            // 显示上拉加载更多的进度条
                            mRootView.startLoadMore();
                        }
                    }
                })
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        if (pullToRefresh) {
                            // 隐藏下拉刷新的进度条
                            mRootView.hideLoading();
                        } else {
                            // 隐藏上拉加载更多的进度条
                            mRootView.endLoadMore();
                        }
                    }
                })
                // 使用 Rxlifecycle,使 Disposable 和 Activity 一起销毁
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))
                .subscribe(new ErrorHandleSubscriber<GankBaseResponse<List<GankItemBean>>>(AppComponent
                        .get()
                        .fetchRxErrorHandler()) {
                    @Override
                    public void onNext(GankBaseResponse<List<GankItemBean>> datas) {
                        lastPage = lastPage + 1;
                        if (pullToRefresh) {
                            gankHomeAdapter.setNewData(datas.getResults());
                        } else {
                            gankHomeAdapter.addData(datas.getResults());
                        }
                    }
                });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        gankHomeAdapter = null;
    }
}
