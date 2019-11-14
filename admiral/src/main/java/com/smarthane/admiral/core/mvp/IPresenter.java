package com.smarthane.admiral.core.mvp;

/**
 * @author smarthane
 * @time 2019/10/20 17:37
 * @describe
 */
public interface IPresenter {

    /**
     * 做一些初始化操作
     */
    void onStart();

    /**
     * 在框架中 link Activity#onDestroy()时会默认调用 IPresenter#onDestroy()
     */
    void onDestroy();
}
