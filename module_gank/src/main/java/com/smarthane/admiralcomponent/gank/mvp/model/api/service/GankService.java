package com.smarthane.admiralcomponent.gank.mvp.model.api.service;

import com.smarthane.admiralcomponent.gank.mvp.model.entity.GankBaseResponse;
import com.smarthane.admiralcomponent.gank.mvp.model.entity.GankItemBean;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;

import static com.smarthane.admiralcomponent.gank.mvp.model.api.Api.GANK_DOMAIN_NAME;
import static com.smarthane.admiral.core.http.retrofiturlmanager.RetrofitUrlManager.DOMAIN_NAME_HEADER;

/**
 * @author smarthane
 * @time 2019/11/10 16:51
 * @describe 展示 {@link Retrofit#create(Class)} 中需要传入的 ApiService 的使用方式
 *  * 存放关于 gank 的一些 API
 */
public interface GankService {
    /**
     * 妹纸列表
     */
    @Headers({DOMAIN_NAME_HEADER + GANK_DOMAIN_NAME})
    @GET("/api/data/福利/{num}/{page}")
    Observable<GankBaseResponse<List<GankItemBean>>> getGirlList(@Path("num") int num, @Path("page") int page);
}
