package com.smarthane.admiral.core.integration.lifecycle;

import com.trello.rxlifecycle4.android.FragmentEvent;

/**
 * @author smarthane
 * @time 2019/10/27 15:57
 * @describe 让 {Fragment} 实现此接口,即可正常使用 {RxLifecycle}
 */
public interface FragmentLifecycleable extends Lifecycleable<FragmentEvent> {
}
