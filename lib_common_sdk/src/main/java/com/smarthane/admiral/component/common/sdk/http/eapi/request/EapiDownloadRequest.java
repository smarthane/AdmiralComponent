package com.smarthane.admiral.component.common.sdk.http.eapi.request;

import android.content.Context;
import android.text.TextUtils;

import com.smarthane.admiral.core.base.AppComponent;

import java.io.File;

/**
 * @author smarthane
 * @time 2019/11/10 13:23
 * @describe 下载请求
 */
public class EapiDownloadRequest extends EapiBaseRequest {

    public static final String EAPI_FILE_CACHE = "eapi_file_cache";

    /**
     * 下载url
     */
    private String fileUrl;

    /**
     * 文件名称
     */
    private String fileName;

    /**
     * 存储位置
     */
    private String savePath;

    /**
     * 文件总长度
     */
    private long countLength;

    /**
     * 下载长度
     */
    private long readLength;

    /**
     * 是否需要回调进度
     */
    private boolean enableProgres = false;

    public EapiDownloadRequest(Context mContext) {
        super(mContext);
    }

    @Override
    public String getUrl() {
        return fileUrl;
    }

    @Override
    public String getSuffixUrl() {
        return null;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public long getCountLength() {
        return countLength;
    }

    public void setCountLength(long countLength) {
        this.countLength = countLength;
    }

    public long getReadLength() {
        return readLength;
    }

    public void setReadLength(long readLength) {
        this.readLength = readLength;
    }

    public String getSavePath() {
        if (TextUtils.isEmpty(savePath)) {
            if (TextUtils.isEmpty(fileName)) {
                fileName = fileUrl.substring(fileUrl.lastIndexOf("/") + 1);
            }
            savePath = AppComponent
                    .get()
                    .fetchGlobalConfigModule()
                    .provideCacheFile()
                    .getAbsolutePath() + File.separator + EAPI_FILE_CACHE + File.separator + fileName;
        }
        return savePath;
    }

    public void setSavePath(String savePath) {
        this.savePath = savePath;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
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
