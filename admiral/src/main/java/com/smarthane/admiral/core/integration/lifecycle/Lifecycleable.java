package com.smarthane.admiral.core.integration.lifecycle;

import android.app.Activity;

import androidx.annotation.NonNull;

import com.trello.rxlifecycle4.RxLifecycle;

import io.reactivex.rxjava3.subjects.Subject;

/**
 * @author smarthane
 * @time 2019/10/27 15:52
 * @describe 让 {@link Activity}/{link @Fragment} 实现此接口,即可正常使用 {@link RxLifecycle}
 * 无需再继承 {@link RxLifecycle} 提供的 Activity/Fragment ,扩展性极强
 */
public interface Lifecycleable<E> {
    @NonNull
    Subject<E> provideLifecycleSubject();
}
