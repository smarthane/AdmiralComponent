package com.smarthane.admiral.core.mvp;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.OnLifecycleEvent;

import com.smarthane.admiral.core.util.PreconditionUtils;

/**
 * @author smarthane
 * @time 2019/10/20 17:38
 * @describe
 */
public class BasePresenter<M extends IModel, V extends IView> implements IPresenter, LifecycleObserver {

    protected M mModel;
    protected V mRootView;

    /**
     * 如果当前页面同时需要 Model 层和 View 层,则使用此构造函数(默认)
     *
     * @param model
     * @param rootView
     */
    public BasePresenter(M model, V rootView) {
        PreconditionUtils.checkNotNull(model, "%s cannot be null", IModel.class.getName());
        PreconditionUtils.checkNotNull(rootView, "%s cannot be null", IView.class.getName());
        this.mModel = model;
        this.mRootView = rootView;
        onStart();
    }

    /**
     * 如果当前页面不需要操作数据,只需要 View 层,则使用此构造函数
     *
     * @param rootView
     */
    public BasePresenter(V rootView) {
        PreconditionUtils.checkNotNull(rootView, "%s cannot be null", IView.class.getName());
        this.mRootView = rootView;
        onStart();
    }

    public BasePresenter() {
        onStart();
    }

    @Override
    public void onStart() {
        // 将 LifecycleObserver 注册给 LifecycleOwner 后 @OnLifecycleEvent 才可以正常使用
        if (mRootView != null && mRootView instanceof LifecycleOwner) {
            ((LifecycleOwner) mRootView).getLifecycle().addObserver(this);
            if (mModel!= null && mModel instanceof LifecycleObserver){
                ((LifecycleOwner) mRootView).getLifecycle().addObserver((LifecycleObserver) mModel);
            }
        }
    }

    /**
     * 在框架中 {Activity#onDestroy()} 时会默认调用 {IPresenter#onDestroy()}
     */
    @Override
    public void onDestroy() {
        if (mModel != null) {
            mModel.onDestroy();
        }
        this.mModel = null;
        this.mRootView = null;
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    void onDestroy(LifecycleOwner owner) {
        owner.getLifecycle().removeObserver(this);
    }

}
