package com.smarthane.admiralcomponent.netease.mvp.model;

import com.smarthane.admiral.core.mvp.BaseModel;
import com.smarthane.admiralcomponent.netease.mvp.contract.NewsListContract;
import com.smarthane.admiralcomponent.netease.mvp.model.api.Api;
import com.smarthane.admiralcomponent.netease.mvp.model.api.service.NewsService;
import com.smarthane.admiralcomponent.netease.mvp.model.entity.NewsSummary;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;

/**
 * @author smarthane
 * @time 2019/11/10 18:05
 * @describe
 */
public class NewsListModel extends BaseModel implements NewsListContract.Model {

    public NewsListModel() {
        super();
    }

    @Override
    public Observable<Map<String, List<NewsSummary>>> getNewsListData(String type, String id, int startPage) {
        return mRepositoryManager
                .obtainRetrofitService(NewsService.class)
                .getNewsList(Api.CACHE_CONTROL_AGE, type, id, startPage);
    }
}
