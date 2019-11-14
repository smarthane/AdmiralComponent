package com.smarthane.admiralcomponent.gank.mvp.model;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.OnLifecycleEvent;

import com.smarthane.admiral.core.mvp.BaseModel;
import com.smarthane.admiral.core.util.LogUtils;
import com.smarthane.admiralcomponent.gank.mvp.contract.GankHomeContract;
import com.smarthane.admiralcomponent.gank.mvp.model.api.service.GankService;
import com.smarthane.admiralcomponent.gank.mvp.model.entity.GankBaseResponse;
import com.smarthane.admiralcomponent.gank.mvp.model.entity.GankItemBean;

import java.util.List;

import io.reactivex.Observable;

/**
 * @author smarthane
 * @time 2019/11/10 17:16
 * @describe 展示 Model 的用法
 */
public class GankModel extends BaseModel implements GankHomeContract.Model {

    public GankModel() {
        super();
    }

    @Override
    public Observable<GankBaseResponse<List<GankItemBean>>> getGirlList(int num, int page) {
        return mRepositoryManager
                .obtainRetrofitService(GankService.class)
                .getGirlList(num, page);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    void onPause() {
        LogUtils.debugInfo("Release Resource");
    }
}
