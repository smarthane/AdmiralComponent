package com.smarthane.admiral.core.base.delegate;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

/**
 * @author smarthane
 * @time 2019/10/27 10:57
 * @describe {FragmentDelegate} 默认实现类
 */
public class FragmentDelegateImpl implements FragmentDelegate {

    private FragmentManager mFragmentManager;
    private Fragment mFragment;
    private IFragment iFragment;

    public FragmentDelegateImpl(@NonNull FragmentManager mFragmentManager, @NonNull Fragment mFragment) {
        this.mFragmentManager = mFragmentManager;
        this.mFragment = mFragment;
        this.iFragment = (IFragment) mFragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

    }

    @Override
    public void onCreateView(@Nullable View view, @Nullable Bundle savedInstanceState) {

    }

    @Override
    public void onActivityCreate(@Nullable Bundle savedInstanceState) {

    }

    @Override
    public void onStart() {

    }

    @Override
    public void onResume() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void onStop() {

    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {

    }

    @Override
    public void onDestroyView() {

    }

    @Override
    public void onDestroy() {
        this.mFragmentManager = null;
        this.mFragment = null;
        this.iFragment = null;
    }

    @Override
    public void onDetach() {

    }

    @Override
    public boolean isAdded() {
        return mFragment != null && mFragment.isAdded();
    }
}
