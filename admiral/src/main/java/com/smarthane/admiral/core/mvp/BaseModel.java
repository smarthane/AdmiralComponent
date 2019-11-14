package com.smarthane.admiral.core.mvp;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.OnLifecycleEvent;

import com.smarthane.admiral.core.base.AppComponent;
import com.smarthane.admiral.core.integration.IRepositoryManager;

/**
 * @author smarthane
 * @time 2019/10/20 17:38
 * @describe
 */
public class BaseModel implements IModel, LifecycleObserver {

    /**
     * 用于管理网络请求层, 以及数据缓存层
     */
    protected IRepositoryManager mRepositoryManager;

    public BaseModel() {
        this.mRepositoryManager = AppComponent.get().fetchRepositoryManager();
    }

    /**
     * 在框架中 {BasePresenter#onDestroy()} 时会默认调用 {IModel#onDestroy()}
     */
    @Override
    public void onDestroy() {
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    void onDestroy(LifecycleOwner owner) {
        owner.getLifecycle().removeObserver(this);
    }
}
