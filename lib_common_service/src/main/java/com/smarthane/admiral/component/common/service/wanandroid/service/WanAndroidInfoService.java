package com.smarthane.admiral.component.common.service.wanandroid.service;

import com.alibaba.android.arouter.facade.template.IProvider;
import com.smarthane.admiral.component.common.service.wanandroid.model.WanAndroidInfo;

/**
 * @author smarthane
 * @time 2019/11/10 16:41
 * @describe
 */
public interface WanAndroidInfoService extends IProvider {
    WanAndroidInfo getInfo();
}

