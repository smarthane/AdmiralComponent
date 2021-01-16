package com.smarthane.admiral.component.common.sdk.http.eapi.request;

import androidx.annotation.NonNull;

import com.smarthane.admiral.component.common.sdk.http.eapi.EapiUploadCallback;
import com.smarthane.admiral.core.util.LogUtils;

import java.io.IOException;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.functions.Consumer;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.Buffer;
import okio.BufferedSink;
import okio.ForwardingSink;
import okio.Okio;
import okio.Sink;

/**
 * @author smarthane
 * @time 2019/11/10 14:56
 * @describe 封装上传文件的请求body用于重写相关方法实现上传进度
 */
public class EapiUploadRequestBody extends RequestBody {

    private RequestBody requestBody;
    private EapiUploadCallback callback;
    private long lastTime;

    public EapiUploadRequestBody(RequestBody requestBody, EapiUploadCallback callback) {
        this.requestBody = requestBody;
        this.callback = callback;
        if (requestBody == null || callback == null) {
            throw new NullPointerException("this requestBody and callback must not null.");
        }
    }

    @Override
    public MediaType contentType() {
        return requestBody.contentType();
    }

    @Override
    public long contentLength() {
        try {
            return requestBody.contentLength();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return -1;
    }

    @Override
    public void writeTo(@NonNull BufferedSink sink) throws IOException {
        CountingSink countingSink = new CountingSink(sink);
        BufferedSink bufferedSink = Okio.buffer(countingSink);
        requestBody.writeTo(bufferedSink);
        bufferedSink.flush();
    }

    private final class CountingSink extends ForwardingSink {

        /**
         * 当前字节长度
         */
        private long currentLength = 0L;

        /**
         * 总字节长度，避免多次调用contentLength()方法
         */
        private long totalLength = 0L;

        public CountingSink(Sink sink) {
            super(sink);
        }

        @Override
        public void write(@NonNull Buffer source, long byteCount) throws IOException {
            super.write(source, byteCount);
            // 增加当前写入的字节数
            currentLength += byteCount;
            // 获得contentLength的值，后续不再调用
            if (totalLength == 0) {
                totalLength = contentLength();
            }
            long currentTime = System.currentTimeMillis();
            if (currentTime - lastTime >= 100 || lastTime == 0 || currentLength == totalLength) {
                lastTime = currentTime;
                Observable.just(currentLength).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        LogUtils.debugInfo("upload progress currentLength:" + currentLength + ",totalLength:" + totalLength);
                        callback.onProgress(currentLength, totalLength, totalLength == currentLength);
                        callback.onProgress((int)((100.0f * currentLength) / totalLength));
                    }
                });
            }
        }
    }
}
