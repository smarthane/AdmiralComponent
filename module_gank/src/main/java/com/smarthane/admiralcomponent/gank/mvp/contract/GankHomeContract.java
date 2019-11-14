package com.smarthane.admiralcomponent.gank.mvp.contract;

import android.app.Activity;

import com.smarthane.admiral.core.mvp.IModel;
import com.smarthane.admiral.core.mvp.IView;
import com.smarthane.admiralcomponent.gank.mvp.model.entity.GankBaseResponse;
import com.smarthane.admiralcomponent.gank.mvp.model.entity.GankItemBean;

import java.util.List;

import io.reactivex.Observable;

/**
 * @author smarthane
 * @time 2019/11/10 17:14
 * @describe 展示 Contract 的用法
 */
public interface GankHomeContract {

    /**
     * 对于经常使用的关于UI的方法可以定义到IView中,如显示隐藏进度条,和显示文字消息
     */
    interface View extends IView {
        void startLoadMore();
        void endLoadMore();
    }

    /**
     * Model层定义接口,外部只需关心Model返回的数据,无需关心内部细节,如是否使用缓存
     */
    interface Model extends IModel {
        Observable<GankBaseResponse<List<GankItemBean>>> getGirlList(int num, int page);
    }

}
