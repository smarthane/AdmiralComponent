package com.smarthane.admiral.core.http.imageloader;

import android.content.Context;

import androidx.annotation.Nullable;

import com.smarthane.admiral.core.base.AppComponent;
import com.smarthane.admiral.core.util.PreconditionUtils;

/**
 * @author smarthane
 * @time 2019/10/27 14:08
 * @describe 使用策略模式和建造者模式,可以动态切换图片请求框架(比如说切换成 Picasso )
 * 当需要切换图片请求框架或图片请求框架升级后变更了 Api 时
 * 这里可以将影响范围降到最低,所以封装是为了屏蔽这个风险
 */
public class ImageLoader {

    private ImageLoader() {
        mStrategy = AppComponent.get().fetchGlobalConfigModule().provideImageLoaderStrategy();
    }

    private static class Holder {
        private static ImageLoader sInstance = new ImageLoader();
    }

    public static ImageLoader get() {
        return Holder.sInstance;
    }

    @Nullable
    private BaseImageLoaderStrategy mStrategy;

    /**
     * 加载图片
     *
     * @param context
     * @param config
     * @param <T>
     */
    public <T extends ImageConfig> void loadImage(Context context, T config) {
        PreconditionUtils.checkNotNull(mStrategy, "Please implement BaseImageLoaderStrategy and call GlobalConfigModule.Builder#imageLoaderStrategy(BaseImageLoaderStrategy) in the applyOptions method of ConfigModule");
        this.mStrategy.loadImage(context, config);
    }

    /**
     * 停止加载或清理缓存
     *
     * @param context
     * @param config
     * @param <T>
     */
    public <T extends ImageConfig> void clear(Context context, T config) {
        PreconditionUtils.checkNotNull(mStrategy, "Please implement BaseImageLoaderStrategy and call GlobalConfigModule.Builder#imageLoaderStrategy(BaseImageLoaderStrategy) in the applyOptions method of ConfigModule");
        this.mStrategy.clear(context, config);
    }

    /**
     * 可在运行时随意切换 {@link BaseImageLoaderStrategy}
     *
     * @param strategy
     */
    public void setLoadImgStrategy(BaseImageLoaderStrategy strategy) {
        PreconditionUtils.checkNotNull(strategy, "strategy == null");
        this.mStrategy = strategy;
    }

    @Nullable
    public BaseImageLoaderStrategy getLoadImgStrategy() {
        return mStrategy;
    }

}
