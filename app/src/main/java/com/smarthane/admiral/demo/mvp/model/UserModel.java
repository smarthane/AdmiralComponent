package com.smarthane.admiral.demo.mvp.model;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.OnLifecycleEvent;

import com.smarthane.admiral.core.base.AppComponent;
import com.smarthane.admiral.core.mvp.BaseModel;
import com.smarthane.admiral.core.util.LogUtils;
import com.smarthane.admiral.demo.mvp.contract.UserContract;
import com.smarthane.admiral.demo.mvp.model.api.service.UserService;
import com.smarthane.admiral.demo.mvp.model.entity.User;

import java.util.List;

import io.reactivex.Observable;

public class UserModel extends BaseModel implements UserContract.Model {

    public static final int USERS_PER_PAGE = 10;

    public UserModel() {
    }

    @Override
    public Observable<List<User>> getUsers(int lastIdQueried, boolean update) {
        // 使用rxcache缓存,上拉刷新则不读取缓存,加载更多读取缓存
        return AppComponent.get().fetchRepositoryManager()
                .obtainRetrofitService(UserService.class)
                .getUsers(lastIdQueried, USERS_PER_PAGE);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    void onPause() {
        LogUtils.debugInfo("Release Resource");
    }
}
