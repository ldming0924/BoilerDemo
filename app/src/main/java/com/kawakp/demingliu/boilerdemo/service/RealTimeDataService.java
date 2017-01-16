package com.kawakp.demingliu.boilerdemo.service;

import android.annotation.TargetApi;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;


import com.kawakp.demingliu.boilerdemo.http.OkHttpHelper;
import com.kawakp.demingliu.boilerdemo.http.SimpleCallback;
import com.kawakp.demingliu.boilerdemo.utils.PathUtils;
import com.kawakp.demingliu.boilerdemo.utils.SharedPerferenceHelper;

import okhttp3.Response;


/**
 * Created by deming.liu on 2016/8/29.
 */
public class RealTimeDataService extends Service {
    private boolean flag = true;

    private String url;
    private String deviceID;
    private OkHttpHelper okHttpHelper;

    public interface MyCallBack {
        void callBackMessage(String message);
    }

    public MyCallBack callBack;

    public void setCallBack(MyCallBack callBack) {
        this.callBack = callBack;
    }


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onCreate() {
        super.onCreate();
        deviceID = SharedPerferenceHelper.getDeviceId(getApplicationContext());
        url = PathUtils.REALTIME_DATA + deviceID + "/elementTables/945aa97257b74f0481f187fdf453f37d/datas?pageNum=1&pageSize=1";
        okHttpHelper = OkHttpHelper.getInstance(getApplicationContext());
        new Thread(new MyThread()).start();



    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new MsgBinder();
    }

    public class MsgBinder extends Binder {
        /**
         * 获取当前Service的实例
         *
         * @return
         */
        public RealTimeDataService getService() {
            return RealTimeDataService.this;
        }
    }

    public class MyThread implements Runnable {

        @Override
        public void run() {
            while (flag){
                try {
                    Thread.sleep(1000);
                    okHttpHelper.get(url, new SimpleCallback<String>(getApplicationContext()) {

                        @Override
                        public void onSuccess(Response response, String s) {
                            if (callBack != null) {
                                callBack.callBackMessage(s);
                            }
                        }

                        @Override
                        public void onError(Response response, int code, Exception e) {

                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }
    }



    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        // handler.removeCallbacks(new MyThread());
        flag = false;
        super.onDestroy();
    }
}
