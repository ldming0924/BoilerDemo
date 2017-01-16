package com.kawakp.demingliu.boilerdemo.utils;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.util.Locale;

/**
 * 网络相关的工具类
 *
 * @author Jax
 * @version V1.0.0
 * @data on 2015/8/15.
 */
public class NetUtils {

    private static final String TAG = "NetUtils";

    private NetUtils() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    public enum NetType {
        WIFI, CMNET, CMWAP, noneNet, GWAP_3, GNET_3, UNIWAP, UNINET, CTWAP, CTNET
    }

    /**
     * 网络是否可用
     *
     * @param context
     * @return
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager mgr = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] info = mgr.getAllNetworkInfo();
        if (info != null) {
            for (NetworkInfo anInfo : info) {
                if (anInfo.getState() == NetworkInfo.State.CONNECTED) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 判断网络是否连接
     *
     * @param context
     * @return
     */
    public static boolean isConnected(Context context) {

        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivity != null) {
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (info != null && info.isConnected()) {
                if (info.getState() == NetworkInfo.State.CONNECTED) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 判断是否是wifi连接
     */
    public static boolean isWifi(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm != null && cm.getActiveNetworkInfo().getType() == ConnectivityManager.TYPE_WIFI;

    }

    /**
     * 判断MOBILE网络是否可用
     *
     * @param context
     * @return
     */
    public static boolean isMobileConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mMobileNetworkInfo = mConnectivityManager
                    .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if (mMobileNetworkInfo != null) {
                return mMobileNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    /**
     * 打开网络设置界面
     */
    public static void openSetting(Activity activity) {
        Intent intent = new Intent("/");
        ComponentName cm = new ComponentName("com.android.settings", "com.android.settings.WirelessSettings");
        intent.setComponent(cm);
        intent.setAction("android.intent.action.VIEW");
        activity.startActivityForResult(intent, 0);
    }

    /**
     * 获取当前网络连接的类型信息
     *
     * @param context
     * @return
     */
    public static int getConnectedType(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null && mNetworkInfo.isAvailable()) {
                return mNetworkInfo.getType();
            }
        }
        return -1;
    }

    /**
     * @return netType 返回类型
     * @throws
     * @方法名: getAPNType
     * @说 明: 获取当前的网络状态 -1：没有网络 1：WIFI网络2：wap 网络3：net网络
     * @参 数: @param context
     * @参 数: @return
     */
    public static NetType getAPNType(Context context) {
        ConnectivityManager connMgr = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo == null) {
            return NetType.noneNet;
        }

        if (!networkInfo.isAvailable()) {
            return NetType.noneNet;
        }
        int nType = networkInfo.getType();

        if (nType == ConnectivityManager.TYPE_MOBILE) {
            // 中国移动
            if (networkInfo.getExtraInfo().toLowerCase(Locale.getDefault()).equals(APNNet.CMNET)) {
                return NetType.CMNET;
            } else if (networkInfo.getExtraInfo().toUpperCase(Locale.getDefault()).equals(APNNet.CMWAP)) {
                return NetType.CMWAP;
                // 中国联通
            } else if (networkInfo.getExtraInfo().toLowerCase(Locale.getDefault()).equals(APNNet.GNET_3)) {
                return NetType.GNET_3;
            } else if (networkInfo.getExtraInfo().toLowerCase(Locale.getDefault()).equals(APNNet.GWAP_3)) {
                return NetType.GWAP_3;
            } else if (networkInfo.getExtraInfo().toLowerCase(Locale.getDefault()).equals(APNNet.UNIWAP)) {
                return NetType.UNIWAP;
            } else if (networkInfo.getExtraInfo().toLowerCase(Locale.getDefault()).equals(APNNet.UNINET)) {
                return NetType.UNINET;
                // 中国联通
            } else if (networkInfo.getExtraInfo().toLowerCase(Locale.getDefault()).equals(APNNet.CTNET)) {
                return NetType.CTNET;
            } else if (networkInfo.getExtraInfo().toLowerCase(Locale.getDefault()).equals(APNNet.CTWAP)) {
                return NetType.CTNET;
            }
        } else if (nType == ConnectivityManager.TYPE_WIFI) {
            return NetType.WIFI;
        }
        return NetType.noneNet;
    }

    public static class APNNet {
        /**
         * 　　* 中国移动cmwap
         */
        public static String CMWAP = "cmwap";
        /**
         * 中国移动cmnet
         */
        public static String CMNET = "cmnet";
        // 中国联通3GWAP设置 中国联通3G因特网设置 中国联通WAP设置 中国联通因特网设置
        // 3gwap 3gnet uniwap uninet
        /**
         * 3G wap 中国联通3gwap APN
         */
        public static String GWAP_3 = "3gwap";

        /**
         * 3G net 中国联通3gnet APN
         */
        public static String GNET_3 = "3gnet";
        /**
         * uni wap 中国联通uni wap APN
         */
        public static String UNIWAP = "uniwap";
        /**
         * uni net 中国联通uni net APN
         */
        public static String UNINET = "uninet";

        /**
         * 中国电信 net APN
         */
        public static String CTNET = "ctnet";
        /**
         * 中国电信 net APN
         */
        public static String CTWAP = "ctwap";
    }

}
