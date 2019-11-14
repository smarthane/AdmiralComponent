package com.smarthane.admiralcomponent.gank.mvp.ui.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.smarthane.admiral.core.base.AppComponent;
import com.smarthane.admiral.core.http.imageloader.glide.GlideAdmiral;
import com.smarthane.admiral.core.http.imageloader.glide.ImageConfigImpl;
import com.smarthane.admiralcomponent.gank.R;
import com.smarthane.admiralcomponent.gank.mvp.model.entity.GankItemBean;

/**
 * @author smarthane
 * @time 2019/11/10 10:46
 * @describe
 */
public class GankHomeAdapter extends BaseQuickAdapter<GankItemBean, BaseViewHolder> {

    private Context mContext;

    public GankHomeAdapter(Context mContext) {
        super(R.layout.gank_recycle_list);
        this.mContext = mContext;
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, GankItemBean item) {
        AppComponent.get().fetchImageLoader().loadImage(mContext,
                ImageConfigImpl
                        .builder()
                        .url(item.getUrl())
                        .placeholder(R.mipmap.gank_ic_logo)
                        .imageView(helper.getView(R.id.iv_avatar))
                        .build());
    }

}
