package com.smarthane.admiral.demo.mvp.contract;

import com.smarthane.admiral.core.mvp.IModel;
import com.smarthane.admiral.core.mvp.IView;
import com.smarthane.admiral.demo.mvp.model.entity.User;

import java.util.List;

import io.reactivex.Observable;

/**
 * @author smarthane
 * @time 2019/10/27 16:06
 * @describe
 */
public interface UserContract {

    interface View extends IView {
        void shonContent(List<User> userList);
    }

    interface Model extends IModel {
        Observable<List<User>> getUsers(int lastIdQueried, boolean update);
    }
}
