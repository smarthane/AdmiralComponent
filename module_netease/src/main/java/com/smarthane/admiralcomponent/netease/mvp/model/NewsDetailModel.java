package com.smarthane.admiralcomponent.netease.mvp.model;

import com.smarthane.admiral.core.mvp.BaseModel;
import com.smarthane.admiralcomponent.netease.mvp.contract.NewsDetailContract;
import com.smarthane.admiralcomponent.netease.mvp.model.api.Api;
import com.smarthane.admiralcomponent.netease.mvp.model.api.service.NewsService;
import com.smarthane.admiralcomponent.netease.mvp.model.entity.NewsDetail;

import java.util.Map;

import io.reactivex.Observable;

/**
 * @author smarthane
 * @time 2019/11/10 14:18
 * @describe
 */
public class NewsDetailModel extends BaseModel implements NewsDetailContract.Model {

    public NewsDetailModel() {
        super();
    }

    @Override
    public Observable<Map<String, NewsDetail>> getOneNewsData(String postId) {
        return mRepositoryManager
                .obtainRetrofitService(NewsService.class)
                .getNewDetail(Api.CACHE_CONTROL_AGE, postId);
    }
}
