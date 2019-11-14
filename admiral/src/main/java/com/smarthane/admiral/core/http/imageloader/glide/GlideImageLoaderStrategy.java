package com.smarthane.admiral.core.http.imageloader.glide;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.Registry;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.smarthane.admiral.core.http.imageloader.BaseImageLoaderStrategy;
import com.smarthane.admiral.core.util.LogUtils;
import com.smarthane.admiral.core.util.PreconditionUtils;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.schedulers.Schedulers;

/**
 * @author smarthane
 * @time 2019/10/27 14:43
 * @describe 此类只是简单的实现了 Glide 加载的策略,方便快速使用,但大部分情况会需要应对复杂的场景
 * 这时可自行实现 {BaseImageLoaderStrategy} 和 {ImageConfig} 替换现有策略
 */
public class GlideImageLoaderStrategy implements BaseImageLoaderStrategy<ImageConfigImpl>, GlideAppliesOptions {


    @Override
    public void loadImage(@Nullable Context ctx, @Nullable ImageConfigImpl config) {
        PreconditionUtils.checkNotNull(ctx, "Context is required");
        PreconditionUtils.checkNotNull(config, "ImageConfigImpl is required");
        PreconditionUtils.checkNotNull(config.getImageView(), "ImageView is required");

        GlideRequests requests;

        // 如果context是activity则自动使用Activity的生命周期
        requests = GlideAdmiral.with(ctx);

        GlideRequest<Drawable> glideRequest = requests.load(config.getUrl());

        // 缓存策略
        switch (config.getCacheStrategy()) {
            case CacheStrategy.ALL:
                glideRequest.diskCacheStrategy(DiskCacheStrategy.ALL);
                break;
            case CacheStrategy.NONE:
                glideRequest.diskCacheStrategy(DiskCacheStrategy.NONE);
                break;
            case CacheStrategy.RESOURCE:
                glideRequest.diskCacheStrategy(DiskCacheStrategy.RESOURCE);
                break;
            case CacheStrategy.DATA:
                glideRequest.diskCacheStrategy(DiskCacheStrategy.DATA);
                break;
            case CacheStrategy.AUTOMATIC:
                glideRequest.diskCacheStrategy(DiskCacheStrategy.AUTOMATIC);
                break;
            default:
                glideRequest.diskCacheStrategy(DiskCacheStrategy.ALL);
                break;
        }

        if (config.isCrossFade()) {
            glideRequest.transition(DrawableTransitionOptions.withCrossFade());
        }

        if (config.isCenterCrop()) {
            glideRequest.centerCrop();
        }

        if (config.isCircle()) {
            glideRequest.circleCrop();
        }

        if (config.isImageRadius()) {
            glideRequest.transform(new RoundedCorners(config.getImageRadius()));
        }

        if (config.isBlurImage()) {
            glideRequest.transform(new BlurTransformation(config.getBlurValue()));
        }

        // glide用它来改变图形的形状
        if (config.getTransformation() != null) {
            glideRequest.transform(config.getTransformation());
        }

        // 设置占位符
        if (config.getPlaceholder() != 0) {
            glideRequest.placeholder(config.getPlaceholder());
        }

        // 设置错误的图片
        if (config.getErrorPic() != 0) {
            glideRequest.error(config.getErrorPic());
        }

        // 设置请求 url 为空图片
        if (config.getFallback() != 0) {
            glideRequest.fallback(config.getFallback());
        }

        glideRequest.into(config.getImageView());
    }

    @Override
    public void clear(@Nullable final Context ctx, @Nullable ImageConfigImpl config) {
        PreconditionUtils.checkNotNull(ctx, "Context is required");
        PreconditionUtils.checkNotNull(config, "ImageConfigImpl is required");

        if (config.getImageView() != null) {
            GlideAdmiral.get(ctx).getRequestManagerRetriever().get(ctx).clear(config.getImageView());
        }

        // 取消在执行的任务并且释放资源
        if (config.getImageViews() != null && config.getImageViews().length > 0) {
            for (ImageView imageView : config.getImageViews()) {
                GlideAdmiral.get(ctx).getRequestManagerRetriever().get(ctx).clear(imageView);
            }
        }

        // 清除本地缓存
        if (config.isClearDiskCache()) {
            Completable.fromAction(new Action() {
                @Override
                public void run() throws Exception {
                    Glide.get(ctx).clearDiskCache();
                }
            }).subscribeOn(Schedulers.io()).subscribe();
        }

        // 清除内存缓存
        if (config.isClearMemory()) {
            Completable.fromAction(new Action() {
                @Override
                public void run() throws Exception {
                    Glide.get(ctx).clearMemory();
                }
            }).subscribeOn(AndroidSchedulers.mainThread()).subscribe();
        }
    }

    @Override
    public void applyGlideOptions(@NonNull Context context, @NonNull GlideBuilder builder) {
        //TODO
        LogUtils.debugInfo("applyGlideOptions");
    }

    @Override
    public void registerComponents(@NonNull Context context, @NonNull Glide glide, @NonNull Registry registry) {
        //TODO
        LogUtils.debugInfo("registerComponents");
    }
}
