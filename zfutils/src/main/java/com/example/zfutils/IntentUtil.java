package com.example.zfutils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;

/**
 * author  : zhufu
 * email   : zhufui@sina.com
 * time    : 2018/03/16
 * desc    : 意图工具类
 * version : 1.0
 */

public final class IntentUtil {

    /**
     * 拨打电话
     * 需要权限 {@code <uses-permission android:name="android.permission.CALL_PHONE"/>}
     *
     * @param context
     * @param phoneNumber
     */
    @SuppressLint("MissingPermission")
    public static void call(Context context, String phoneNumber) {
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNumber));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * 跳转到拨号盘
     *
     * @param context
     * @param phoneNumber
     */
    public static void dial(Context context, String phoneNumber) {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNumber));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * 发送短信
     *
     * @param context
     * @param phoneNumber 收件人手机号
     * @param content     发送短信内容
     */
    public static void sendSms(Context context, String phoneNumber, String content) {
        Uri uri = Uri.parse("smsto:" + phoneNumber);
        Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
        intent.putExtra("sms_body", content);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * 卸载app
     *
     * @param context
     * @param packageName 包名
     */
    public static void unInstallApp(Context context, String packageName) {
        Intent intent = new Intent(Intent.ACTION_DELETE);
        intent.setData(Uri.parse("package:" + packageName));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * 打开app
     *
     * @param context
     * @param packageName 包名
     */
    public static void launchApp(Context context, String packageName) {
        context.getPackageManager().getLaunchIntentForPackage(packageName);
    }

    /**
     * 打开app具体设置界面
     *
     * @param context
     * @param packageName
     */
    public static void appDetailsSettings(Context context, String packageName) {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + packageName));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * 跳转到app指定的界面
     *
     * @param context
     * @param packageName 包名
     * @param className   类名
     * @param bundle
     */
    public static void appComponent(Context context, String packageName, String className, Bundle bundle) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        if (bundle != null) intent.putExtras(bundle);
        ComponentName cn = new ComponentName(packageName, className);
        intent.setComponent(cn);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * 跳转到app指定的界面
     *
     * @param context
     * @param packageName 包名
     * @param className   类名
     */
    public static void appComponent(Context context, String packageName, String className) {
        appComponent(context, packageName, className);
    }

    /**
     * 关机
     */
    public static void shutdown() {
        Intent intent = new Intent(Intent.ACTION_SHUTDOWN);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    }

    /**
     * 打开浏览器
     *
     * @param context
     * @param urlString url地址，例如：www.baidu.com
     */
    public static void openBrowsable(Context context, String urlString) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(urlString));
        context.startActivity(intent);
    }

    /**
     * 获取App具体设置的意图
     * 跳转到应用信息界面
     *
     * @param packageName 包名
     * @return intent
     */
    public static void getAppDetailsSettingsIntent(Context context, String packageName) {
        Intent intent = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS");
        intent.setData(Uri.parse("package:" + packageName));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * 打开系统相册
     *
     * @param activity
     * @param requestCode
     * @param imageType   图片类型，可以写image/jpeg 、 image/png，所有类型写image/*，默认是所有类型
     */
    public static void choosePhoto(Activity activity, int requestCode, String imageType) {
        Intent intentToPickPic = new Intent(Intent.ACTION_PICK, null);
        intentToPickPic.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, imageType == null ? "image/*" : imageType);
        activity.startActivityForResult(intentToPickPic, requestCode);
    }

    /**
     * 打开系统相册
     *
     * @param activity
     * @param requestCode
     */
    public static void choosePhoto(Activity activity, int requestCode) {
        choosePhoto(activity, requestCode, null);
    }

    /**
     * 删除系统中图片或者文件后，需要发送一个广播刷新媒体库
     * 扫描文件
     */
    public static void scanFile(Context context, String[] paths) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            MediaScannerConnection.scanFile(context, paths,
                    null, new MediaScannerConnection.OnScanCompletedListener() {
                        public void onScanCompleted(String path, Uri uri) {
                        }
                    });
        } else {
            for (String path : paths) {
                Uri data = Uri.parse("file://" + path);
                context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, data));
            }
        }
    }

}