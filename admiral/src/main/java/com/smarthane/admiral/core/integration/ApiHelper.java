package com.smarthane.admiral.core.integration;

/**
 * @author smarthane
 * @time 2019/10/20 18:06
 * @describe 网络请求框架统一封装
 */
public class ApiHelper {

    private ApiHelper() {
    }

    private static class Holder {
        private static ApiHelper sInstance = new ApiHelper();
    }

    public static ApiHelper get() {
        return Holder.sInstance;
    }







}
