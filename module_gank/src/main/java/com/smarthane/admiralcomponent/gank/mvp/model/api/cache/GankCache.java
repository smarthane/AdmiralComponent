package com.smarthane.admiralcomponent.gank.mvp.model.api.cache;

import com.smarthane.admiralcomponent.gank.mvp.model.entity.GankBaseResponse;
import com.smarthane.admiralcomponent.gank.mvp.model.entity.GankItemBean;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.core.Observable;
import io.rx_cache3.runtime.LifeCache;

/**
 * @author smarthane
 * @time 2019/11/10 13:05
 * @describe
 */
public interface GankCache {

    @LifeCache(duration = 10, timeUnit = TimeUnit.SECONDS)
    Observable<GankBaseResponse<List<GankItemBean>>> getGirlList(Observable<GankBaseResponse<List<GankItemBean>>> girlList);
}
