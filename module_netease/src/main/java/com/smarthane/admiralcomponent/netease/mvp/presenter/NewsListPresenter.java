package com.smarthane.admiralcomponent.netease.mvp.presenter;

import com.smarthane.admiral.core.base.AppComponent;
import com.smarthane.admiral.core.mvp.BasePresenter;
import com.smarthane.admiral.core.util.RxLifecycleUtils;
import com.smarthane.admiralcomponent.netease.mvp.contract.NewsListContract;
import com.smarthane.admiralcomponent.netease.mvp.model.NewsListModel;
import com.smarthane.admiralcomponent.netease.mvp.model.api.Api;
import com.smarthane.admiralcomponent.netease.mvp.model.entity.NewsSummary;
import com.smarthane.admiralcomponent.netease.mvp.ui.adapter.NewsListAdapter;

import java.util.ArrayList;
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
 * @time 2019/11/10 18:21
 * @describe
 */
public class NewsListPresenter extends BasePresenter<NewsListContract.Model, NewsListContract.View> {

    private NewsListAdapter newsListAdapter;
    private int startPage;

    private NewsListPresenter(NewsListContract.Model model, NewsListContract.View rootView) {
        super(model, rootView);
    }

    public static NewsListPresenter build(NewsListContract.View rootView) {
        return new NewsListPresenter(new NewsListModel(), rootView);
    }

    public void setNewsListAdapter(NewsListAdapter newsListAdapter) {
        this.newsListAdapter = newsListAdapter;
    }

    public void getNewsListDataRequest(boolean pullToRefresh, String type, String id) {
        if (pullToRefresh) {
            startPage = 0;
        }
        mModel.getNewsListData(type, id, startPage)
                .subscribeOn(Schedulers.io())
                // 遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
                .retryWhen(new RetryWithDelay(3, 2))
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        // 显示下拉刷新的进度条
                        // mRootView.showLoading();
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        // 隐藏下拉刷新的进度条
                        if (pullToRefresh) {
                            mRootView.hideLoading();
                        } else {
                            mRootView.endLoadMore();
                        }
                    }
                })
                // 使用 Rxlifecycle,使 Disposable 和 Activity 一起销毁
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))
                .subscribe(new ErrorHandleSubscriber<Map<String, List<NewsSummary>>>(AppComponent
                        .get()
                        .fetchRxErrorHandler()) {
                    @Override
                    public void onNext(Map<String, List<NewsSummary>> map) {
                        // 房产实际上针对地区的它的id与返回key不同
                        List<NewsSummary> newsSummaryList = id.endsWith(Api.HOUSE_ID) ? map.get("北京") : map.get(id);
                        if (newsSummaryList != null
                                && !newsSummaryList.isEmpty()
                                && newsListAdapter != null) {
                            // 去重 TODO 算法待优化
                            List<NewsSummary> tempList = new ArrayList<>();
                            List<String> tempPostidList = new ArrayList<>();
                            for (NewsSummary summary : newsSummaryList) {
                                // 如果tempPostidList中没有则添加
                                if (!tempPostidList.contains(summary.getPostid())) {
                                    tempPostidList.add(summary.getPostid());
                                    tempList.add(summary);
                                }
                            }
                            // 按时间排序
                            Collections.sort(tempList, new Comparator<NewsSummary>() {
                                @Override
                                public int compare(NewsSummary o1, NewsSummary o2) {
                                    return o2.getPtime().compareTo(o1.getPtime());
                                }
                            });
                            if (pullToRefresh) {
                                newsListAdapter.setNewData(tempList);
                            } else {
                                newsListAdapter.addData(tempList);
                                startPage++;
                            }
                        }
                    }
                });

    }

}
