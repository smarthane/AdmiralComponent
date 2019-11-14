package com.smarthane.admiral.core.http.imageloader;

import android.widget.ImageView;

/**
 * @author smarthane
 * @time 2019/10/27 14:06
 * @describe 这里是图片加载配置信息的基类,定义一些所有图片加载框架都可以用的通用参数
 */
public class ImageConfig {

    protected String url;
    protected ImageView imageView;
    // 占位符
    protected int placeholder;
    // 错误占位符
    protected int errorPic;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }

    public int getPlaceholder() {
        return placeholder;
    }

    public void setPlaceholder(int placeholder) {
        this.placeholder = placeholder;
    }

    public int getErrorPic() {
        return errorPic;
    }

    public void setErrorPic(int errorPic) {
        this.errorPic = errorPic;
    }
}
