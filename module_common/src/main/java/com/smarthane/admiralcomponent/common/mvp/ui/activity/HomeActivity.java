package com.smarthane.admiralcomponent.common.mvp.ui.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.smarthane.admiral.component.common.sdk.core.RouterHub;
import com.smarthane.admiral.component.common.sdk.http.eapi.EapiDownloadCallback;
import com.smarthane.admiral.component.common.sdk.http.eapi.EapiUploadCallback;
import com.smarthane.admiral.component.common.sdk.http.eapi.EasyApiHelper;
import com.smarthane.admiral.component.common.sdk.http.eapi.request.EapiDownloadRequest;
import com.smarthane.admiral.component.common.sdk.http.eapi.request.EapiUploadRequest;
import com.smarthane.admiral.core.base.BaseActivity;
import com.smarthane.admiral.core.util.LogUtils;
import com.smarthane.admiralcomponent.common.R;
import com.smarthane.admiralcomponent.common.mvp.contract.HomeContract;
import com.smarthane.admiralcomponent.common.mvp.presenter.HomePresenter;

import java.io.File;

/**
 * @author smarthane
 * @time 2019/11/10 16:00
 * @describe 首页
 */
@Route(path = RouterHub.COMMON_HOMEACTIVITY)
public class HomeActivity extends BaseActivity implements HomeContract.View {

    private Button btnStartDownload;
    private TextView tvProgress;
    private Button btnStartUpload;
    private TextView tvProgressUpload;


    @Override
    public int setLayoutResouceId() {
        return R.layout.common_activity_home;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        btnStartDownload = findViewById(R.id.btn_start_download);
        tvProgress = findViewById(R.id.tv_progress_download);
        btnStartUpload = findViewById(R.id.btn_start_upload);
        tvProgressUpload = findViewById(R.id.tv_progress_upload);
    }

    @Override
    protected void initPresenter() {
        mPresenter = HomePresenter.build(this);
    }


    @Override
    protected void bindListener() {
        btnStartDownload.setOnClickListener(mOnClickListener);
        btnStartUpload.setOnClickListener(mOnClickListener);
    }

    @Override
    protected void execBusiness() {
        // do nothing
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.btn_start_download) {
                startDownload();
            } else if (v.getId() == R.id.btn_start_upload) {
                startUpload();
            }
        }
    };

    private void startDownload() {
        EapiDownloadRequest request = new EapiDownloadRequest(this);
        request.setFileName("wx13.apk");
        request.setFileUrl("http://dldir1.qq.com/weixin/android/weixin667android1320.apk");
        request.enableProgres();
        EasyApiHelper.downloadFile(request, new EapiDownloadCallback() {

            @Override
            public void onStart() {
                LogUtils.debugInfo("EasyApiHelper.downloadFile onStart ");
                btnStartDownload.setEnabled(false);
            }

            @Override
            public void onProgress(long read, long count, boolean done) {
                LogUtils.debugInfo("EasyApiHelper.downloadFile read: " + read + " count: " + count + " done : " + done);
            }

            @Override
            public void onProgress(int percent) {
                LogUtils.debugInfo("EasyApiHelper.downloadFile percent: " + percent);
                tvProgress.setText("下载进度:" + percent + "%");
            }

            @Override
            public void onComplete(String filePath) {
                LogUtils.debugInfo("EasyApiHelper.downloadFile onComplete " + filePath);
                tvProgress.setText("下载完成:" + filePath);
                btnStartDownload.setEnabled(true);
            }

            @Override
            public void onFail(Throwable t) {
                LogUtils.debugInfo("EasyApiHelper.downloadFile onFail");
                tvProgress.setText("下载失败:" + t.getMessage());
            }
        });
    }

    private void startUpload() {
        EapiUploadRequest request = new EapiUploadRequest(this);
        request.setUploadUrl("http://192.168.1.57:8010/doUpload");
        request.setUploadFile(new File("/storage/emulated/0/Android/data/com.smarthane.admiralcomponent.common/cache/eapi_file_cache/wx13.apk"));
        request.enableProgres();
        EasyApiHelper.uploadFile(request, new EapiUploadCallback() {

            @Override
            public void onStart() {
                LogUtils.debugInfo("EasyApiHelper.uploadFile onStart ");
                btnStartUpload.setEnabled(false);
            }

            @Override
            public void onProgress(long read, long count, boolean done) {
                LogUtils.debugInfo("EasyApiHelper.uploadFile read: " + read + " count: " + count + " done : " + done);
            }

            @Override
            public void onProgress(int percent) {
                LogUtils.debugInfo("EasyApiHelper.uploadFile percent: " + percent);
                tvProgressUpload.setText("上传进度:" + percent + "%");
            }

            @Override
            public void onComplete(String result) {
                LogUtils.debugInfo("EasyApiHelper.uploadFile onComplete " + result);
                tvProgressUpload.setText("上传完成:" + result);
                btnStartUpload.setEnabled(true);
            }

            @Override
            public void onFail(Throwable t) {
                LogUtils.debugInfo("EasyApiHelper.uploadFile onFail");
                tvProgressUpload.setText("上传失败:" + t.getMessage());
            }
        });
    }
}
