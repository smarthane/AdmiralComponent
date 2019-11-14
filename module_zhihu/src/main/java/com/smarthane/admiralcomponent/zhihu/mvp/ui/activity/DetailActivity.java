package com.smarthane.admiralcomponent.zhihu.mvp.ui.activity;

import android.app.Dialog;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.smarthane.admiral.component.common.res.dialog.ProgresDialog;
import com.smarthane.admiral.component.common.sdk.core.RouterHub;
import com.smarthane.admiral.core.base.BaseActivity;
import com.smarthane.admiral.core.util.HtmlUtils;
import com.smarthane.admiralcomponent.zhihu.R;
import com.smarthane.admiralcomponent.zhihu.app.ZhihuConstants;
import com.smarthane.admiralcomponent.zhihu.mvp.contract.DetailContract;
import com.smarthane.admiralcomponent.zhihu.mvp.model.entity.ZhihuDetailBean;
import com.smarthane.admiralcomponent.zhihu.mvp.presenter.DetailPresenter;

/**
 * @author smarthane
 * @time 2019/11/10 9:54
 * @describe
 */
@Route(path = RouterHub.ZHIHU_DETAILACTIVITY)
public class DetailActivity extends BaseActivity<DetailPresenter> implements DetailContract.View {

    private WebView mWebView;
    private Dialog mDialog;

    @Override
    public int setLayoutResouceId() {
        return R.layout.zhihu_activity_detail;
    }


    @Override
    protected void initView(Bundle savedInstanceState) {
        mDialog = new ProgresDialog(this);
        mWebView = findViewById(R.id.webView);
        initWebView();
        loadTitle();
    }

    @Override
    protected void initPresenter() {
        mPresenter = DetailPresenter.build(this);
    }

    @Override
    protected void execBusiness() {
        mPresenter.requestDetailInfo(getIntent().getIntExtra(ZhihuConstants.DETAIL_ID, 0));
    }

    private void initWebView() {
        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setLoadWithOverviewMode(true);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        settings.setSupportZoom(true);
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
    }

    private void loadTitle() {
        String title = getIntent().getStringExtra(ZhihuConstants.DETAIL_TITLE);
        if (title.length() > 10) {
            title = title.substring(0, 10) + " ...";
        }
        setTitle(title);
    }

    @Override
    public void shonContent(ZhihuDetailBean bean) {
        String htmlData = HtmlUtils.createHtmlData(bean.getBody(), bean.getCss(), bean.getJs());
        mWebView.loadData(htmlData, HtmlUtils.MIME_TYPE, ZhihuConstants.ENCODING);
    }

    @Override
    public void showLoading() {
        mDialog.show();
    }

    @Override
    public void hideLoading() {
        mDialog.dismiss();
    }
}
