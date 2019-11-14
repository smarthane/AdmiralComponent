package com.smarthane.admiral.core.mvp;

/**
 * @author smarthane
 * @time 2019/10/20 17:37
 * @describe
 */
public interface IModel {

    /**
     * 在框架中 {BasePresenter#onDestroy()} 时会默认调用 {IModel#onDestroy()}
     */
    void onDestroy();
}
