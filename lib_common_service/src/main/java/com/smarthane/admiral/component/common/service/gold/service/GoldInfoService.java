package com.smarthane.admiral.component.common.service.gold.service;

import com.alibaba.android.arouter.facade.template.IProvider;
import com.smarthane.admiral.component.common.service.gold.model.GoldInfo;

/**
 * @author smarthane
 * @time 2019/11/10 16:39
 * @describe 向外提供服务的接口, 在此接口中声明一些具有特定功能的方法提供给外部, 即可让一个组件与其他组件或宿主进行交互
 */
public interface GoldInfoService extends IProvider {
    GoldInfo getInfo();
}
