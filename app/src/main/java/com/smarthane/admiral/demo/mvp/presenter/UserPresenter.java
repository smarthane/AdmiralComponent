package com.smarthane.admiral.demo.mvp.presenter;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.OnLifecycleEvent;

import com.smarthane.admiral.core.base.AppComponent;
import com.smarthane.admiral.core.mvp.BasePresenter;
import com.smarthane.admiral.core.util.LogUtils;
import com.smarthane.admiral.core.util.RxLifecycleUtils;
import com.smarthane.admiral.demo.mvp.contract.UserContract;
import com.smarthane.admiral.demo.mvp.model.UserModel;
import com.smarthane.admiral.demo.mvp.model.entity.User;

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
 * @time 2019/10/27 16:15
 * @describe
 */
public class UserPresenter extends BasePresenter<UserContract.Model, UserContract.View> {

    private UserPresenter(UserContract.Model model, UserContract.View rootView) {
        super(model, rootView);
    }

    public static UserPresenter build(UserContract.View rootView) {
        return new UserPresenter(new UserModel(), rootView);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    void onCreate() {
        LogUtils.debugInfo("打开 App 时自动加载列表");
        requestFromModel();
    }

    public void requestFromModel() {
        mModel.getUsers(1, false)
                .subscribeOn(Schedulers.io())
                .retryWhen(new RetryWithDelay(3, 2))
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        mRootView.showLoading();
                    }
                }).subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        mRootView.hideLoading();
                    }
                })
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))
                .subscribe(new ErrorHandleSubscriber<List<User>>(AppComponent.get().fetchRxErrorHandler()) {
                    @Override
                    public void onNext(List<User> users) {
                        mRootView.shonContent(users);
                    }

                    @Override
                    public void onError(Throwable t) {
                        super.onError(t);
                    }
                });
    }
}
