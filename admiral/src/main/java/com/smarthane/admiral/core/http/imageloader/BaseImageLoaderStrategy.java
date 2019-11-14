package com.smarthane.admiral.core.http.imageloader;

import android.content.Context;

import androidx.annotation.Nullable;

/**
 * @author smarthane
 * @time 2019/10/27 14:04
 * @describe 图片加载策略
 */
public interface BaseImageLoaderStrategy<T extends ImageConfig> {

    /**
     * 加载图片
     *
     * @param ctx {@link Context}
     * @param config 图片加载配置信息
     */
    void loadImage(@Nullable Context ctx, @Nullable T config);

    /**
     * 停止加载
     *
     * @param ctx {@link Context}
     * @param config 图片加载配置信息
     */
    void clear(@Nullable Context ctx, @Nullable T config);
}
