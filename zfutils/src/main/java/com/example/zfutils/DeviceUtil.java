package com.example.zfutils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;

import java.io.File;

/**
 * author: zhufu
 * email : zhufui@sina.com
 * time  : 2018/03/18
 * desc  : 获取设备信息,设备的相关操作
 * version : 1.0
 */

public final class DeviceUtil {
    public DeviceUtil() {
    }

    /**
     * 获取真实的屏幕分辨率
     *
     * @return int[]{宽度,高度}
     */
    @SuppressLint("NewApi")
    public static int[] getRealMetrics(Activity activity) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getRealMetrics(displayMetrics);
        return new int[]{displayMetrics.widthPixels, displayMetrics.heightPixels};
    }

    /**
     * 判断设备是否root
     *
     * @return true:是,false:否
     */
    public static boolean isRooted() {
        String su = "su";
        String[] locations = {"/system/bin/", "/system/xbin/", "/sbin/", "/system/sd/xbin/", "/system/bin/failsafe/",
                "/data/local/xbin/", "/data/local/bin/", "/data/local/"};
        for (String location : locations) {
            if (new File(location + su).exists()) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取系统SDK版本号
     *
     * @return 系统版本号
     */
    public static int getSDKVersion() {
        return Build.VERSION.SDK_INT;
    }

    /**
     * 获取设备AndroidID
     *
     * @return AndroidID
     */
    @SuppressLint("HardwareIds")
    public static String getAndroidID(Context context) {
        return Settings.Secure.getString(context.getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    /**
     * 获取设备厂商
     * <p>如Xiaomi</p>
     *
     * @return 设备厂商
     */

    public static String getManufacturer() {
        return Build.MANUFACTURER;
    }

    /**
     * 获取IMEI
     * 需要权限<uses-permission android:name="android.permission.READ_PHONE_STATE" />
     *
     * @return
     */
    @SuppressLint({"MissingPermission"})
    public static String getIMEI(Context context) {
        try {
            TelephonyManager tm = (TelephonyManager) context.getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
            return tm.getDeviceId();
        } catch (Exception e) {
            Log.e("DeviceUtil", "getIMEI error", e);
        }
        return null;
    }

    /**
     * 获取设备信息
     *
     * @return
     */
    public static Device getDevice() {
        Device device = new Device();
        device.phoneBrand = Build.BRAND;
        device.phoneModel = Build.MODEL;
        device.buildLevel = Build.VERSION.SDK_INT;
        device.buildVersion = Build.VERSION.RELEASE;
        return device;
    }

    public static class Device {
        public String phoneBrand;   //手机品牌
        public String phoneModel;   //手机型号
        public int buildLevel;      //手机Android API等级(22,23)
        public String buildVersion; //手机Android 版本(5.0,6.0,7.0...)
    }
}
