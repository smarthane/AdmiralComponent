package com.smarthane.admiral.component.common.sdk.tinker.service;

import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.smarthane.admiral.component.common.sdk.util.TinkerUtils;
import com.smarthane.admiral.core.util.LogUtils;
import com.tencent.tinker.lib.service.DefaultTinkerResultService;
import com.tencent.tinker.lib.service.PatchResult;
import com.tencent.tinker.lib.util.TinkerServiceInternals;

import java.io.File;

/**
 * @author smarthane
 * @time 2019/11/10 19:27
 * @describe
 */
public class AdmiralResultService extends DefaultTinkerResultService {

    private static final String TAG = "Tinker.AdmiralResultService";

    @Override
    public void onPatchResult(final PatchResult result) {
        if (result == null) {
            LogUtils.errorInfo(TAG, "AdmiralResultService received null result!!!!");
            return;
        }
        LogUtils.debugInfo(TAG, String.format("AdmiralResultService receive result: %s", result.toString()));

        //first, we want to kill the recover process
        TinkerServiceInternals.killTinkerPatchServiceProcess(getApplicationContext());

        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (result.isSuccess) {
                    Toast.makeText(getApplicationContext(), "patch success, please restart process", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "patch fail, please check reason", Toast.LENGTH_LONG).show();
                }
            }
        });
        // is success and newPatch, it is nice to delete the raw file, and restart at once
        // for old patch, you can't delete the patch file
        if (result.isSuccess) {
            deleteRawPatchFile(new File(result.rawPatchFilePath));

            //not like TinkerResultService, I want to restart just when I am at background!
            //if you have not install tinker this moment, you can use TinkerApplicationHelper api
            if (checkIfNeedKill(result)) {
                if (TinkerUtils.isBackground()) {
                    LogUtils.debugInfo(TAG, "it is in background, just restart process");
                    restartProcess();
                } else {
                    //we can wait process at background, such as onAppBackground
                    //or we can restart when the screen off
                    LogUtils.debugInfo(TAG, "tinker wait screen to restart process");
                    new TinkerUtils.ScreenState(getApplicationContext(), new TinkerUtils.ScreenState.IOnScreenOff() {
                        @Override
                        public void onScreenOff() {
                            restartProcess();
                        }
                    });
                }
            } else {
                LogUtils.debugInfo(TAG, "I have already install the newly patch version!");
            }
        }
    }

    /**
     * you can restart your process through service or broadcast
     */
    private void restartProcess() {
        LogUtils.debugInfo(TAG, "app is background now, i can kill quietly");
        //you can send service or broadcast intent to restart your process
        android.os.Process.killProcess(android.os.Process.myPid());
    }
}
