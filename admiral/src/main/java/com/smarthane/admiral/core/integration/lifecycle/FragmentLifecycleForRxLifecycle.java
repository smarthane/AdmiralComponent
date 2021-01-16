package com.smarthane.admiral.core.integration.lifecycle;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.trello.rxlifecycle4.android.FragmentEvent;

import io.reactivex.rxjava3.subjects.Subject;

/**
 * @author smarthane
 * @time 2019/10/27 16:00
 * @describe
 */
public class FragmentLifecycleForRxLifecycle extends FragmentManager.FragmentLifecycleCallbacks {

    public FragmentLifecycleForRxLifecycle() {
    }

    @Override
    public void onFragmentAttached(FragmentManager fm, Fragment f, Context context) {
        if (f instanceof FragmentLifecycleable) {
            obtainSubject(f).onNext(FragmentEvent.ATTACH);
        }
    }

    @Override
    public void onFragmentCreated(FragmentManager fm, Fragment f, Bundle savedInstanceState) {
        if (f instanceof FragmentLifecycleable) {
            obtainSubject(f).onNext(FragmentEvent.CREATE);
        }
    }

    @Override
    public void onFragmentViewCreated(FragmentManager fm, Fragment f, View v, Bundle savedInstanceState) {
        if (f instanceof FragmentLifecycleable) {
            obtainSubject(f).onNext(FragmentEvent.CREATE_VIEW);
        }
    }

    @Override
    public void onFragmentStarted(FragmentManager fm, Fragment f) {
        if (f instanceof FragmentLifecycleable) {
            obtainSubject(f).onNext(FragmentEvent.START);
        }
    }

    @Override
    public void onFragmentResumed(FragmentManager fm, Fragment f) {
        if (f instanceof FragmentLifecycleable) {
            obtainSubject(f).onNext(FragmentEvent.RESUME);
        }
    }

    @Override
    public void onFragmentPaused(FragmentManager fm, Fragment f) {
        if (f instanceof FragmentLifecycleable) {
            obtainSubject(f).onNext(FragmentEvent.PAUSE);
        }
    }

    @Override
    public void onFragmentStopped(FragmentManager fm, Fragment f) {
        if (f instanceof FragmentLifecycleable) {
            obtainSubject(f).onNext(FragmentEvent.STOP);
        }
    }

    @Override
    public void onFragmentViewDestroyed(FragmentManager fm, Fragment f) {
        if (f instanceof FragmentLifecycleable) {
            obtainSubject(f).onNext(FragmentEvent.DESTROY_VIEW);
        }
    }

    @Override
    public void onFragmentDestroyed(FragmentManager fm, Fragment f) {
        if (f instanceof FragmentLifecycleable) {
            obtainSubject(f).onNext(FragmentEvent.DESTROY);
        }
    }

    @Override
    public void onFragmentDetached(FragmentManager fm, Fragment f) {
        if (f instanceof FragmentLifecycleable) {
            obtainSubject(f).onNext(FragmentEvent.DETACH);
        }
    }

    private Subject<FragmentEvent> obtainSubject(Fragment fragment) {
        return ((FragmentLifecycleable) fragment).provideLifecycleSubject();
    }
}
