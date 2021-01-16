package com.smarthane.admiralcomponent.netease.mvp.model.api.service;

import com.smarthane.admiralcomponent.netease.mvp.model.api.Api;
import com.smarthane.admiralcomponent.netease.mvp.model.entity.NewsDetail;
import com.smarthane.admiralcomponent.netease.mvp.model.entity.NewsSummary;

import java.util.List;
import java.util.Map;

import io.reactivex.rxjava3.core.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Url;

import static com.smarthane.admiral.core.http.retrofiturlmanager.RetrofitUrlManager.DOMAIN_NAME_HEADER;

/**
 * @author smarthane
 * @time 2019/11/10 17:59
 * @describe
 */
public interface NewsService {


    @Headers({DOMAIN_NAME_HEADER + Api.NETEASE_DOMAIN_NAME})
    @GET("/nc/article/{type}/{id}/{startPage}-20.html")
    Observable<Map<String, List<NewsSummary>>> getNewsList(
            @Header("Cache-Control") String cacheControl,
            @Path("type") String type,
            @Path("id") String id,
            @Path("startPage") int startPage);

    @Headers({DOMAIN_NAME_HEADER + Api.NETEASE_DOMAIN_NAME})
    @GET("/nc/article/{postId}/full.html")
    Observable<Map<String, NewsDetail>> getNewDetail(
            @Header("Cache-Control") String cacheControl,
            @Path("postId") String postId);

    @GET
    Observable<ResponseBody> getNewsBodyHtmlPhoto(@Url String photoPath);
}
