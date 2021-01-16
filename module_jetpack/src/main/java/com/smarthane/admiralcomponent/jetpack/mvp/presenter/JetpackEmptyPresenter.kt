package com.smarthane.admiralcomponent.jetpack.mvp.presenter

import com.smarthane.admiral.core.mvp.BasePresenter
import com.smarthane.admiralcomponent.jetpack.mvp.contract.JetpackEmptyContract
import com.smarthane.admiralcomponent.jetpack.mvp.model.JetpackEmptyModel

/**
 * @author smarthane
 * @time 2019/11/10 18:16
 * @describe
 */
class JetpackEmptyPresenter : BasePresenter<JetpackEmptyContract.Model, JetpackEmptyContract.View> {

    private constructor(model: JetpackEmptyContract.Model, rootView: JetpackEmptyContract.View) : super(model, rootView)

    companion object {
        fun build(rootView: JetpackEmptyContract.View): JetpackEmptyPresenter {
            return JetpackEmptyPresenter(JetpackEmptyModel(), rootView)
        }
    }
}