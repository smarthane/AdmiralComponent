package com.smarthane.admiralcomponent.zhihu.mvp.model.api.service;

import com.smarthane.admiralcomponent.zhihu.mvp.model.api.Api;
import com.smarthane.admiralcomponent.zhihu.mvp.model.entity.DailyListBean;
import com.smarthane.admiralcomponent.zhihu.mvp.model.entity.ZhihuDetailBean;

import io.reactivex.Observable;
import com.smarthane.admiral.core.http.retrofiturlmanager.RetrofitUrlManager;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;


/**
 * @author smarthane
 * @time 2019/11/10 18:28
 * @describe
 */
public interface ZhihuService {
    /**
     * 最新日报
     */
    @Headers({RetrofitUrlManager.DOMAIN_NAME_HEADER + Api.ZHIHU_DOMAIN_NAME})
    @GET("/api/4/news/latest")
    Observable<DailyListBean> getDailyList();

    /**
     * 日报详情
     */
    @Headers({RetrofitUrlManager.DOMAIN_NAME_HEADER + Api.ZHIHU_DOMAIN_NAME})
    @GET("/api/4/news/{id}")
    Observable<ZhihuDetailBean> getDetailInfo(@Path("id") int id);
}
