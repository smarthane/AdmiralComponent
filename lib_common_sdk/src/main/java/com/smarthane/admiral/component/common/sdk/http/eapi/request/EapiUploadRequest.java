package com.smarthane.admiral.component.common.sdk.http.eapi.request;

import android.content.Context;

import com.smarthane.admiral.component.common.sdk.http.eapi.EapiUploadCallback;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * @author smarthane
 * @time 2019/11/10 10:51
 * @describe 上传文件请求
 * TODO 支持多文件上传
 */
public class EapiUploadRequest extends EapiBaseRequest {

    private String uploadUrl;
    private File uploadFile;
    private EapiUploadCallback callback;

    /**
     * 是否需要回调进度
     */
    private boolean enableProgres = false;

    public EapiUploadRequest(Context mContext) {
        super(mContext);
    }

    @Override
    public String getSuffixUrl() {
        return null;
    }

    @Override
    public String getUrl() {
        return uploadUrl;
    }

    public void setUploadUrl(String uploadUrl) {
        this.uploadUrl = uploadUrl;
    }

    public void setUploadFile(File uploadFile) {
        this.uploadFile = uploadFile;
    }

    public List<MultipartBody.Part> getParts() {
        List<MultipartBody.Part> multipartBodyParts = new ArrayList<>();
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/octet-stream"), uploadFile);
        if (callback != null && isEnableProgres()) {
            EapiUploadRequestBody eapiUploadRequestBody = new EapiUploadRequestBody(requestBody, callback);
            MultipartBody.Part part = MultipartBody.Part.createFormData("file", uploadFile.getName(), eapiUploadRequestBody);
            multipartBodyParts.add(part);
        } else {
            MultipartBody.Part part = MultipartBody.Part.createFormData("file", uploadFile.getName(), requestBody);
            multipartBodyParts.add(part);
        }
        return multipartBodyParts;
    }

    public void setCallback(EapiUploadCallback callback) {
        this.callback = callback;
    }

    public boolean isEnableProgres() {
        return enableProgres;
    }

    public void enableProgres() {
        this.enableProgres = true;
    }

    public void disEnableProgres() {
        this.enableProgres = false;
    }
}
