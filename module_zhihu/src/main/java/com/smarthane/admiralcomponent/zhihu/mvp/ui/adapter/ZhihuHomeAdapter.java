package com.smarthane.admiralcomponent.zhihu.mvp.ui.adapter;

import android.content.Context;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.smarthane.admiral.core.base.AppComponent;
import com.smarthane.admiral.core.http.imageloader.glide.ImageConfigImpl;
import com.smarthane.admiralcomponent.zhihu.R;
import com.smarthane.admiralcomponent.zhihu.mvp.model.entity.DailyListBean;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;

/**
 * @author smarthane
 * @time 2019/11/10 9:59
 * @describe
 */
public class ZhihuHomeAdapter extends BaseQuickAdapter<DailyListBean.StoriesBean, BaseViewHolder> {

    private Context mContext;

    public ZhihuHomeAdapter(Context mContext) {
        super(R.layout.zhihu_recycle_list);
        this.mContext = mContext;
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, DailyListBean.StoriesBean item) {
        Observable.just(item.getTitle())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        helper.setText(R.id.tv_name, s);
                    }
                });

        // TODO itemView 的 Context 就是 Activity, Glide 会自动处理并和该 Activity 的生命周期绑定
        AppComponent.get().fetchImageLoader().loadImage(mContext,
                ImageConfigImpl
                        .builder()
                        .url(item.getImages().get(0))
                        .placeholder(R.mipmap.zhihu_ic_logo)
                        .imageView(helper.getView(R.id.iv_avatar))
                        .build());
    }

}
