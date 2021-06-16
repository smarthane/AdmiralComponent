package com.smarthane.admiral.component.common.service.jetpack.service

import com.alibaba.android.arouter.facade.template.IProvider
import com.smarthane.admiral.component.common.service.jetpack.model.JetpackInfo

/**
 * @author smarthane
 * @time 2019/11/10 13:55
 * @describe
 */
interface JetpackInfoService : IProvider {
    fun getInfo() : JetpackInfo
}