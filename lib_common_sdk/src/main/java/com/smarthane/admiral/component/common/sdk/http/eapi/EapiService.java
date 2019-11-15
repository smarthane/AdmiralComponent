package com.smarthane.admiral.component.common.sdk.http.eapi;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.QueryMap;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * @author smarthane
 * @time 2019/11/10 13:49
 * @describe 提供简单网络请求的通用接口
 */
public interface EapiService {


    /**
     * GET 请求
     *
     * @param url
     * @param params
     * @return
     */
    @GET()
    Observable<ResponseBody> get(
            @Url String url,
            @QueryMap Map<String, Object> params
    );

    /**
     * POST 请求
     *
     * @param url
     * @param params
     * @return
     */
    @FormUrlEncoded
    @POST()
    Observable<ResponseBody> post(
            @Url() String url,
            @FieldMap Map<String, Object> params
    );

    /**
     * 下载
     *
     * @param url
     * @param params
     * @return
     */
    @Streaming
    @GET()
    Observable<ResponseBody> downloadFile(
            @Url() String url,
            @QueryMap Map<String, Object> params
    );

    /**
     * 上传
     *
     * @param url
     * @param parts
     * @return
     */
    @Multipart
    @POST()
    Observable<ResponseBody> uploadFiles(
            @Url() String url,
            @Part() List<MultipartBody.Part> parts
    );

}
