package com.smarthane.admiralcomponent.netease.mvp.ui.adapter;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;

import com.alibaba.android.arouter.launcher.ARouter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.smarthane.admiral.component.common.sdk.core.RouterHub;
import com.smarthane.admiral.core.base.AppComponent;
import com.smarthane.admiral.core.http.imageloader.glide.ImageConfigImpl;
import com.smarthane.admiral.core.util.TimeUtils;
import com.smarthane.admiralcomponent.netease.R;
import com.smarthane.admiralcomponent.netease.app.NeteaseConstants;
import com.smarthane.admiralcomponent.netease.mvp.model.entity.NewsSummary;

/**
 * @author smarthane
 * @time 2019/11/10 10:47
 * @describe
 */
public class NewsListAdapter extends BaseQuickAdapter<NewsSummary, BaseViewHolder> {

    private Context mContext;

    public NewsListAdapter(Context mContext) {
        super(R.layout.netease_news_list_item);
        this.mContext = mContext;
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, NewsSummary item) {
        String title = item.getLtitle();
        if (title == null) {
            title = item.getTitle();
        }
        String ptime = TimeUtils.formatDate(item.getPtime());
        String digest = item.getDigest();
        String imgSrc = item.getImgsrc();

        helper.setText(R.id.tv_news_summary_title, title);
        helper.setText(R.id.tv_news_summary_ptime, ptime);
        helper.setText(R.id.tv_news_summary_digest, digest);

        AppComponent.get().fetchImageLoader().loadImage(mContext,
                ImageConfigImpl
                        .builder()
                        .url(imgSrc)
                        .placeholder(R.mipmap.netease_ic_logo)
                        .imageView(helper.getView(R.id.iv_news_summary_photo))
                        .build());

        helper.setOnClickListener(R.id.cardView, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance()
                        .build(RouterHub.NETEASE_NEWSDETAILACTIVITY)
                        .withString(NeteaseConstants.NEWS_POST_ID, item.getPostid())
                        .withString(NeteaseConstants.NEWS_IMG_RES, item.getImgsrc())
                        .navigation(mContext);
            }
        });
    }
}
