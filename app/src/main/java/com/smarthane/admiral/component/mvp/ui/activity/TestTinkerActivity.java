package com.smarthane.admiral.component.mvp.ui.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.smarthane.admiral.component.common.sdk.core.RouterHub;
import com.smarthane.admiral.component.common.sdk.util.TinkerUtils;
import com.smarthane.admiral.core.base.BaseActivity;
import com.smarthane.admiral.core.util.LogUtils;
import com.smarthane.admiralcomponent.R;
import com.tencent.tinker.lib.library.TinkerLoadLibrary;
import com.tencent.tinker.lib.tinker.Tinker;
import com.tencent.tinker.lib.tinker.TinkerInstaller;
import com.tencent.tinker.loader.shareutil.ShareTinkerInternals;

/**
 * @author smarthane
 * @time 2019/11/10 21:22
 * @describe 测试Tinker
 */
@Route(path = RouterHub.APP_TEST_TINKERACTIVITY)
public class TestTinkerActivity extends BaseActivity {

    private TextView tvMsg;
    private Button btnLoadPatch;
    private Button btnLoadLibrary;
    private Button btnCleanPatch;
    private Button btnKillSelf;
    private Button btnShowInfo;

    @Override
    public int setLayoutResouceId() {
        return R.layout.activity_tinker;
    }


    @Override
    protected void initData() {
        super.initData();
        askForRequiredPermissions();
        checkARKHotRunning();
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        tvMsg = findViewById(R.id.tv_message);
        btnLoadPatch = findViewById(R.id.btn_loadpatch);
        btnLoadLibrary = findViewById(R.id.btn_loadlibrary);
        btnCleanPatch = findViewById(R.id.btn_cleanpatch);
        btnKillSelf = findViewById(R.id.btn_killself);
        btnShowInfo = findViewById(R.id.btn_showinfo);
    }

    @Override
    protected void bindListener() {
        super.bindListener();
        btnLoadPatch.setOnClickListener(mOnClickListener);
        btnLoadLibrary.setOnClickListener(mOnClickListener);
        btnCleanPatch.setOnClickListener(mOnClickListener);
        btnKillSelf.setOnClickListener(mOnClickListener);
        btnShowInfo.setOnClickListener(mOnClickListener);
    }

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.btn_loadpatch) {
                TinkerInstaller.onReceiveUpgradePatch(
                        getApplicationContext(),
                        Environment.getExternalStorageDirectory().getAbsolutePath() + "/patch_signed_7zip.apk"
                );
            } else if (v.getId() == R.id.btn_loadlibrary) {
                // #method 1, hack classloader library path
                TinkerLoadLibrary.installNavitveLibraryABI(getApplicationContext(), "armeabi");
                System.loadLibrary("stlport_shared");
                // #method 2, for lib/armeabi, just use TinkerInstaller.loadLibrary
                // TinkerLoadLibrary.loadArmLibrary(getApplicationContext(), "stlport_shared");
                // #method 3, load tinker patch library directly
                // TinkerInstaller.loadLibraryFromTinker(getApplicationContext(), "assets/x86", "stlport_shared");
            } else if (v.getId() == R.id.btn_cleanpatch) {
                Tinker.with(getApplicationContext()).cleanPatch();
            } else if (v.getId() == R.id.btn_killself) {
                ShareTinkerInternals.killAllOtherProcess(getApplicationContext());
                android.os.Process.killProcess(android.os.Process.myPid());
            } else if (v.getId() == R.id.btn_showinfo) {
                // 修改Toast提示文字进行测试修复
                Toast.makeText(getApplicationContext(), "Hello Tinker !!!! I AM CHANGED!!", Toast.LENGTH_LONG).show();
            }
        }
    };

    private void checkARKHotRunning() {
        boolean isARKHotRunning = ShareTinkerInternals.isArkHotRuning();
        LogUtils.debugInfo(TAG, "ARK HOT Running status = " + isARKHotRunning);
        LogUtils.debugInfo(TAG, "i am on onCreate classloader:" + TestTinkerActivity.class.getClassLoader().toString());
        // test resource change
        LogUtils.debugInfo(TAG, "i am on onCreate string: I am in the base apk");
        LogUtils.debugInfo(TAG, "i am on patch onCreate");
    }

    private void askForRequiredPermissions() {
        if (Build.VERSION.SDK_INT < 23) {
            return;
        }
        if (!hasRequiredPermissions()) {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
        }
    }

    private boolean hasRequiredPermissions() {
        if (Build.VERSION.SDK_INT >= 16) {
            final int res = ContextCompat.checkSelfPermission(this.getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE);
            return res == PackageManager.PERMISSION_GRANTED;
        } else {
            // When SDK_INT is below 16, READ_EXTERNAL_STORAGE will also be granted if WRITE_EXTERNAL_STORAGE is granted.
            final int res = ContextCompat.checkSelfPermission(this.getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
            return res == PackageManager.PERMISSION_GRANTED;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        TinkerUtils.setBackground(false);
    }

    @Override
    protected void onPause() {
        super.onPause();
        TinkerUtils.setBackground(true);
    }
}
