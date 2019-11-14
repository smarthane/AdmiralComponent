package com.smarthane.admiralcomponent.netease.mvp.contract;

import com.smarthane.admiral.core.mvp.IModel;
import com.smarthane.admiral.core.mvp.IView;
import com.smarthane.admiralcomponent.netease.mvp.model.entity.NewsChannelTable;

import java.util.List;

import io.reactivex.Observable;

/**
 * @author smarthane
 * @time 2019/11/10 16:51
 * @describe
 */
public interface NewsMainContract {

    /**
     * 对于经常使用的关于UI的方法可以定义到IView中,如显示隐藏进度条,和显示文字消息
     */
    interface View extends IView {
        void setMineNewsChannels(List<NewsChannelTable> newsChannelsMine);
    }

    /**
     * Model层定义接口,外部只需关心Model返回的数据,无需关心内部细节,如是否使用缓存
     */
    interface Model extends IModel {
        Observable<List<NewsChannelTable>> lodeMineNewsChannels();
    }
}
