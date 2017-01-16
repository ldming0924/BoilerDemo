package com.kawakp.demingliu.boilerdemo.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;


import com.kawakp.demingliu.boilerdemo.R;
import com.kawakp.demingliu.boilerdemo.base.BaseActivity;
import com.kawakp.demingliu.boilerdemo.http.OkHttpHelper;
import com.kawakp.demingliu.boilerdemo.http.SpotsCallBack;
import com.kawakp.demingliu.boilerdemo.utils.ActivityManager;
import com.kawakp.demingliu.boilerdemo.utils.NetUtils;
import com.kawakp.demingliu.boilerdemo.utils.PathUtils;
import com.kawakp.demingliu.boilerdemo.utils.SharedPerferenceHelper;
import com.kawakp.demingliu.boilerdemo.widget.systembar.StatusBarUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;
import okhttp3.Response;

/**
 * Created by deming.liu on 2017/1/11.
 */

public class LoginActivity extends BaseActivity {
    @Bind(R.id.editText_username)
    EditText edtUserName;
    @Bind(R.id.editText_password)
    EditText edtUserPwd;
    @Bind(R.id.checkBox_remember_password)
    CheckBox remember_password;
    @Bind(R.id.checkBox_auto_login)
    CheckBox auto_login;


    @Override
    public int setContentViewId() {
        return R.layout.activity_login;
    }

    @Override
    public void initViews(Bundle savedInstanceState) {
        ActivityManager.getInstance().addActivity(this);
        StatusBarUtil.setColor(this, getResources().getColor(R.color.white),0);
        String str1 = SharedPerferenceHelper.getRememberPwd(LoginActivity.this);
        String str2 = SharedPerferenceHelper.getRememberAuto(LoginActivity.this);
        SharedPreferences sharedPreferences = SharedPerferenceHelper.getUserMessage(LoginActivity.this);
        String userName = sharedPreferences.getString("username", null);
        String passWord = sharedPreferences.getString("password", null);

        //设置记住密码与自动登录
        setUser(str1, userName, passWord,str2);
        //设置选择状态
        setIsCheck(str2);
        //保存选择的状态
        saveIsCheck();

    }

    private void setIsCheck(String str2) {
        if (str2 != null && str2.equals("true")) {
            auto_login.setChecked(true);

        } else {
            auto_login.setChecked(false);
        }
    }

    private void setUser(String str1, String userName, String passWord,String str2) {
        if (str1 != null && str1.equals("true")) {
            remember_password.setChecked(true);
            edtUserName.setText(userName);
            edtUserPwd.setText(passWord);
        }else if (str2 != null && str2.equals("true")){
            auto_login.setChecked(true);
            edtUserName.setText(userName);
            edtUserPwd.setText(passWord);
        }else {
            remember_password.setChecked(false);

        }
    }

    protected void saveIsCheck() {
        //是否记住密码
        savaIsCheckRemenberPwd();
        //是否自动登录
        savaIsCheckAutoLogin();
    }

    private void savaIsCheckAutoLogin() {
        auto_login.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    boolean b1 = SharedPerferenceHelper.saveRememberAuto(LoginActivity.this, "true");
                } else {
                    boolean b1 = SharedPerferenceHelper.saveRememberAuto(LoginActivity.this, "false");
                }
            }
        });
    }

    private void savaIsCheckRemenberPwd() {
        remember_password.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    boolean b1 = SharedPerferenceHelper.saveRememberPwd(LoginActivity.this, "true");
                } else {
                    boolean b1 = SharedPerferenceHelper.saveRememberPwd(LoginActivity.this, "false");
                }
            }
        });
    }

    @OnClick(R.id.button_login)
    public void onClick(View view){
        switch (view.getId()){
            case R.id.button_login:
                if (TextUtils.isEmpty(edtUserName.getText().toString())) {
                    Toast.makeText(LoginActivity.this, "请输入账号",Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(edtUserPwd.getText().toString())) {
                    Toast.makeText(LoginActivity.this, "请输入密码",Toast.LENGTH_SHORT).show();
                } else {
                    if (NetUtils.isConnected(LoginActivity.this)) {

                        Map<String, Object> map = new HashMap<String, Object>();
                        map.put("username", edtUserName.getText().toString());
                        map.put("password", edtUserPwd.getText().toString());

                        OkHttpHelper okHttpHelper = OkHttpHelper.getInstance(LoginActivity.this);
                        okHttpHelper.post(PathUtils.LOGIN_PATH, map, new SpotsCallBack<String>(LoginActivity.this) {

                            @Override
                            public void onSuccess(Response response, String s) {
                                Log.d("TAG", "登录返回数据:" + s);
                                if (s == null) {
                                    Toast.makeText(LoginActivity.this, "登录失败，请检查网络", Toast.LENGTH_SHORT).show();

                                } else {
                                    JSONObject object = null;
                                    try {
                                        object = new JSONObject(s);
                                        if (s.contains("\"error\"")) {
                                            Toast.makeText(LoginActivity.this, object.getString("error"), Toast.LENGTH_SHORT).show();

                                        } else {
                                            String username = object.getString("username");
                                            boolean b = SharedPerferenceHelper.saveUserMessage(LoginActivity.this, username, edtUserPwd.getText().toString());
                                            boolean bb = SharedPerferenceHelper.saveOrgId(LoginActivity.this, object.getString("orgId"));
                                            if (b && bb) {
                                                Intent intent_Login = new Intent(LoginActivity.this, MainActivity.class);
                                                startActivity(intent_Login);
                                                finish();
                                            } else {
                                                Toast.makeText(LoginActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }
                            }

                            @Override
                            public void onError(Response response, int code, Exception e) {
                                Log.d("TAG", e.toString());
                            }
                        });
                    }else {
                        Toast.makeText(LoginActivity.this, "网络连接失败，请检查网络", Toast.LENGTH_SHORT).show();
                    }
                }

                break;
        }
    }



    //点击两次退出
    private long firstTime = 0;

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                long secondTime = System.currentTimeMillis();
                if (secondTime - firstTime > 2000) { //如果两次按键时间间隔大于2秒，则不退出
                    Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                    firstTime = secondTime;//更新firstTime
                    return true;
                } else { //两次按键小于2秒时，退出应用
                    ActivityManager.getInstance().exit();

                }
                break;
        }
        return super.onKeyUp(keyCode, event);
    }
}
