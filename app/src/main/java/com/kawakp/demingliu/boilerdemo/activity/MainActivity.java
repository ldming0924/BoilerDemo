package com.kawakp.demingliu.boilerdemo.activity;

import android.Manifest;
import android.app.Activity;
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
import android.widget.Toast;

import com.bigkoo.convenientbanner.CBViewHolderCreator;
import com.bigkoo.convenientbanner.ConvenientBanner;
import com.kawakp.demingliu.boilerdemo.R;
import com.kawakp.demingliu.boilerdemo.base.BaseActivity;
import com.kawakp.demingliu.boilerdemo.canstant.Canstant;
import com.kawakp.demingliu.boilerdemo.http.OkHttpHelper;
import com.kawakp.demingliu.boilerdemo.http.SimpleCallback;
import com.kawakp.demingliu.boilerdemo.service.UpdateService;
import com.kawakp.demingliu.boilerdemo.service.WarnService;
import com.kawakp.demingliu.boilerdemo.utils.ActivityManager;
import com.kawakp.demingliu.boilerdemo.utils.LocalImageHolderView;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import com.bigkoo.convenientbanner.ConvenientBanner.Transformer;
import com.kawakp.demingliu.boilerdemo.utils.PathUtils;
import com.kawakp.demingliu.boilerdemo.utils.ServiceHelper;
import com.kawakp.demingliu.boilerdemo.utils.SharedPerferenceHelper;
import com.kawakp.demingliu.boilerdemo.utils.SystemVerdonCode;
import com.kawakp.demingliu.boilerdemo.widget.CommonDialog;
import com.kawakp.demingliu.boilerdemo.widget.systembar.StatusBarUtil;

import org.json.JSONException;

import butterknife.Bind;
import butterknife.OnClick;
import okhttp3.Response;

/**
 * Created by deming.liu on 2016/12/29.
 */

public class MainActivity extends BaseActivity {
    @Bind(R.id.lin_device_manager)
    LinearLayout lin_device_manager;
    @Bind(R.id.lin_real_time_monitoring)
    LinearLayout lin_real_time_monitoring;
    @Bind(R.id.lin_data_analysis)
    LinearLayout lin_data_analysis;
    @Bind(R.id.lin_maintenance)
    LinearLayout lin_maintenance;
    @Bind(R.id.lin_optimization_suggestions)
    LinearLayout lin_optimization_suggestions;
    @Bind(R.id.lin_alarm_management)
    LinearLayout lin_alarm_management;
    @Bind(R.id.lin_about)
    LinearLayout lin_about;

    private ConvenientBanner convenientBanner;//顶部广告栏控件
    private ArrayList<Integer> localImages = new ArrayList<Integer>();
    private List<String> networkImages;
    private String[] images = {"http://img2.imgtn.bdimg.com/it/u=3093785514,1341050958&fm=21&gp=0.jpg",
            "http://img2.3lian.com/2014/f2/37/d/40.jpg",
            "http://d.3987.com/sqmy_131219/001.jpg",
            "http://img2.3lian.com/2014/f2/37/d/39.jpg",
            "http://www.8kmm.com/UploadFiles/2012/8/201208140920132659.jpg",
            "http://f.hiphotos.baidu.com/image/h%3D200/sign=1478eb74d5a20cf45990f9df460b4b0c/d058ccbf6c81800a5422e5fdb43533fa838b4779.jpg",
            "http://f.hiphotos.baidu.com/image/pic/item/09fa513d269759ee50f1971ab6fb43166c22dfba.jpg"
    };


    private OkHttpHelper okHttpHelper;
    private String appName;
    //private ListView listView;
    // private ArrayAdapter transformerArrayAdapter;
    private ArrayList<String> transformerList = new ArrayList<String>();
    private String  device;


    @Override
    public int setContentViewId() {
        return R.layout.activity_function;
    }

    @Override
    public void initViews(Bundle savedInstanceState) {
        ActivityManager.getInstance().addActivity(this);
        StatusBarUtil.setColor(this, getResources().getColor(R.color.stastusbar_bg),0);

        initView();
        init();

    }


    private void initView() {
        convenientBanner = (ConvenientBanner) findViewById(R.id.convenientBanner);
        //  listView = (ListView) findViewById(R.id.listView);
        // transformerArrayAdapter = new ArrayAdapter(this,R.layout.adapter_transformer,transformerList);
        // listView.setAdapter(transformerArrayAdapter);
        // listView.setOnItemClickListener(this);
    }

    private void init() {

        //TODO 判断服务是否在运行
        if (!ServiceHelper.isServiceWork(getApplicationContext(), Canstant.INTENTFILTER)) {
            Intent intent = new Intent(getApplicationContext(), WarnService.class);
            startService(intent);
        }
        getAppMessage();
        loadTestDatas();
        setPges();

    }

    private void initData(){
        device = SharedPerferenceHelper.getDeviceId(MainActivity.this);
    }

    @OnClick({R.id.lin_device_manager, R.id.lin_real_time_monitoring, R.id.lin_data_analysis, R.id.lin_maintenance, R.id.lin_optimization_suggestions, R.id.lin_alarm_management, R.id.lin_about})
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.lin_device_manager:  //跳转到设备管理
                startActivity(new Intent(MainActivity.this,DeviceManagerActivity.class));
                break;
            case R.id.lin_real_time_monitoring://跳转到实时监控
                if (device != null){
                    startActivity(new Intent(MainActivity.this,RealTimeMonActivity.class));
                }else {
                    Toast.makeText(this, "请先选择设备", Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.lin_data_analysis://跳转到数据分析
                if (device != null){
                    startActivity(new Intent(MainActivity.this,DataAnalysisActivity.class));
                }else {
                    Toast.makeText(this, "请先选择设备", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.lin_maintenance://跳转到维修保养
                if (device != null){
                    startActivity(new Intent(MainActivity.this,MaintenanceActivity.class));
                }else {
                    Toast.makeText(this, "请先选择设备", Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.lin_optimization_suggestions://跳转到优化建议
                if (device != null){
                    startActivity(new Intent(MainActivity.this,OptSugActivity.class));
                }else {
                    Toast.makeText(this, "请先选择设备", Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.lin_alarm_management://跳转到报警管理
                if (device != null){
                    startActivity(new Intent(MainActivity.this,AlarmManagerActivity.class));
                }else {
                    Toast.makeText(this, "请先选择设备", Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.lin_about://跳转到关于
                startActivity(new Intent(MainActivity.this,AboutActivity.class));
                break;
        }
    }


    /**
     * 本地图片例子
     */
    private void setPges() {
        convenientBanner.setPages(
                new CBViewHolderCreator<LocalImageHolderView>() {
                    @Override
                    public LocalImageHolderView createHolder() {
                        return new LocalImageHolderView();
                    }
                }, localImages)
                //设置两个点图片作为翻页指示器，不设置则没有指示器，可以根据自己需求自行配合自己的指示器,不需要圆点指示器可用不设
                .setPageIndicator(new int[]{R.drawable.ic_page_indicator, R.drawable.ic_page_indicator_focused})
                //设置翻页的效果，不需要翻页效果可用不设
                .setPageTransformer(Transformer.DefaultTransformer);

//        convenientBanner.setManualPageable(false);设置不能手动影响

        //网络加载例子
//        networkImages= Arrays.asList(images);
//        convenientBanner.setPages(new CBViewHolderCreator<NetworkImageHolderView>() {
//            @Override
//            public NetworkImageHolderView createHolder() {
//                return new NetworkImageHolderView();
//            }
//        },networkImages);
    }


    /*
    加入测试Views
    * */
    private void loadTestDatas() {
        //本地图片集合
        for (int position = 0; position < 5; position++)
            localImages.add(getResId("ic_test_" + 0, R.drawable.class));

        //各种翻页效果
        transformerList.add(Transformer.DefaultTransformer.getClassName());
        transformerList.add(Transformer.AccordionTransformer.getClassName());
        transformerList.add(Transformer.BackgroundToForegroundTransformer.getClassName());
        transformerList.add(Transformer.CubeInTransformer.getClassName());
        transformerList.add(Transformer.CubeOutTransformer.getClassName());
        transformerList.add(Transformer.DepthPageTransformer.getClassName());
        transformerList.add(Transformer.FlipHorizontalTransformer.getClassName());
        transformerList.add(Transformer.FlipVerticalTransformer.getClassName());
        transformerList.add(Transformer.ForegroundToBackgroundTransformer.getClassName());
        transformerList.add(Transformer.RotateDownTransformer.getClassName());
        transformerList.add(Transformer.RotateUpTransformer.getClassName());
        transformerList.add(Transformer.StackTransformer.getClassName());
        transformerList.add(Transformer.ZoomInTransformer.getClassName());
        transformerList.add(Transformer.ZoomOutTranformer.getClassName());

        // transformerArrayAdapter.notifyDataSetChanged();
    }

    /**
     * 通过文件名获取资源id 例子：getResId("icon", R.drawable.class);
     *
     * @param variableName
     * @param c
     * @return
     */
    public static int getResId(String variableName, Class<?> c) {
        try {
            Field idField = c.getDeclaredField(variableName);
            return idField.getInt(idField);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    // 开始自动翻页
    @Override
    protected void onResume() {
        super.onResume();
        initData();
        //开始自动翻页
        convenientBanner.startTurning(2000);
    }

    // 停止自动翻页
    @Override
    protected void onPause() {
        super.onPause();
        //停止翻页
        convenientBanner.stopTurning();
    }

    //点击切换效果
  /*  @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        String name = transformerList.get(position);
        Transformer transformer = Transformer.valueOf(name);
        convenientBanner.setPageTransformer(transformer);
    }*/


    /**
     * 获取app信息
     */
    private void getAppMessage() {
        okHttpHelper = OkHttpHelper.getInstance(MainActivity.this);
        okHttpHelper.get(PathUtils.APP_MSG_PATH, new SimpleCallback<String>(MainActivity.this) {

            @Override
            public void onSuccess(Response response, String s) {
                Log.d("MainActivity","获取app信息:"+s);
                if (s != null && !s.equals("")) {
                    org.json.JSONObject object = null;
                    try {
                        object = new org.json.JSONObject(s);
                        int versionCode = object.getInt("versionCode");
                        appName = object.getString("appName");
                        int code = SystemVerdonCode.getAppVersionCode(MainActivity.this);
                        if (versionCode > code) {
                            showNoticeDialog();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

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
        builder.setWidth((int)(MainActivity.this.getWindowManager().getDefaultDisplay().getWidth()*0.8));
        builder.setPositiveButton("更新", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                //判断权限
                if (Build.VERSION.SDK_INT >= 23) {
                    //判断权限
                    if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        //用户没有授予，做权限申请
                        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
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
        String urlPath = PathUtils.NEWAPP + appName + "/file";
        Intent intent = new Intent(MainActivity.this, UpdateService.class);
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
                    Toast.makeText(MainActivity.this,"拒绝安装",Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
