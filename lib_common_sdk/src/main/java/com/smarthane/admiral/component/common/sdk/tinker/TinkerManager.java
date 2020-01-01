package com.smarthane.admiral.component.common.sdk.tinker;

import com.smarthane.admiral.component.common.sdk.tinker.reporter.AdmiralLoadReporter;
import com.smarthane.admiral.component.common.sdk.tinker.reporter.AdmiralPatchListener;
import com.smarthane.admiral.component.common.sdk.tinker.reporter.AdmiralPatchReporter;
import com.smarthane.admiral.component.common.sdk.tinker.service.AdmiralResultService;
import com.smarthane.admiral.core.util.LogUtils;
import com.tencent.tinker.entry.ApplicationLike;
import com.tencent.tinker.lib.listener.PatchListener;
import com.tencent.tinker.lib.patch.AbstractPatch;
import com.tencent.tinker.lib.patch.UpgradePatch;
import com.tencent.tinker.lib.reporter.LoadReporter;
import com.tencent.tinker.lib.reporter.PatchReporter;
import com.tencent.tinker.lib.tinker.TinkerInstaller;
import com.tencent.tinker.lib.util.TinkerLog;
import com.tencent.tinker.lib.util.UpgradePatchRetry;

/**
 * @author smarthane
 * @time 2019/11/10 19:28
 * @describe
 */
public class TinkerManager {

    private static final String TAG = "Tinker.TinkerManager";

    private static ApplicationLike applicationLike;
    private static boolean isInstalled = false;

    public static void setTinkerApplicationLike(ApplicationLike appLike) {
        applicationLike = appLike;
    }

    public static ApplicationLike getTinkerApplicationLike() {
        return applicationLike;
    }

    public static void setUpgradeRetryEnable(boolean enable) {
        UpgradePatchRetry.getInstance(applicationLike.getApplication()).setRetryEnable(enable);
    }

    public static void simpleInstallTinker(ApplicationLike appLike) {
        if (isInstalled) {
            LogUtils.debugInfo(TAG, "Install tinker, but has installed, ignore!");
            return;
        }
        TinkerInstaller.install(appLike);
        isInstalled = true;
    }

    /**
     * you can specify all class you want.
     * sometimes, you can only install tinker in some process you want!
     *
     * @param appLike
     */
    public static void installTinker(ApplicationLike appLike) {
        if (isInstalled) {
            TinkerLog.w(TAG, "install tinker, but has installed, ignore");
            return;
        }
        //or you can just use DefaultLoadReporter
        LoadReporter loadReporter = new AdmiralLoadReporter(appLike.getApplication());
        //or you can just use DefaultPatchReporter
        PatchReporter patchReporter = new AdmiralPatchReporter(appLike.getApplication());
        //or you can just use DefaultPatchListener
        PatchListener patchListener = new AdmiralPatchListener(appLike.getApplication());
        //you can set your own upgrade patch if you need
        AbstractPatch upgradePatchProcessor = new UpgradePatch();

        TinkerInstaller.install(
                appLike,
                loadReporter,
                patchReporter,
                patchListener,
                AdmiralResultService.class,
                upgradePatchProcessor
        );

        isInstalled = true;
    }
}