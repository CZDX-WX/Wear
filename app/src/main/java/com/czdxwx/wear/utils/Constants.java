package com.czdxwx.wear.utils;



public class Constants {
    public static final String BASE_URL = "http://47.109.198.70:8080/";
    //获取设备列表
    public static final String DEVICE_GET_Devices = BASE_URL + "devices/getDevices";
    //获取状态列表
    public static final String STATE_GET_STATES = BASE_URL + "states/getLists";
    //验证密码
    public static final String VLIDATE_USER = BASE_URL + "users/validate";
    //获取静态地图
    public static final String GET_STATIC_MAP="https://restapi.amap.com/v3/staticmap?key=d0f52bb91c6a794e2089d08bdf1bc41c&zoom=16&size=400*400&markers=mid,,A:";
    //获取oss里的图片
    public static final String GET_PIC_OSS="http://47.109.198.70:8080/oss/getPic?imageName=image/";
}
