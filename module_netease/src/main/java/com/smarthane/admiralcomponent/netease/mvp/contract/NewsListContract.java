package com.smarthane.admiralcomponent.netease.mvp.contract;

import com.smarthane.admiral.core.mvp.IModel;
import com.smarthane.admiral.core.mvp.IView;
import com.smarthane.admiralcomponent.netease.mvp.model.entity.NewsSummary;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;

/**
 * @author smarthane
 * @time 2019/11/10 18:03
 * @describe
 */
public interface NewsListContract {

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
        // 请求获取新闻
        Observable<Map<String, List<NewsSummary>>> getNewsListData(String type, final String id, int startPage);
    }
}

