package com.smarthane.admiralcomponent.jetpack.mvp.presenter

import com.smarthane.admiral.core.mvp.BasePresenter
import com.smarthane.admiralcomponent.jetpack.mvp.contract.JetpackHomeContract
import com.smarthane.admiralcomponent.jetpack.mvp.model.JetpackHomeModel

/**
 * @author smarthane
 * @time 2019/11/10 18:16
 * @describe
 */
class JetpackHomePresenter : BasePresenter<JetpackHomeContract.Model, JetpackHomeContract.View> {

    private constructor(model: JetpackHomeContract.Model, rootView: JetpackHomeContract.View) : super(model, rootView)

    companion object {

        fun build(rootView: JetpackHomeContract.View): JetpackHomePresenter {
            return JetpackHomePresenter(JetpackHomeModel(), rootView)
        }
    }

}