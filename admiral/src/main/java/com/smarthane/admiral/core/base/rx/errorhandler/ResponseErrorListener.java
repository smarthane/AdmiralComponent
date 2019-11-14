package com.smarthane.admiral.core.base.rx.errorhandler;

import android.content.Context;

/**
 * @author smarthane
 * @time 2019/11/10 14:19
 * @describe
 */
public interface ResponseErrorListener {

    void handleResponseError(Context context, Throwable t);

    ResponseErrorListener EMPTY = new ResponseErrorListener() {
        @Override
        public void handleResponseError(Context context, Throwable t) {
            // do nothing
        }
    };

}
