package com.smarthane.admiral.component.common.sdk.http.eapi;

/**
 * @author smarthane
 * @time 2019/11/10 13:28
 * @describe 下载请求回调
 */
public abstract class EapiDownloadCallback {

    /**
     * 开始
     */
    public void onStart() {
        // do nothing
    }

    /**
     * 进度
     * @param read
     * @param count
     * @param done
     */
    public void onProgress(long read, long count, boolean done) {
        // do nothing
    }

    /**
     * 进度百分比
     * @param percent
     */
    public void onProgress(int percent) {
        // do nothing
    }

    /**
     * 下载完成
     */
    public abstract void onComplete(String result);

    /**
     * 下载失败
     * @param t
     */
    public abstract void onFail(Throwable t);
}
