package com.smarthane.admiralcomponent.netease.mvp.ui.activity;

import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.smarthane.admiral.component.common.sdk.core.RouterHub;
import com.smarthane.admiral.component.common.sdk.util.StatusBarCompatUtils;
import com.smarthane.admiral.core.base.AppComponent;
import com.smarthane.admiral.core.base.BaseActivity;
import com.smarthane.admiral.core.http.imageloader.glide.ImageConfigImpl;
import com.smarthane.admiral.core.util.TimeUtils;
import com.smarthane.admiralcomponent.netease.R;
import com.smarthane.admiralcomponent.netease.app.NeteaseConstants;
import com.smarthane.admiralcomponent.netease.mvp.contract.NewsDetailContract;
import com.smarthane.admiralcomponent.netease.mvp.model.entity.NewsDetail;
import com.smarthane.admiralcomponent.netease.mvp.presenter.NewsDetailPresenter;
import com.smarthane.admiralcomponent.netease.mvp.ui.widget.HtmlUrlImageGetter;

import java.util.List;

/**
 * @author smarthane
 * @time 2019/11/10 12:30
 * @describe 新闻详情
 */
@Route(path = RouterHub.NETEASE_NEWSDETAILACTIVITY)
public class NewsDetailActivity extends BaseActivity<NewsDetailPresenter> implements NewsDetailContract.View {

    private ImageView ivDetailPhoto;
    private View maskView;
    private Toolbar toolbar;
    private CollapsingToolbarLayout toolbarLayout;
    private AppBarLayout appBar;
    private TextView tvNewsDetailFrom;
    private TextView tvNewsDetailBody;
    private ProgressBar progressBar;
    private FloatingActionButton fab;

    private String postId;
    private String imgUrl;
    private String mNewsTitle;
    private String mShareLink;

    @Override
    public int setLayoutResouceId() {
        return R.layout.netease_activity_news_detail;
    }

    @Override
    protected void initData() {
        postId = getIntent().getStringExtra(NeteaseConstants.NEWS_POST_ID);
        imgUrl = getIntent().getStringExtra(NeteaseConstants.NEWS_IMG_RES);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        StatusBarCompatUtils.translucentStatusBar(this);
        ivDetailPhoto = findViewById(R.id.iv_news_detail_photo);
        maskView = findViewById(R.id.mask_view);
        toolbar = findViewById(R.id.toolbar);
        toolbarLayout = findViewById(R.id.toolbar_layout);
        appBar = findViewById(R.id.app_bar);
        tvNewsDetailFrom = findViewById(R.id.tv_news_detail_from);
        tvNewsDetailBody = findViewById(R.id.tv_news_detail_body);
        progressBar = findViewById(R.id.progress_bar);
        fab = findViewById(R.id.fab);
        toolbar.inflateMenu(R.menu.netease_news_detail);
    }

    @Override
    protected void bindListener() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    finishAfterTransition();
                } else {
                    finish();
                }
            }
        });
    }

    @Override
    protected void initPresenter() {
        mPresenter = NewsDetailPresenter.build(this);
    }

    @Override
    protected void execBusiness() {
        mPresenter.requestOneNewsData(postId);
    }

    @Override
    public void showLoading() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void setOneNewsData(NewsDetail newsDetail) {
        mShareLink = newsDetail.getShareLink();
        mNewsTitle = newsDetail.getTitle();
        String newsSource = newsDetail.getSource();
        String newsTime = TimeUtils.formatDate(newsDetail.getPtime());
        String newsImgSrc = getImgSrcs(newsDetail);
        setNewsDetailFrom(newsSource, newsTime);
        setToolBarLayout(mNewsTitle);
        setNewsDetailPhoto(newsImgSrc);
        setNewsDetailBody(newsDetail);
    }

    private void setNewsDetailFrom(String newsSource, String newsTime) {
        tvNewsDetailFrom.setText(getString(R.string.netease_news_from, newsSource, newsTime));
    }

    private void setToolBarLayout(String newsTitle) {
        toolbarLayout.setTitle(newsTitle);
        toolbarLayout.setExpandedTitleColor(ContextCompat.getColor(this, R.color.public_white));
        toolbarLayout.setCollapsedTitleTextColor(ContextCompat.getColor(this, R.color.netease_primary_text_white));
    }

    private void setNewsDetailPhoto(String imgSrc) {
        AppComponent.get().fetchImageLoader().loadImage(this,
                ImageConfigImpl
                        .builder()
                        .url(imgSrc)
                        .placeholder(R.mipmap.netease_ic_logo)
                        .imageView(ivDetailPhoto)
                        .build());
    }

    private String getImgSrcs(NewsDetail newsDetail) {
        List<NewsDetail.ImgBean> imgSrcs = newsDetail.getImg();
        String imgSrc;
        if (imgSrcs != null && imgSrcs.size() > 0) {
            imgSrc = imgSrcs.get(0).getSrc();
        } else {
            imgSrc = imgUrl;
        }
        return imgSrc;
    }

    private void setNewsDetailBody(NewsDetail newsDetail) {
        int imgTotal = newsDetail.getImg().size();
        String newsBody = newsDetail.getBody();
        if (isShowBody(newsBody, imgTotal)) {
            tvNewsDetailBody.setText(Html.fromHtml(
                    newsBody,
                    new HtmlUrlImageGetter(
                            tvNewsDetailBody,
                            newsBody, imgTotal
                    ),
                    null
            ));
        } else {
            tvNewsDetailBody.setText(Html.fromHtml(newsBody));
        }
    }

    private boolean isShowBody(String newsBody, int imgTotal) {
        return imgTotal >= 2 && newsBody != null;
    }
}
