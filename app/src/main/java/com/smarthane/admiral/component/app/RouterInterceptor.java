package com.smarthane.admiral.component.app;

import android.content.Context;

import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.facade.annotation.Interceptor;
import com.alibaba.android.arouter.facade.callback.InterceptorCallback;
import com.alibaba.android.arouter.facade.template.IInterceptor;
import com.alibaba.android.arouter.launcher.ARouter;
import com.smarthane.admiral.core.util.LogUtils;

/**
 * @author smarthane
 * @time 2019/11/10 11:17
 * @describe 声明 {@link ARouter} 拦截器, 可以根据需求自定义拦截逻辑, 比如用户没有登录就拦截其他页面
 */
@Interceptor(priority = 8, name = "RouterInterceptor")
public class RouterInterceptor implements IInterceptor {

    private Context mContext;

    @Override
    public void process(Postcard postcard, InterceptorCallback callback) {
        LogUtils.debugInfo("RouterInterceptor process " + postcard.getPath());
        callback.onContinue(postcard);
        //这里示例的意思是, 如果用户没有登录就只能进入登录注册等划分到 ACCOUNT 分组的页面, 用户进入其他页面将全部被拦截
//        if (postcard.getGroup().equals(RouterHub.ACCOUNT.split("/")[1])
//            callback.onContinue(postcard);
//        } else {
//            callback.onInterrupt(new Exception("用户没有登陆"));
//        }
    }

    @Override
    public void init(Context context) {
        LogUtils.debugInfo("RouterInterceptor init ");
        mContext = context;
    }
}

