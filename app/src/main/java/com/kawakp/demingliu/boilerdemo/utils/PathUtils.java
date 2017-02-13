package com.kawakp.demingliu.boilerdemo.utils;

/**
 * Created by deming.liu on 2017/1/5.
 */

public class PathUtils {
    //获取app信息
    public static final String APP_MSG_PATH = "http://kawakp.chinclouds.com/userconsle/clientApps/boiler";
    //登录
    public static final String LOGIN_PATH = "http://kawakp.chinclouds.com/userconsle/login";
    //设备列表
    public static final String DEVICELIST_PATH = "http://kawakp.chinclouds.com/userconsle/devices?";
    //设备组织结构
    public static final String DEVICE_ORG = "http://kawakp.chinclouds.com/userconsle/orgs?orgId=";
    //实时数据
    public static final String REALTIME_DATA = "http://kawakp.chinclouds.com/userconsle/devices/";
    //获取元件列表
    public static final String ELEMENT_LIST ="http://kawakp.chinclouds.com/userconsle/";
    //获取历史报警
    public static final String HISTORY = "http://kawakp.chinclouds.com/userconsle/deviceAlarms?";
    //控制设备
    public static final String DEVICE_SET = "http://kawakp.chinclouds.com/userconsle/devices/";
    //下载新版本
    public static final String NEWAPP = "http://kawakp.chinclouds.com/userconsle/clientApps/";
    //请求最新的一条报警信息
    public static final String NEW_WARM = "http://kawakp.chinclouds.com/userconsle/deviceAlarms?pageSize=1";
    //获取实时报警
    public static final String REAL_WARN = "http://kawakp.chinclouds.com/userconsle/deviceAlarms?pageSize=0";
    //数据统计
    public static final String DATA_STATISTICS ="http://kawakp.chinclouds.com/userconsle/devices/";

}
