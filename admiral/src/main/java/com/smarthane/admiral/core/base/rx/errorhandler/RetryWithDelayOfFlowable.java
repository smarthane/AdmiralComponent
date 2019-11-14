package com.smarthane.admiral.core.base.rx.errorhandler;

import android.util.Log;

import androidx.annotation.NonNull;

import org.reactivestreams.Publisher;

import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.functions.Function;

/**
 * @author smarthane
 * @time 2019/11/10 14:26
 * @describe
 */
public class RetryWithDelayOfFlowable implements Function<Flowable<Throwable>, Publisher<?>> {

    public final String TAG = this.getClass().getSimpleName();
    private final int maxRetries;
    private final int retryDelaySecond;
    private int retryCount;

    public RetryWithDelayOfFlowable(int maxRetries, int retryDelaySecond) {
        this.maxRetries = maxRetries;
        this.retryDelaySecond = retryDelaySecond;
    }

    @Override
    public Publisher<?> apply(@NonNull Flowable<Throwable> throwableFlowable) throws Exception {
        return throwableFlowable
                .flatMap(new Function<Throwable, Publisher<?>>() {
                    @Override
                    public Publisher<?> apply(@NonNull Throwable throwable) throws Exception {
                        if (++retryCount <= maxRetries) {
                            // When this Observable calls onNext, the original Observable will be retried (i.e. re-subscribed).
                            Log.d(TAG, "Flowable get error, it will try after " + retryDelaySecond
                                    + " second, retry count " + retryCount);
                            return Flowable.timer(retryDelaySecond,
                                    TimeUnit.SECONDS);
                        }
                        // Max retries hit. Just pass the error along.
                        return Flowable.error(throwable);
                    }
                });
    }
}
