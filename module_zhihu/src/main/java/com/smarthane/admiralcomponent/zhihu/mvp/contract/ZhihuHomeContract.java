package com.smarthane.admiralcomponent.zhihu.mvp.contract;

import com.smarthane.admiral.core.mvp.IModel;
import com.smarthane.admiral.core.mvp.IView;
import com.smarthane.admiralcomponent.zhihu.mvp.model.entity.DailyListBean;

import io.reactivex.rxjava3.core.Observable;

/**
 * @author smarthane
 * @time 2019/11/10 18:37
 * @describe
 */
public interface ZhihuHomeContract {
    /**
     * 对于经常使用的关于UI的方法可以定义到IView中,如显示隐藏进度条,和显示文字消息
     */
    interface View extends IView {
    }

    /**
     * Model层定义接口,外部只需关心Model返回的数据,无需关心内部细节,如是否使用缓存
     */
    interface Model extends IModel {
        Observable<DailyListBean> getDailyList();
    }
}
