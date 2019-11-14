package com.smarthane.admiral.core.base;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.smarthane.admiral.core.base.delegate.IActivity;
import com.smarthane.admiral.core.integration.cache.Cache;
import com.smarthane.admiral.core.integration.cache.CacheType;
import com.smarthane.admiral.core.integration.lifecycle.ActivityLifecycleable;
import com.smarthane.admiral.core.mvp.IPresenter;
import com.trello.rxlifecycle2.android.ActivityEvent;

import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.Subject;

/**
 * @author smarthane
 * @time 2019/10/20 10:19
 * @describe
 */
public abstract class BaseActivity<P extends IPresenter> extends AppCompatActivity implements IActivity, ActivityLifecycleable {

    protected final String TAG = this.getClass().getSimpleName();
    private final BehaviorSubject<ActivityEvent> mLifecycleSubject = BehaviorSubject.create();
    private Cache<String, Object> mCache;

    /**
     * 如果当前页面逻辑简单, Presenter 可以为 null
     */
    @Nullable
    protected P mPresenter;

    @NonNull
    @Override
    public synchronized Cache<String, Object> provideCache() {
        if (mCache == null) {
            mCache = AppComponent
                    .get()
                    .fetchCacheFactory()
                    .build(CacheType.ACTIVITY_CACHE);
        }
        return mCache;
    }

    @NonNull
    @Override
    public final Subject<ActivityEvent> provideLifecycleSubject() {
        return mLifecycleSubject;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            // 释放资源
            mPresenter.onDestroy();
        }
        mPresenter = null;
    }

    @Override
    public boolean useFragment() {
        return true;
    }

    /**
     * 获取布局文件
     * @return
     */
    public abstract int setLayoutResouceId();

    /**
     * 初始化数据
     */
    protected void initData() {
    }

    /**
     * 简单页面无需mvp就不用管此方法即可,完美兼容各种实际场景的变通
     */
    protected void initPresenter() {
    }

    /**
     * 初始化view
     */
    protected void initView(Bundle savedInstanceState) {
    }

    /**
     * 初始化Listener
     */
    protected void bindListener() {
    }

    /**
     * 执行业务逻辑
     */
    protected void execBusiness() {
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(setLayoutResouceId());
        initData();
        initView(savedInstanceState);
        bindListener();
        initPresenter();
        execBusiness();
    }
}
