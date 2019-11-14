package com.smarthane.admiral.component.common.sdk.http.progressmanager;

/**
 * @author smarthane
 * @time 2019/11/10 15:14
 * @describe
 */
public interface ProgressListener {

    /**
     * 进度监听
     *
     * @param progressInfo 关于进度的所有信息
     */
    void onProgress(ProgressInfo progressInfo);

    /**
     * 错误监听
     *
     * @param id 进度信息的 id
     * @param e  错误
     */
    void onError(long id, Exception e);
}
