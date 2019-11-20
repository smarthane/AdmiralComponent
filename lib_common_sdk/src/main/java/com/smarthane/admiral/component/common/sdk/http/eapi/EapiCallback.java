package com.smarthane.admiral.component.common.sdk.http.eapi;

/**
 * @author smarthane
 * @time 2019/11/10 11:30
 * @describe
 */
public abstract class EapiCallback<T> {

    public void onStart() {
        // do nothing
    }

    public void onComplete() {
        // do nothing
    }

    public abstract void onSuccess(T data);

    public abstract void onFail(Throwable t);

}
