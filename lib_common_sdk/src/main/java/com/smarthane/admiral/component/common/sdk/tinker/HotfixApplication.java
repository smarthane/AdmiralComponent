package com.smarthane.admiral.component.common.sdk.tinker;

import com.tencent.tinker.loader.app.TinkerApplication;
import com.tencent.tinker.loader.shareutil.ShareConstants;

/**
 * @author smarthane
 * @time 2019/11/10 19:27
 * @describe 集成Tinker热修复，重写Admiral中的Application, application for tinker life cycle
 */
public class HotfixApplication extends TinkerApplication {

    public HotfixApplication() {
        super(ShareConstants.TINKER_ENABLE_ALL,
                "com.smarthane.admiral.component.common.sdk.tinker.TinkerApplicationLike",
                "com.tencent.tinker.loader.TinkerLoader",
                false);
    }
}
