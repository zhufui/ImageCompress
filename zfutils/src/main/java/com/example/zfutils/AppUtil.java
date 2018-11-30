package com.example.zfutils;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageInstaller;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.util.Log;

import java.util.List;

/**
 * author  : zhufu
 * email   : zhufui@sina.com
 * time    : 2018/03/09
 * desc    : app工具
 * version : 1.0
 */

public final class AppUtil {
    private AppUtil() {
    }

    /**
     * 判断App是否安装
     *
     * @param context     上下文
     * @param packageName 包名
     * @return true:已安装,false:未安装
     */
    public static boolean isInstallApp(Context context, String packageName) {
        return context.getApplicationContext().getPackageManager().getLaunchIntentForPackage(packageName) != null;
    }

    /**
     * 打开app
     *
     * @param context     上下文
     * @param packageName 包名
     */
    public static void launchApp(Context context, String packageName) {
        context.startActivity(context.getApplicationContext().getPackageManager().getLaunchIntentForPackage(packageName));
    }

    /**
     * 判断App是否有root权限
     *
     * @return {@code true}: 是<br>{@code false}: 否
     */
    public static boolean isAppRoot() {
        ShellUtil.CommandResult result = ShellUtil.execCmd("echo root", true);
        if (result.result == 0) {
            return true;
        }
        if (result.errorMsg != null) {
            Log.d("isAppRoot", result.errorMsg);
        }
        return false;
    }

    /**
     * 判断是否是系统应用
     *
     * @param context
     * @param packageName 包名
     * @return
     */
    public static boolean isSystemApp(Context context, String packageName) {
        try {
            PackageManager pm = context.getApplicationContext().getPackageManager();
            ApplicationInfo ai = pm.getApplicationInfo(packageName, 0);
            return ai != null && (ai.flags & ApplicationInfo.FLAG_SYSTEM) != 0;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 判断自身是否是系统应用
     *
     * @param context
     * @return
     */
    public static boolean isSystemApp(Context context) {
        return isSystemApp(context, context.getApplicationContext().getPackageName());
    }

    /**
     * 判断App是否是debug版本
     *
     * @param context
     * @param packageName
     * @return
     */
    public static boolean isAppDebug(Context context, String packageName) {
        try {
            PackageManager pm = context.getApplicationContext().getPackageManager();
            ApplicationInfo ai = pm.getApplicationInfo(packageName, 0);
            return ai != null && (ai.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 获取App签名
     *
     * @param context
     * @param packageName
     * @return
     */
    @SuppressLint("PackageManagerGetSignatures")
    public static Signature[] getAppSignature(Context context, String packageName) {
        try {
            PackageManager pm = context.getApplicationContext().getPackageManager();
            PackageInfo pi = pm.getPackageInfo(packageName, PackageManager.GET_SIGNATURES);
            return pi == null ? null : pi.signatures;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 静默安装App
     * 非root需添加权限 <uses-permission android:name="android.permission.INSTALL_PACKAGES" />
     * 1.需要root才能静默安装
     * 2.需要系统签名才能静默安装
     * 3.需要在manifest中增加android:sharedUserId="android.uid.system"
     *
     * @param filePath 文件路径
     * @return true:安装成功,false:安装失败
     */
    public static boolean installAppSilent(Context context, String filePath) {
        String command = StringUtil.append("pm install -i ", context.getPackageName(), " --user 0 ", filePath, " & exit");
        ShellUtil.CommandResult commandResult = ShellUtil.execLineCmd(command, true);
//        String command = "LD_LIBRARY_PATH=/vendor/lib:/system/lib pm install " + filePath;
//        commandResult = ShellUtil.execCmd(command,
//                !isSystemApp(context, context.getApplicationContext().getPackageName()), true);
        return commandResult.successMsg != null && commandResult.successMsg.toLowerCase().contains("success");
    }

    /**
     * 静默卸载App
     * 非root需添加权限<uses-permission android:name="android.permission.DELETE_PACKAGES" />
     * 1.需要root才能静默安装
     * 2.需要系统签名才能静默安装
     * 3.需要在manifest中增加android:sharedUserId="android.uid.system"
     *
     * @param packageName 包名
     * @param isKeepData  是否保留数据,true:保留,false:不保留
     * @return true:卸载成功,false:卸载成功
     */
    @SuppressLint("NewApi")
    public static boolean uninstallAppSilent(Context context, String packageName, boolean isKeepData) {
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent sender = PendingIntent.getActivity(context, 0, intent, 0);
        PackageInstaller mPackageInstaller = context.getPackageManager().getPackageInstaller();
        mPackageInstaller.uninstall(packageName, sender.getIntentSender());

//        StringBuilder sb = new StringBuilder();
//        sb.append("pm uninstall ");
//        if(isKeepData){
//            sb.append(" -k ");
//        }
//        sb.append("--user 0 ");
//        sb.append(packageName);
//        Log.d("execLineCmd","command = " + sb.toString());
//        ShellUtil.CommandResult commandResult = ShellUtil.execLineCmd(sb.toString(), true);

//        String command = "LD_LIBRARY_PATH=/vendor/lib:/system/lib pm uninstall " + (isKeepData ? "-k " : "") + packageName;
//        ShellUtil.CommandResult commandResult = ShellUtil.execCmd(command, !isSystemApp(context), true);
//        return commandResult.successMsg != null && commandResult.successMsg.toLowerCase().contains("success");
        return true;
    }

    /**
     * 判断App是否处于前台
     *
     * @param context 上下文
     * @return true:是,false:否
     */
    public static boolean isAppForeground(Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> infos = manager.getRunningAppProcesses();
        if (infos == null || infos.size() == 0) return false;
        for (ActivityManager.RunningAppProcessInfo info : infos) {
            if (info.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                return info.processName.equals(context.getPackageName());
            }
        }
        return false;
    }

}