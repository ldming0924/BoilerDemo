package com.kawakp.demingliu.boilerdemo.utils;

/**
 * Created by deming.liu on 2017/1/5.
 */

public class PathUtils {
    //获取app信息
    public final static String APP_MSG_PATH = "http://58.250.204.112:58010/userconsle/clientApps/boiler";

    public static final String LOGIN_PATH = "http://58.250.204.112:58010/userconsle/login";
    //设备列表
    public final static String DEVICELIST_PATH = "http://58.250.204.112:58010/userconsle/devices?";
    //设备组织结构
    public final static String DEVICE_ORG = "http://58.250.204.112:58010/userconsle/orgs";
    //实时数据
    public final static String REALTIME_DATA = "http://58.250.204.112:58010/userconsle/devices/";
    //获取元件列表
    public final static String ELEMENT_LIST ="http://58.250.204.112:58010/userconsle/";
    //获取历史报警
    public final static String HISTORY = "http://58.250.204.112:58010/userconsle/deviceAlarms?";
    //控制设备
    public static String DEVICE_SET = "http://58.250.204.112:58010/userconsle/devices/";
    //下载新版本
    public static String NEWAPP = "http://58.250.204.112:58010/userconsle/clientApps/";
    //请求最新的一条报警信息
    public static final String NEW_WARM = "http:58.250.204.112:58010/userconsle/deviceAlarms?pageSize=1";
    //获取实时报警
    public static final String REAL_WARN = "http://58.250.204.112:58010/userconsle/deviceAlarms?pageSize=0";


}
