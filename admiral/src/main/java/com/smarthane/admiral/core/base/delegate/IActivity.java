package com.smarthane.admiral.core.base.delegate;

import android.app.Activity;

import androidx.annotation.NonNull;

import com.smarthane.admiral.core.integration.cache.Cache;

/**
 * @author smarthane
 * @time 2019/10/27 10:50
 * @describe
 */
public interface IActivity {

    /**
     * 提供在 {Activity} 生命周期内的缓存容器, 可向此 {Activity} 存取一些必要的数据
     * 此缓存容器和 {Activity} 的生命周期绑定, 如果 {Activity} 在屏幕旋转或者配置更改的情况下
     * 重新创建, 那此缓存容器中的数据也会被清空, 如果你想避免此种情况请使用 <a href="https://github.com/JessYanCoding/LifecycleModel">LifecycleModel</a>
     *
     * @return like {LruCache}
     */
    @NonNull
    Cache<String, Object> provideCache();

    /**
     * 这个 Activity 是否会使用 Fragment,框架会根据这个属性判断是否注册 {FragmentManager.FragmentLifecycleCallbacks}
     * 如果返回{@code false},那意味着这个 Activity 不需要绑定 Fragment,那你再在这个 Activity 中绑定继承于 {BaseFragment} 的 Fragment 将不起任何作用
     * ActivityLifecycle#registerFragmentCallbacks (Fragment 的注册过程)
     * @return
     */
    boolean useFragment();

}
