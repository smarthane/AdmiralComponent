package com.smarthane.admiral.component.common.sdk.http.eapi;

/**
 * @author smarthane
 * @time 2019/11/10 13:13
 * @describe
 */
public class EapiGlobalConfig {

    /**
     * 默认重试次数
     */
    public static final int DEFAULT_RETRY_COUNT = 0;
    /**
     * 默认重试间隔时间（毫秒）
     */
    public static final int DEFAULT_RETRY_DELAY_MILLIS = 1000;

    /**
     * 是否开启Eapi
     */
    protected boolean enableEapi = true;

    /**
     * 基础域名
     */
    protected String baseUrl;

    /**
     * 请求失败重试间隔时间
     */
    protected int retryDelayMillis;

    /**
     * 重试次数
     */
    protected int retryCount;

    private EapiGlobalConfig() {
    }

    private static class Holder {
        private static EapiGlobalConfig sInstance = new EapiGlobalConfig();
    }

    public static EapiGlobalConfig get() {
        return Holder.sInstance;
    }

    /**
     * 是否开启Eapi
     *
     * @param enableEapi
     * @return
     */
    public EapiGlobalConfig enableEapi(boolean enableEapi) {
        this.enableEapi = enableEapi;
        return this;
    }

    /**
     * 设置请求baseUrl
     *
     * @param baseUrl
     * @return
     */
    public EapiGlobalConfig baseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
        return this;
    }

    /**
     * 设置请求失败重试间隔时间
     *
     * @param retryDelayMillis
     * @return
     */
    public EapiGlobalConfig retryDelayMillis(int retryDelayMillis) {
        this.retryDelayMillis = retryDelayMillis;
        return this;
    }

    /**
     * 设置请求失败重试次数
     *
     * @param retryCount
     * @return
     */
    public EapiGlobalConfig retryCount(int retryCount) {
        this.retryCount = retryCount;
        return this;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public int getRetryDelayMillis() {
        if (retryDelayMillis < 0) {
            retryDelayMillis = DEFAULT_RETRY_DELAY_MILLIS;
        }
        return retryDelayMillis;
    }

    public int getRetryCount() {
        if (retryCount < 0) {
            retryCount = DEFAULT_RETRY_COUNT;
        }
        return retryCount;
    }

}
