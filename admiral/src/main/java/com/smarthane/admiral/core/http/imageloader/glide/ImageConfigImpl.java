package com.smarthane.admiral.core.http.imageloader.glide;

import android.widget.ImageView;

import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.smarthane.admiral.core.http.imageloader.ImageConfig;

/**
 * @author smarthane
 * @time 2019/10/27 14:30
 * @describe 这里存放图片请求的配置信息, 可以一直扩展字段, 如果外部调用时想让图片加载框架
 * 做一些操作,比如清除缓存或者切换缓存策略,则可以定义一个 int 类型的变量,内部根据 switch(int) 做不同的操作
 * 其他操作同理
 */
public class ImageConfigImpl extends ImageConfig {
    /**
     * 0对应DiskCacheStrategy.all,1对应DiskCacheStrategy.NONE,2对应DiskCacheStrategy.SOURCE,3对应DiskCacheStrategy.RESULT
     */
    private @CacheStrategy.Strategy int cacheStrategy;
    /**
     * 请求 url 为空,则使用此图片作为占位符
     */
    private int fallback;
    /**
     * 图片每个圆角的大小
     */
    private int imageRadius;
    /**
     * 高斯模糊值, 值越大模糊效果越大
     */
    private int blurValue;
    /**
     * @see {@link Builder#transformation(BitmapTransformation)}
     * glide用它来改变图形的形状
     */
    @Deprecated
    private BitmapTransformation transformation;
    private ImageView[] imageViews;
    /**
     * 是否使用淡入淡出过渡动画
     */
    private boolean isCrossFade;
    /**
     * 是否将图片剪切为 CenterCrop
     */
    private boolean isCenterCrop;
    /**
     * 是否将图片剪切为圆形
     */
    private boolean isCircle;
    /**
     * 清理内存缓存
     */
    private boolean isClearMemory;
    /**
     * 清理本地缓存
     */
    private boolean isClearDiskCache;

    private ImageConfigImpl(Builder builder) {
        this.url = builder.url;
        this.imageView = builder.imageView;
        this.placeholder = builder.placeholder;
        this.errorPic = builder.errorPic;
        this.fallback = builder.fallback;
        this.cacheStrategy = builder.cacheStrategy;
        this.imageRadius = builder.imageRadius;
        this.blurValue = builder.blurValue;
        this.transformation = builder.transformation;
        this.imageViews = builder.imageViews;
        this.isCrossFade = builder.isCrossFade;
        this.isCenterCrop = builder.isCenterCrop;
        this.isCircle = builder.isCircle;
        this.isClearMemory = builder.isClearMemory;
        this.isClearDiskCache = builder.isClearDiskCache;
    }

    public @CacheStrategy.Strategy int getCacheStrategy() {
        return cacheStrategy;
    }

    public BitmapTransformation getTransformation() {
        return transformation;
    }

    public ImageView[] getImageViews() {
        return imageViews;
    }

    public boolean isClearMemory() {
        return isClearMemory;
    }

    public boolean isClearDiskCache() {
        return isClearDiskCache;
    }

    public int getFallback() {
        return fallback;
    }

    public int getBlurValue() {
        return blurValue;
    }

    public boolean isBlurImage() {
        return blurValue > 0;
    }

    public int getImageRadius() {
        return imageRadius;
    }

    public boolean isImageRadius() {
        return imageRadius > 0;
    }

    public boolean isCrossFade() {
        return isCrossFade;
    }

    public boolean isCenterCrop() {
        return isCenterCrop;
    }

    public boolean isCircle() {
        return isCircle;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private String url;
        private ImageView imageView;
        private int placeholder;
        private int errorPic;
        private int fallback; //请求 url 为空,则使用此图片作为占位符
        private @CacheStrategy.Strategy int cacheStrategy;//0对应DiskCacheStrategy.all,1对应DiskCacheStrategy.NONE,2对应DiskCacheStrategy.SOURCE,3对应DiskCacheStrategy.RESULT
        private int imageRadius;//图片每个圆角的大小
        private int blurValue;//高斯模糊值, 值越大模糊效果越大
        /**
         * @see {@link Builder#transformation(BitmapTransformation)}
         */
        @Deprecated
        private BitmapTransformation transformation;//glide用它来改变图形的形状
        private ImageView[] imageViews;
        private boolean isCrossFade;//是否使用淡入淡出过渡动画
        private boolean isCenterCrop;//是否将图片剪切为 CenterCrop
        private boolean isCircle;//是否将图片剪切为圆形
        private boolean isClearMemory;//清理内存缓存
        private boolean isClearDiskCache;//清理本地缓存

        private Builder() {
        }

        public Builder url(String url) {
            this.url = url;
            return this;
        }

        public Builder placeholder(int placeholder) {
            this.placeholder = placeholder;
            return this;
        }

        public Builder errorPic(int errorPic) {
            this.errorPic = errorPic;
            return this;
        }

        public Builder fallback(int fallback) {
            this.fallback = fallback;
            return this;
        }

        public Builder imageView(ImageView imageView) {
            this.imageView = imageView;
            return this;
        }

        public Builder cacheStrategy(@CacheStrategy.Strategy int cacheStrategy) {
            this.cacheStrategy = cacheStrategy;
            return this;
        }

        public Builder imageRadius(int imageRadius) {
            this.imageRadius = imageRadius;
            return this;
        }

        public Builder blurValue(int blurValue) { //blurValue 建议设置为 15
            this.blurValue = blurValue;
            return this;
        }

        /**
         * 给图片添加 Glide 独有的 BitmapTransformation
         * <p>
         * 因为 BitmapTransformation 是 Glide 独有的类, 所以如果 BitmapTransformation 出现在 {@link ImageConfigImpl} 中
         * 会使 {ImageLoader} 难以切换为其他图片加载框架, 在 {@link ImageConfigImpl} 中只能配置基础类型和 Android 包里的类
         * 此 API 会在后面的版本中被删除, 请使用其他 API 替代
         *
         * @param transformation {@link BitmapTransformation}
         * @deprecated 请使用 {@link #isCircle()}, {@link #isCenterCrop()}, {@link #isImageRadius()} 替代
         * 如果有其他自定义 BitmapTransformation 的需求, 请自行扩展 {BaseImageLoaderStrategy}
         */
        @Deprecated
        public Builder transformation(BitmapTransformation transformation) {
            this.transformation = transformation;
            return this;
        }

        public Builder imageViews(ImageView... imageViews) {
            this.imageViews = imageViews;
            return this;
        }

        public Builder isCrossFade(boolean isCrossFade) {
            this.isCrossFade = isCrossFade;
            return this;
        }

        public Builder isCenterCrop(boolean isCenterCrop) {
            this.isCenterCrop = isCenterCrop;
            return this;
        }

        public Builder isCircle(boolean isCircle) {
            this.isCircle = isCircle;
            return this;
        }

        public Builder isClearMemory(boolean isClearMemory) {
            this.isClearMemory = isClearMemory;
            return this;
        }

        public Builder isClearDiskCache(boolean isClearDiskCache) {
            this.isClearDiskCache = isClearDiskCache;
            return this;
        }

        public ImageConfigImpl build() {
            return new ImageConfigImpl(this);
        }
    }

}
