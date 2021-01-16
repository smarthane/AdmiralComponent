package com.smarthane.admiralcomponent.jetpack.component.service

import android.content.Context
import com.alibaba.android.arouter.facade.annotation.Route
import com.smarthane.admiral.component.common.sdk.core.RouterHub
import com.smarthane.admiral.component.common.service.jetpack.model.JetpackInfo
import com.smarthane.admiral.component.common.service.jetpack.service.JetpackInfoService

/**
 * @author smarthane
 * @time 2019/11/10 18:16
 * @describe
 */
@Route(path = RouterHub.JETPACK_SERVICE_JETPACKINFOSERVICE, name = "Jetpack信息服务")
class JetpackInfoServiceImpl : JetpackInfoService {
    override fun getInfo(): JetpackInfo {
        return JetpackInfo("JetPack组件")
    }

    override fun init(context: Context?) {
    }
}