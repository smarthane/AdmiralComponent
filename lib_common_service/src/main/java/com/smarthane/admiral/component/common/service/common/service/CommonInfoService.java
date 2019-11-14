package com.smarthane.admiral.component.common.service.common.service;

import com.alibaba.android.arouter.facade.template.IProvider;
import com.smarthane.admiral.component.common.service.common.model.CommonInfo;

/**
 * @author smarthane
 * @time 2019/11/10 15:45
 * @describe
 */
public interface CommonInfoService extends IProvider {
    CommonInfo getInfo();
}
