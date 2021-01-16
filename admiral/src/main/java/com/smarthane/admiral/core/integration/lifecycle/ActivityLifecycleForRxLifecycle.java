package com.smarthane.admiral.core.integration.lifecycle;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import androidx.fragment.app.FragmentActivity;

import com.trello.rxlifecycle4.android.ActivityEvent;

import io.reactivex.rxjava3.subjects.Subject;

/**
 * @author smarthane
 * @time 2019/10/27 16:00
 * @describe
 */
public class ActivityLifecycleForRxLifecycle implements Application.ActivityLifecycleCallbacks {

    private FragmentLifecycleForRxLifecycle mFragmentLifecycle;

    public ActivityLifecycleForRxLifecycle() {
    }

    /**
     * 通过桥梁对象 {@code BehaviorSubject<ActivityEvent> mLifecycleSubject}
     * 在每个 Activity 的生命周期中发出对应的生命周期事件
     */
    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        if (activity instanceof ActivityLifecycleable) {
            obtainSubject(activity).onNext(ActivityEvent.CREATE);
            if (activity instanceof FragmentActivity) {
                if (mFragmentLifecycle == null) {
                    mFragmentLifecycle = new FragmentLifecycleForRxLifecycle();
                }
                ((FragmentActivity) activity).getSupportFragmentManager().registerFragmentLifecycleCallbacks(mFragmentLifecycle, true);
            }
        }
    }

    @Override
    public void onActivityStarted(Activity activity) {
        if (activity instanceof ActivityLifecycleable) {
            obtainSubject(activity).onNext(ActivityEvent.START);
        }
    }

    @Override
    public void onActivityResumed(Activity activity) {
        if (activity instanceof ActivityLifecycleable) {
            obtainSubject(activity).onNext(ActivityEvent.RESUME);
        }
    }

    @Override
    public void onActivityPaused(Activity activity) {
        if (activity instanceof ActivityLifecycleable) {
            obtainSubject(activity).onNext(ActivityEvent.PAUSE);
        }
    }

    @Override
    public void onActivityStopped(Activity activity) {
        if (activity instanceof ActivityLifecycleable) {
            obtainSubject(activity).onNext(ActivityEvent.STOP);
        }
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        if (activity instanceof ActivityLifecycleable) {
            obtainSubject(activity).onNext(ActivityEvent.DESTROY);
        }
    }

    /**
     * 从 {BaseActivity} 中获得桥梁对象 {@code BehaviorSubject<ActivityEvent> mLifecycleSubject}
     *
     * @see <a href="https://mcxiaoke.gitbooks.io/rxdocs/content/Subject.html">BehaviorSubject 官方中文文档</a>
     */
    private Subject<ActivityEvent> obtainSubject(Activity activity) {
        return ((ActivityLifecycleable) activity).provideLifecycleSubject();
    }
}
