package com.smarthane.admiralcomponent.gank.mvp.ui.activity

import android.app.Dialog
import android.os.Bundle
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener
import com.smarthane.admiral.component.common.res.dialog.ProgresDialog
import com.smarthane.admiral.component.common.sdk.core.RouterHub
import com.smarthane.admiral.core.base.BaseActivity
import com.smarthane.admiralcomponent.gank.R
import com.smarthane.admiralcomponent.gank.mvp.contract.GankHomeContract
import com.smarthane.admiralcomponent.gank.mvp.presenter.GankHomePresenter
import com.smarthane.admiralcomponent.gank.mvp.ui.adapter.GankHomeAdapter
import kotlinx.android.synthetic.main.gank_activity_home.*

/**
 * @author smarthane
 * @time 2019/10/27 9:54
 * @describe
 */
@Route(path = RouterHub.GANK_HOMEACTIVITY2)
class GankHome2Activity : BaseActivity<GankHomePresenter>(), GankHomeContract.View {

    private var mDialog: Dialog? = null
    private var mGankHomeAdapter: GankHomeAdapter? = null

    override fun setLayoutResouceId(): Int = R.layout.gank_activity_home

    override fun initView(savedInstanceState: Bundle?) {
        mDialog = ProgresDialog(this)
        mGankHomeAdapter = GankHomeAdapter(this)
        recyclerView.adapter = mGankHomeAdapter
        recyclerView.layoutManager = GridLayoutManager(this, 2)
        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.setHasFixedSize(true)
    }

    override fun initPresenter() {
        mPresenter = GankHomePresenter.build(this)
        mPresenter?.setGankHomeAdapter(mGankHomeAdapter)
    }

    override fun bindListener() {
        refreshLayout.setOnRefreshLoadMoreListener(object : OnRefreshLoadMoreListener {
            override fun onLoadMore(refreshLayout: RefreshLayout) {
                mPresenter?.requestGirls(false)
            }

            override fun onRefresh(refreshLayout: RefreshLayout) {
                mPresenter?.requestGirls(true)
            }
        })
    }

    override fun execBusiness() {
        mDialog?.show()
    }

    override fun showLoading() {
    }

    override fun startLoadMore() {
    }

    override fun hideLoading() {
        refreshLayout.finishRefresh()
        mDialog?.dismiss()
    }

    override fun endLoadMore() {
        refreshLayout.finishLoadMore()
    }
}