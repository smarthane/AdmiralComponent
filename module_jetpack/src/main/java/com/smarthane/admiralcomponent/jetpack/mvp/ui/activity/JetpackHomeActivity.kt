package com.smarthane.admiralcomponent.jetpack.mvp.ui.activity

import android.view.View
import com.alibaba.android.arouter.facade.annotation.Route
import com.smarthane.admiral.component.common.sdk.core.RouterHub
import com.smarthane.admiral.core.base.BaseActivity
import com.smarthane.admiralcomponent.jetpack.R
import com.smarthane.admiralcomponent.jetpack.mvp.contract.JetpackHomeContract
import com.smarthane.admiralcomponent.jetpack.mvp.presenter.JetpackHomePresenter
import kotlinx.android.synthetic.main.jetpack_activity_home.*

/**
 * @author smarthane
 * @time 2019/11/10 18:16
 * @describe
 */
@Route(path = RouterHub.JETPACK_HOMEACTIVITY)
class JetpackHomeActivity : BaseActivity<JetpackHomePresenter>(), JetpackHomeContract.View {

    override fun setLayoutResouceId(): Int = R.layout.jetpack_activity_home

    override fun initPresenter() {
        mPresenter = JetpackHomePresenter.build(this)
    }

    override fun bindListener() {
        btnTest.setOnClickListener(mOnClickListener)
    }

    override fun showLoading() {
        TODO("Not yet implemented")
    }

    override fun hideLoading() {
        TODO("Not yet implemented")
    }

    private val mOnClickListener = View.OnClickListener {
        when(it.id) {
            R.id.btnTest -> {

            }
        }
    }
}