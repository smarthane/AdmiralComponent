package com.smarthane.admiral.component.common.sdk.http.eapi;

/**
 * @author smarthane
 * @time 2019/11/10 13:46
 * @describe 简单的网络请求工具
 */
public class EasyApiHelper {


    private EasyApiHelper() {
    }

    private static class Holder {
        private static EasyApiHelper sInstance = new EasyApiHelper();
    }

    public static EasyApiHelper get() {
        return Holder.sInstance;
    }

    // TODO 封装简单网络请求 让网络操作更Easy
}
