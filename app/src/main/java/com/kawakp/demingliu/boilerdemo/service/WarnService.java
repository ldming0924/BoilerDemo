package com.kawakp.demingliu.boilerdemo.service;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;


import com.kawakp.demingliu.boilerdemo.R;
import com.kawakp.demingliu.boilerdemo.canstant.Canstant;
import com.kawakp.demingliu.boilerdemo.http.OkHttpHelper;
import com.kawakp.demingliu.boilerdemo.http.SimpleCallback;
import com.kawakp.demingliu.boilerdemo.receiver.NotificationReceiver;
import com.kawakp.demingliu.boilerdemo.utils.PathUtils;
import com.kawakp.demingliu.boilerdemo.utils.SharedPerferenceHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Response;

/**
 * Created by Administrator on 2015/9/8.
 */
public class WarnService extends Service  {

    private boolean flag = true;
    private OkHttpHelper okHttpHelper;
    int total;

    private String json;
    private String time;


    public Handler myHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 10001) {
                //解析数据
                String json = (String) msg.obj;
                //Log.d("WarnService", json + "--------------");
                if (json != null) {
                    try {
                        JSONObject object = new JSONObject(json);
                        int t = object.getInt("total");
                        //发送广播
                        if (total != t) {
                            Intent intent = new Intent(Canstant.INTENTFILTER);
                            intent.putExtra("TOTAL", t);
                            sendBroadcast(intent);
                             //Log.d("WarnService", total + "--------------");
                            //请求报警内容
                            okHttpHelper.get(PathUtils.NEW_WARM, new SimpleCallback<String>(getApplicationContext()) {

                                @Override
                                public void onSuccess(Response response, String s) {
                                    Log.d("TAG",s+"----------------");
                                    JSONObject object = null;
                                    try {
                                        object = new JSONObject(s);
                                        JSONArray array = object.getJSONArray("list");
                                        JSONObject obj = array.getJSONObject(0);
                                        if (!obj.getString("createDate").equals(time)) {
                                            showNotification(getApplicationContext(), obj.getString("name"), obj.getString("createDate"));
                                            time = obj.getString("createDate");

                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }

                                @Override
                                public void onError(Response response, int code, Exception e) {

                                }
                            });
                        }else {
                            //Log.d("WarnService","==============");
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }else {
                Log.d("WarnService","没有获取到数据");
            }
        }
    };

    private void showNotification(Context context, String message, String time) {
        Intent broadcastIntent = new Intent(context, NotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.
                getBroadcast(context, Canstant.REQUEST_CODE, broadcastIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setContentTitle("设备报警")
                .setContentText(time + "   " + message)
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setAutoCancel(true);

        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = builder.build();
        notification.defaults = Notification.DEFAULT_SOUND;//系统默认提示音
        notification.defaults |= Notification.DEFAULT_VIBRATE;//震动
        long[] vibrate = {0, 100, 200, 300};
        notification.vibrate = vibrate;

        manager.notify(Canstant.NOTIFY_ID, notification);

    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
    }






    public class MyThread implements Runnable {

        @Override
        public void run() {
            while (flag) {
                total = SharedPerferenceHelper.getUpdateNum(getApplicationContext());
                try {
                    Thread.sleep(2000);
                    okHttpHelper.get(PathUtils.REAL_WARN, new SimpleCallback<String>(getApplicationContext()) {

                        @Override
                        public void onSuccess(Response response, String s) {
                            Message message = Message.obtain();
                            message.what = 10001;
                            message.obj = s;
                            myHandler.sendMessage(message);
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

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //total = SharedPerferenceHelper.getUpdateNum(getApplicationContext());
        //获取cookie
        okHttpHelper = OkHttpHelper.getInstance(getApplicationContext());
        new Thread(new MyThread()).start();

        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        myHandler.removeCallbacks(new MyThread());
        flag = false;
        super.onDestroy();
    }


}
