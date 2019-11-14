package com.smarthane.admiralcomponent.zhihu.mvp.model;

import com.smarthane.admiral.core.mvp.BaseModel;
import com.smarthane.admiralcomponent.zhihu.mvp.contract.DetailContract;
import com.smarthane.admiralcomponent.zhihu.mvp.contract.ZhihuHomeContract;
import com.smarthane.admiralcomponent.zhihu.mvp.model.api.service.ZhihuService;
import com.smarthane.admiralcomponent.zhihu.mvp.model.entity.DailyListBean;
import com.smarthane.admiralcomponent.zhihu.mvp.model.entity.ZhihuDetailBean;

import io.reactivex.Observable;

/**
 * @author smarthane
 * @time 2019/11/10 9:40
 * @describe
 */
public class ZhihuModel extends BaseModel implements ZhihuHomeContract.Model, DetailContract.Model {

    public ZhihuModel() {
        super();
    }

    @Override
    public Observable<ZhihuDetailBean> getDetailInfo(int id) {
        return mRepositoryManager
                .obtainRetrofitService(ZhihuService.class)
                .getDetailInfo(id);
    }

    @Override
    public Observable<DailyListBean> getDailyList() {
        return mRepositoryManager.obtainRetrofitService(ZhihuService.class)
                .getDailyList();
    }
}
