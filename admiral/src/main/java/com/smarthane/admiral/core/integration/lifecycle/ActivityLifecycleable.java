package com.smarthane.admiral.core.integration.lifecycle;

import com.trello.rxlifecycle4.android.ActivityEvent;

/**
 * @author smarthane
 * @time 2019/10/27 15:56
 * @describe 让 {Activity} 实现此接口,即可正常使用 {RxLifecycle}
 */
public interface ActivityLifecycleable extends Lifecycleable<ActivityEvent> {
}
