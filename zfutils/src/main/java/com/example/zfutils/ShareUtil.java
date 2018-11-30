package com.example.zfutils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;

import java.io.File;

/**
 * author: zhufu
 * email : zhufui@sina.com
 * time  : 2018/03/20
 * desc  : 分享工具类
 * version : 1.0
 */

public final class ShareUtil {
    private ShareUtil() {
    }

    /**
     * 通过蓝牙分享
     * 蓝牙分享需要添加权限如下：
     * <uses-feature android:name="android.hardware.bluetooth_le" android:required="false"/>
     * <uses-permission android:name="android.permission.BLUETOOTH_ADMIN" />
     * <uses-permission android:name="android.permission.BLUETOOTH" />
     * <uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
     * <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
     */
    public static void shareBle(Context context, String filePath) {
        share(context, filePath, "com.android.bluetooth");
    }

    /**
     * 通过蓝牙分享
     *
     * @param context
     * @param file
     */
    public static void shareBle(Context context, File file) {
        share(context, file, "com.android.bluetooth");
    }

    /**
     * 通过nfc分享
     * nfc分享需要添加权限如下：
     * <uses-permission android:name="android.permission.NFC"/>
     * <uses-feature android:name="android.hardware.nfc" android:required="false"/>
     */
    public static void shareNfc(Context context, String filePath) {
        share(context, filePath, "com.android.nfc");
    }

    /**
     * 通过nfc分享
     */
    public static void shareNfc(Context context, File file) {
        share(context, file, "com.android.nfc");
    }

    /**
     * 分享
     *
     * @param context
     * @param filePath    文件路径
     * @param packageName 包名
     */
    private static void share(Context context, String filePath, String packageName) {
        if (TextUtils.isEmpty(filePath)) {
            throw new NullPointerException("filePath is null");
        }
        share(context, new File(filePath), packageName);
    }

    /**
     * 分享
     *
     * @param context
     * @param file        文件
     * @param packageName 包名
     */
    private static void share(Context context, File file, String packageName) {
        if (file == null) {
            throw new NullPointerException("file is null");
        }
        Uri uri = Uri.fromFile(file);
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("*/*");
        intent.setPackage(packageName);
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        context.startActivity(Intent.createChooser(intent, "Share"));
    }
}
