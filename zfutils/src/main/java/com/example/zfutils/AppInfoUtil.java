package com.example.zfutils;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

import java.util.ArrayList;
import java.util.List;

/**
 * author: zhufu
 * email : zhufui@sina.com
 * time  : 2018/03/19
 * desc  : 应用信息类
 * version : 1.0
 */

public final class AppInfoUtil {
    private AppInfoUtil() {
    }

    //默认 所有应用
    public static final int DEFAULT = 0;
    //系统应用
    public static final int SYSTEM_APP = DEFAULT + 1;
    //非系统应用
    public static final int UNSYSTEM_APP = DEFAULT + 2;

    /**
     * 获取当前应用的信息
     *
     * @param context
     * @return
     */
    public static AppInfo getAppInfo(Context context) {
        return getAppInfoByPackageName(context, context.getPackageName());
    }

    /**
     * 根据包名获取对应的应用信息
     *
     * @param context
     * @param packageName
     * @return
     */
    public static AppInfo getAppInfoByPackageName(Context context, String packageName) {
        PackageManager pm = context.getApplicationContext().getPackageManager();
        PackageInfo packageInfo = null;
        try {
            packageInfo = pm.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        if (packageInfo == null) {
            return null;
        }

        return getAppInfo(pm, packageInfo);
    }

    /**
     * 获取设备所有应用信息(包括系统内置应用和非系统应用)
     *
     * @param context
     * @return
     */
    public static List<AppInfo> getAllAppInfoList(Context context) {
        return getAppInfoList(context, DEFAULT);
    }

    /**
     * 获取非系统应用信息
     *
     * @param context
     * @return
     */
    public static List<AppInfo> getUnSystemAppInfoList(Context context) {
        return getAppInfoList(context, UNSYSTEM_APP);
    }

    /**
     * 获取系统内置应用信息
     *
     * @param context
     * @return
     */
    public static List<AppInfo> getSystemAppInfoList(Context context) {
        return getAppInfoList(context, SYSTEM_APP);
    }

    /**
     * 获取App详情列表
     *
     * @param context
     * @param type    类型,DEFAULT:默认所有的应用,SYSTEM_APP:系统应用,UNSYSTEM_APP:非系统应用
     * @return
     */
    private static List<AppInfo> getAppInfoList(Context context, int type) {
        ArrayList<AppInfo> appInfoList = new ArrayList<>();
        PackageManager pm = context.getApplicationContext().getPackageManager();
        List<PackageInfo> packages = pm.getInstalledPackages(0);

        for (int i = 0; i < packages.size(); i++) {
            PackageInfo packageInfo = packages.get(i);
            switch (type) {
                case UNSYSTEM_APP:
                    if (!isSystemAPP(packageInfo)) {
                        appInfoList.add(getAppInfo(pm, packageInfo));
                    }
                    break;
                case SYSTEM_APP:
                    if (isSystemAPP(packageInfo)) {
                        appInfoList.add(getAppInfo(pm, packageInfo));
                    }
                    break;
                default:
                    appInfoList.add(getAppInfo(pm, packageInfo));
                    break;
            }

        }
        return appInfoList;
    }

    /**
     * 获取App详情
     *
     * @param pm          包管理器
     * @param packageInfo 包信息
     * @return
     */
    private static AppInfo getAppInfo(PackageManager pm, PackageInfo packageInfo) {
        AppInfo appInfo = new AppInfo();
        appInfo.appName = packageInfo.applicationInfo.loadLabel(pm).toString();
        appInfo.packageName = packageInfo.packageName;
        appInfo.versionName = packageInfo.versionName;
        appInfo.versionCode = packageInfo.versionCode;
        appInfo.appIcon = packageInfo.applicationInfo.loadIcon(pm);
        appInfo.sourceDir = packageInfo.applicationInfo.sourceDir;
        return appInfo;
    }

    /**
     * 判断是否是系统应用
     *
     * @param packageInfo
     * @return true:是系统应用,false:不是系统应用
     */
    private static Boolean isSystemAPP(PackageInfo packageInfo) {
        return (packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0;
    }

    /**
     * 获取当前App进程的id
     *
     * @return
     */
    public static int getAppProcessId() {
        return android.os.Process.myPid();
    }

    /**
     * 获取当前进程的名称
     *
     * @param context
     * @return
     */
    public static String getCurProcessName(Context context) {
        String processName = null;
        int pid = android.os.Process.myPid();
        ActivityManager mActivityManager = (ActivityManager) context.getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo appProcess : mActivityManager.getRunningAppProcesses()) {
            if (appProcess.pid == pid) {
                processName = appProcess.processName;
            }
        }
        return processName;
    }

    /**
     * 应用相关信息
     */
    public static class AppInfo {
        //应用名称
        public String appName;
        //包名
        public String packageName;
        //应用icon
        public Drawable appIcon;
        //版本名
        public String versionName;
        //版本号
        public int versionCode = 0;
        //app路径
        public String sourceDir;
    }
}
