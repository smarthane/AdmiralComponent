package com.smarthane.admiral.component.common.service.netease.service;

import com.alibaba.android.arouter.facade.template.IProvider;
import com.smarthane.admiral.component.common.service.netease.model.NeteaseInfo;

/**
 * @author smarthane
 * @time 2019/11/10 13:55
 * @describe
 */
public interface NeteaseInfoService extends IProvider {
    NeteaseInfo getInfo();
}
