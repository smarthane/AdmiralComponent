package com.smarthane.admiral.core.base;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.smarthane.admiral.core.base.delegate.IFragment;
import com.smarthane.admiral.core.integration.cache.Cache;
import com.smarthane.admiral.core.integration.cache.CacheType;
import com.smarthane.admiral.core.integration.lifecycle.FragmentLifecycleable;
import com.smarthane.admiral.core.mvp.IPresenter;
import com.trello.rxlifecycle2.android.FragmentEvent;

import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.Subject;

/**
 * @author smarthane
 * @time 2019/10/20 10:19
 * @describe
 */
public abstract class BaseFragment<P extends IPresenter> extends Fragment implements IFragment, FragmentLifecycleable {

    protected final String TAG = this.getClass().getSimpleName();
    private final BehaviorSubject<FragmentEvent> mLifecycleSubject = BehaviorSubject.create();
    private Cache<String, Object> mCache;
    protected Context mContext;

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
                    .build(CacheType.FRAGMENT_CACHE);
        }
        return mCache;
    }

    @NonNull
    @Override
    public final Subject<FragmentEvent> provideLifecycleSubject() {
        return mLifecycleSubject;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            // 释放资源
            mPresenter.onDestroy();
        }
        mPresenter = null;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mContext = null;
    }

    /**
     * 获取布局文件
     * @return
     */
    public abstract int setLayoutResouceId();

    /**
     * 初始化数据
     */
    protected void initData(Bundle arguments) {
    }

    /**
     * 简单页面无需mvp就不用管此方法即可,完美兼容各种实际场景的变通
     */
    protected void initPresenter() {
    }

    /**
     * 初始化view
     */
    protected void initView() {
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


    protected View mContextView = null;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        mContextView = inflater.inflate(
                setLayoutResouceId(),
                container,
                false);
        initData(getArguments());
        initView();
        bindListener();
        initPresenter();
        execBusiness();
        return mContextView;
    }

    protected <T extends View> T findViewById(int id) {
        if (mContextView == null) {
            return null;
        }
        return (T) mContextView.findViewById(id);
    }

}
