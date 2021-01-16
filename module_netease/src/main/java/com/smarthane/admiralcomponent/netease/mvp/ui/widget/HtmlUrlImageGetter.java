package com.smarthane.admiralcomponent.netease.mvp.ui.widget;

import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.smarthane.admiral.core.base.AppComponent;
import com.smarthane.admiral.core.mvp.IView;
import com.smarthane.admiral.core.util.RxLifecycleUtils;
import com.smarthane.admiralcomponent.netease.R;
import com.smarthane.admiralcomponent.netease.mvp.model.api.service.NewsService;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.schedulers.Schedulers;
import com.smarthane.admiral.core.base.rx.errorhandler.ErrorHandleSubscriber;
import okhttp3.ResponseBody;

/**
 * @author smarthane
 * @time 2019/11/10 16:10
 * @describe TextView 显示HTML图片加载器
 */
public class HtmlUrlImageGetter implements Html.ImageGetter {
    private TextView mTextView;
    private String mNewsBody;
    private int mPicCount;
    private int mPicTotal;
    private static final String mFilePath = AppComponent
            .get()
            .fetchCacheFile()
            .getPath();

    public HtmlUrlImageGetter(TextView textView, String newsBody, int picTotal) {
        mTextView = textView;
        mNewsBody = newsBody;
        mPicTotal = picTotal;
    }

    @Override
    public Drawable getDrawable(final String source) {
        Drawable drawable;
        File file = new File(mFilePath, source.hashCode() + "");
        if (file.exists()) {
            mPicCount++;
            drawable = getDrawableFromDisk(file);
        } else {
            drawable = getDrawableFromNet(source);
        }
        return drawable;
    }

    @Nullable
    private Drawable getDrawableFromDisk(File file) {
        Drawable drawable = Drawable.createFromPath(file.getAbsolutePath());
        if (drawable != null) {
            int picHeight = calculatePicHeight(drawable);
            drawable.setBounds(0, 0, mTextView.getWidth(), picHeight);
        }
        return drawable;
    }

    private int calculatePicHeight(Drawable drawable) {
        float imgWidth = drawable.getIntrinsicWidth();
        float imgHeight = drawable.getIntrinsicHeight();
        float rate = imgHeight / imgWidth;
        return (int) (mTextView.getWidth() * rate);
    }

    @NonNull
    private Drawable getDrawableFromNet(final String source) {
        AppComponent.get()
                .fetchRepositoryManager()
                .obtainRetrofitService(NewsService.class)
                .getNewsBodyHtmlPhoto(source)
                .unsubscribeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Function<ResponseBody, Boolean>() {
                    @Override
                    public Boolean apply(ResponseBody responseBody) throws Exception {
                        return WritePicToDisk(responseBody, source);
                    }
                })
                // 使用 Rxlifecycle,使 Disposable 和 Activity 一起销毁
                .compose(RxLifecycleUtils.bindToLifecycle((IView) mTextView.getContext()))
                .subscribe(new ErrorHandleSubscriber<Boolean>(AppComponent
                .get()
                .fetchRxErrorHandler()) {
                    @Override
                    public void onNext(Boolean aBoolean) {
                        mPicCount++;
                        if (aBoolean && (mPicCount == mPicTotal - 1)) {
                            mTextView.setText(Html.fromHtml(
                                    mNewsBody,
                                    HtmlUrlImageGetter.this,
                                    null)
                            );
                        }
                    }
                });
        return createPicPlaceholder();
    }

    @NonNull
    private Boolean WritePicToDisk(ResponseBody response, String source) {
        File file = new File(mFilePath, source.hashCode() + "");
        InputStream in = null;
        FileOutputStream out = null;
        try {
            in = response.byteStream();
            out = new FileOutputStream(file);
            byte[] buffer = new byte[1024];
            int len;
            while ((len = in.read(buffer)) != -1) {
                out.write(buffer, 0, len);
            }
            return true;
        } catch (Exception e) {
            return false;
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
            }
        }
    }

    @SuppressWarnings("deprecation")
    @NonNull
    private Drawable createPicPlaceholder() {
        Drawable drawable;
        int color = R.color.public_white;
        drawable = new ColorDrawable(AppComponent
                .get()
                .fetchApplication()
                .getResources()
                .getColor(color)
        );
        drawable.setBounds(
                0,
                0,
                mTextView.getWidth(),
                mTextView.getWidth() / 3
        );
        return drawable;
    }
}