package com.smarthane.admiralcomponent.jetpack.mvp.ui.activity

import com.smarthane.admiral.core.base.BaseActivity
import com.smarthane.admiralcomponent.jetpack.R
import com.smarthane.admiralcomponent.jetpack.mvp.presenter.JetpackEmptyPresenter

/**
 * @author smarthane
 * @time 2019/11/10 18:16
 * @describe
 */
class JetpackEmptyActivity : BaseActivity<JetpackEmptyPresenter>() {

    override fun setLayoutResouceId(): Int = R.layout.jetpack_activity_empty


}