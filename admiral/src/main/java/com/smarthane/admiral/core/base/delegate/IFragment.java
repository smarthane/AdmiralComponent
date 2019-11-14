package com.smarthane.admiral.core.base.delegate;

import androidx.annotation.NonNull;

import com.smarthane.admiral.core.integration.cache.Cache;

/**
 * @author smarthane
 * @time 2019/10/27 10:51
 * @describe
 */
public interface IFragment {

    /**
     * 提供在 {Fragment} 生命周期内的缓存容器, 可向此 {Fragment} 存取一些必要的数据
     * 此缓存容器和 {Fragment} 的生命周期绑定, 如果 {Fragment} 在屏幕旋转或者配置更改的情况下
     * 重新创建, 那此缓存容器中的数据也会被清空, 如果你想避免此种情况请使用 <a href="https://github.com/JessYanCoding/LifecycleModel">LifecycleModel</a>
     *
     * @return like {LruCache}
     */
    @NonNull
    Cache<String, Object> provideCache();
    
}
