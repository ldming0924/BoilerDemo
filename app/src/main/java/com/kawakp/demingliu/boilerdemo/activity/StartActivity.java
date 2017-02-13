package com.kawakp.demingliu.boilerdemo.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.kawakp.demingliu.boilerdemo.R;
import com.kawakp.demingliu.boilerdemo.base.BaseActivity;
import com.kawakp.demingliu.boilerdemo.http.OkHttpHelper;
import com.kawakp.demingliu.boilerdemo.http.SimpleCallback;
import com.kawakp.demingliu.boilerdemo.utils.ActivityManager;
import com.kawakp.demingliu.boilerdemo.utils.AnimationUtil;
import com.kawakp.demingliu.boilerdemo.utils.IToast;
import com.kawakp.demingliu.boilerdemo.utils.NetUtils;
import com.kawakp.demingliu.boilerdemo.utils.PathUtils;
import com.kawakp.demingliu.boilerdemo.utils.SharedPerferenceHelper;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Response;

/**
 * Created by deming.liu on 2017/1/12.
 */

public class StartActivity extends BaseActivity {
    private static final long DELAY_TIME = 2000L;
    private String userName;
    private String passWord;

    private String auto;

    @Override
    public int setContentViewId() {
        return R.layout.activity_start;
    }

    @Override
    public void initViews(Bundle savedInstanceState) {
        ActivityManager.getInstance().addActivity(this);
        SharedPreferences sharedPreferences = SharedPerferenceHelper.getUserMessage(StartActivity.this);
        userName = sharedPreferences.getString("username", null);
        passWord = sharedPreferences.getString("password", null);
        auto = SharedPerferenceHelper.getRememberAuto(StartActivity.this);

        doLogin();

    }

    /**
     * 登录
     */
    private void doLogin() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (NetUtils.isConnected(StartActivity.this)) {
                    if (userName == null || passWord == null || auto == null || auto.equals("false")) {
                        startActivity(new Intent(StartActivity.this, LoginActivity.class));
                        AnimationUtil.finishActivityAnimation(StartActivity.this);

                    } else {
                        Map<String, Object> map = new HashMap<String, Object>();
                        map.put("username", userName);
                        map.put("password", passWord);
                        OkHttpHelper okHttpHelper = OkHttpHelper.getInstance(StartActivity.this);
                        okHttpHelper.post(PathUtils.LOGIN_PATH, map, new SimpleCallback<String>(StartActivity.this) {

                            @Override
                            public void onSuccess(Response response, String s) {
                                if (s == null) {
                                    IToast.showToast(StartActivity.this, "登录失败，请检查网络");
                                    startActivity(new Intent(StartActivity.this, LoginActivity.class));
                                    AnimationUtil.finishActivityAnimation(StartActivity.this);
                                } else {
                                    JSONObject object = null;
                                    try {
                                        object = new JSONObject(s);
                                        if (s.contains("\"error\"")) {
                                            IToast.showToast(StartActivity.this, object.getString("error"));
                                        } else {
                                            String username = object.getString("username");
                                            boolean b = SharedPerferenceHelper.saveUserMessage(StartActivity.this, username, passWord);
                                            if (b) {
                                                Intent intent_Login = new Intent(StartActivity.this, MainActivity.class);
                                                startActivity(intent_Login);
                                                AnimationUtil.finishActivityAnimation(StartActivity.this);
                                                finish();
                                            }
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }
                            }

                            @Override
                            public void onError(Response response, int code, Exception e) {
                                Log.d("TAG", code + "  " + e.toString());
                                startActivity(new Intent(StartActivity.this, LoginActivity.class));
                                AnimationUtil.finishActivityAnimation(StartActivity.this);
                            }
                        });

                    }
                } else {
                    IToast.showToast(StartActivity.this, "登录失败，请检查网络");
                    startActivity(new Intent(StartActivity.this, LoginActivity.class));
                    AnimationUtil.finishActivityAnimation(StartActivity.this);
                }
            }
        }, DELAY_TIME);
    }
}
