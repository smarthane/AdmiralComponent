package com.smarthane.admiral.core.http.imageloader.glide;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author smarthane
 * @time 2019/10/27 14:29
 * @describe
 */
public interface CacheStrategy {

    int ALL = 0;

    int NONE  = 1;

    int RESOURCE  = 2;

    int DATA  = 3;

    int AUTOMATIC  = 4;

    @IntDef({ALL, NONE, RESOURCE, DATA, AUTOMATIC})
    @Retention(RetentionPolicy.SOURCE)
    @interface Strategy{}
}
