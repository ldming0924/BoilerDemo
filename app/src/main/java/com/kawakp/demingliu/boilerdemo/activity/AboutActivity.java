package com.kawakp.demingliu.boilerdemo.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kawakp.demingliu.boilerdemo.R;
import com.kawakp.demingliu.boilerdemo.base.BaseActivity;
import com.kawakp.demingliu.boilerdemo.http.OkHttpHelper;
import com.kawakp.demingliu.boilerdemo.http.SimpleCallback;
import com.kawakp.demingliu.boilerdemo.service.UpdateService;
import com.kawakp.demingliu.boilerdemo.utils.ActivityManager;
import com.kawakp.demingliu.boilerdemo.utils.PathUtils;
import com.kawakp.demingliu.boilerdemo.utils.SystemVerdonCode;
import com.kawakp.demingliu.boilerdemo.widget.CommonDialog;

import org.json.JSONException;

import butterknife.Bind;
import butterknife.OnClick;
import okhttp3.Response;

/**
 * Created by deming.liu on 2017/1/11.
 */

public class AboutActivity extends BaseActivity {
    @Bind(R.id.tv_version_name)
    TextView tvVersionName;

    private int sysVersionCode; //本地app版本
    private String sysvVersionName;
    private OkHttpHelper okHttpHelper;
    private String appName;


    @Override
    public int setContentViewId() {
        return R.layout.activity_about;
    }

    @Override
    public void initViews(Bundle savedInstanceState) {
        ActivityManager.getInstance().addActivity(this);
        initData();
    }

    private void initData() {
        //获取当前版本号
        sysVersionCode = SystemVerdonCode.getAppVersionCode(this);
        sysvVersionName = SystemVerdonCode.getAppVersionName(this);

        tvVersionName.setText("v "+sysvVersionName);
    }

    @OnClick({R.id.lin_back,R.id.button_exit,R.id.rel_update,R.id.rel_about_us})
    public void onClick(View view){
        switch (view.getId()) {
            case R.id.lin_back:
                finish();
                break;
            case R.id.button_exit:
                startActivity(new Intent(AboutActivity.this,LoginActivity.class));
                finish();
                break;
            case R.id.rel_update:
                //获取云端code
                getAppMessage();
                break;
            case R.id.rel_about_us:
                startActivity(new Intent(AboutActivity.this,AboutUsActivity.class));
                break;
        }
    }

    /**
     * 获取app信息
     */
    private void getAppMessage() {
        okHttpHelper = OkHttpHelper.getInstance(AboutActivity.this);
        okHttpHelper.get(PathUtils.APP_MSG_PATH, new SimpleCallback<String>(AboutActivity.this) {

            @Override
            public void onSuccess(Response response, String s) {
                Log.d("TAG","app信息:"+s);
                if (s != null && !s.equals("")) {
                    org.json.JSONObject object = null;
                    try {
                        object = new org.json.JSONObject(s);
                        int versionCode = object.getInt("versionCode");
                        appName = object.getString("appName");
                        int code = SystemVerdonCode.getAppVersionCode(AboutActivity.this);
                        if (versionCode > code) {
                            showNoticeDialog();
                        }else {
                            Toast.makeText(AboutActivity.this,"已经只最新版本",Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }else {
                    Log.d("TAG","app信息:"+s);
                }
            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }
        });
    }

    /**
     * 显示软件更新对话框
     */
    private void showNoticeDialog() {

        CommonDialog.Builder builder = new CommonDialog.Builder(this);
        builder.setTitle("升级提示");
        builder.setMessage("检测到新版本，立即更新吗");
        builder.setPositiveButton("更新", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                //判断权限
                if (Build.VERSION.SDK_INT >= 23) {
                    //判断权限
                    if (ContextCompat.checkSelfPermission(AboutActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        //用户没有授予，做权限申请
                        ActivityCompat.requestPermissions(AboutActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
                    } else {
                        dialog.dismiss();
                        startUpdateService();
                    }
                } else {
                    // 显示下载对话框
                    // checkUpdate();
                    dialog.dismiss();
                    //通知栏下载提示
                    startUpdateService();
                }
            }

        });
        builder.setNegativeButton("稍候更新", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

            }
        });
        builder.create().show();
    }

    /**
     * 启动更新下载服务
     */
    private void startUpdateService() {
        String urlPath = "http://kawakp.chinclouds.com:60034/userconsle/clientApps/" + appName + "/file";
        Intent intent = new Intent(AboutActivity.this, UpdateService.class);
        intent.putExtra("apkUrl", urlPath);
        intent.putExtra("TIME", System.currentTimeMillis() + "");
        startService(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,  String[] permissions,  int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case 0:
                //权限回调处理
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    //checkUpdate();
                    startUpdateService();
                }else {
                    //用户拒绝权限申请
                    Toast.makeText(AboutActivity.this,"拒绝安装",Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
