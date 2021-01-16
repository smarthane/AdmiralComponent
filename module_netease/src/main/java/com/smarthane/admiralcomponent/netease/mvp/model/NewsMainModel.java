package com.smarthane.admiralcomponent.netease.mvp.model;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.OnLifecycleEvent;

import com.smarthane.admiral.core.mvp.BaseModel;
import com.smarthane.admiral.core.util.LogUtils;
import com.smarthane.admiralcomponent.netease.mvp.contract.NewsMainContract;
import com.smarthane.admiralcomponent.netease.mvp.model.db.NewsChannelTableManager;
import com.smarthane.admiralcomponent.netease.mvp.model.entity.NewsChannelTable;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableEmitter;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;

/**
 * @author smarthane
 * @time 2019/11/10 16:55
 * @describe
 */
public class NewsMainModel extends BaseModel implements NewsMainContract.Model {

    public NewsMainModel() {
    }

    @Override
    public Observable<List<NewsChannelTable>> lodeMineNewsChannels() {
        return Observable.create(new ObservableOnSubscribe<List<NewsChannelTable>>() {
            @Override
            public void subscribe(ObservableEmitter<List<NewsChannelTable>> emitter) throws Exception {
                emitter.onNext(NewsChannelTableManager.loadNewsChannelsStatic());
                emitter.onComplete();
            }
        });
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    void onPause() {
        LogUtils.debugInfo("NewsMainModel Release Resource");
    }
}
