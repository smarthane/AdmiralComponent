package com.smarthane.admiralcomponent.netease.mvp.presenter;

import com.smarthane.admiral.core.base.AppComponent;
import com.smarthane.admiral.core.mvp.BasePresenter;
import com.smarthane.admiral.core.util.RxLifecycleUtils;
import com.smarthane.admiralcomponent.netease.mvp.contract.NewsDetailContract;
import com.smarthane.admiralcomponent.netease.mvp.model.NewsDetailModel;
import com.smarthane.admiralcomponent.netease.mvp.model.api.Api;
import com.smarthane.admiralcomponent.netease.mvp.model.entity.NewsDetail;
import com.smarthane.admiralcomponent.netease.mvp.model.entity.NewsSummary;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import com.smarthane.admiral.core.base.rx.errorhandler.ErrorHandleSubscriber;
import com.smarthane.admiral.core.base.rx.errorhandler.RetryWithDelay;

/**
 * @author smarthane
 * @time 2019/11/10 14:21
 * @describe
 */
public class NewsDetailPresenter extends BasePresenter<NewsDetailContract.Model, NewsDetailContract.View> {

    private NewsDetailPresenter(NewsDetailContract.Model model, NewsDetailContract.View rootView) {
        super(model, rootView);
    }

    public static NewsDetailPresenter build(NewsDetailContract.View rootView) {
        return new NewsDetailPresenter(new NewsDetailModel(), rootView);
    }

    public void requestOneNewsData(String postId) {
        mModel.getOneNewsData(postId)
                .subscribeOn(Schedulers.io())
                // 遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
                //.retryWhen(new RetryWithDelay(3, 2))
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        // 显示下拉刷新的进度条
                        mRootView.showLoading();
                    }
                })
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        // 隐藏下拉刷新的进度条
                        mRootView.hideLoading();
                    }
                })
                // 使用 Rxlifecycle,使 Disposable 和 Activity 一起销毁
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))
                .subscribe(new ErrorHandleSubscriber<Map<String, NewsDetail>>(AppComponent
                        .get()
                        .fetchRxErrorHandler()) {
                    @Override
                    public void onNext(Map<String, NewsDetail> map) {
                        NewsDetail newsDetail = map.get(postId);
                        if (newsDetail != null) {
                            changeNewsDetail(newsDetail);
                            mRootView.setOneNewsData(newsDetail);
                        }
                    }

                    @Override
                    public void onError(Throwable t) {
                        super.onError(t);
                    }
                });
    }

    private void changeNewsDetail(NewsDetail newsDetail) {
        List<NewsDetail.ImgBean> imgSrcs = newsDetail.getImg();
        if (isChange(imgSrcs)) {
            String newsBody = newsDetail.getBody();
            newsBody = changeNewsBody(imgSrcs, newsBody);
            newsDetail.setBody(newsBody);
        }
    }

    private boolean isChange(List<NewsDetail.ImgBean> imgSrcs) {
        return imgSrcs != null && imgSrcs.size() >= 2;
    }

    private String changeNewsBody(List<NewsDetail.ImgBean> imgSrcs, String newsBody) {
        for (int i = 0; i < imgSrcs.size(); i++) {
            String oldChars = "<!--IMG#" + i + "-->";
            String newChars;
            if (i == 0) {
                newChars = "";
            } else {
                newChars = "<img src=\"" + imgSrcs.get(i).getSrc() + "\" />";
            }
            newsBody = newsBody.replace(oldChars, newChars);

        }
        return newsBody;
    }

}
