package com.smarthane.admiralcomponent.netease.mvp.contract;

import com.smarthane.admiral.core.mvp.IModel;
import com.smarthane.admiral.core.mvp.IView;
import com.smarthane.admiralcomponent.netease.mvp.model.entity.NewsDetail;

import java.util.Map;

import io.reactivex.rxjava3.core.Observable;

/**
 * @author smarthane
 * @time 2019/11/10 14:15
 * @describe
 */
public interface NewsDetailContract {

    /**
     * 对于经常使用的关于UI的方法可以定义到IView中,如显示隐藏进度条,和显示文字消息
     */
    interface View extends IView {
        // 返回获取的新闻
        void setOneNewsData(NewsDetail newsDetail);
    }

    /**
     * Model层定义接口,外部只需关心Model返回的数据,无需关心内部细节,如是否使用缓存
     */
    interface Model extends IModel {
        // 请求获取新闻
        Observable<Map<String, NewsDetail>> getOneNewsData(String postId);
    }

}
