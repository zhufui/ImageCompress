package com.example.zfutils;

import android.app.Application;
import android.content.Context;
import android.support.v4.content.ContextCompat;

/**
 * 获取资源文件中的属性
 * 使用时先初始化Context
 */
public class ResUtil {
    public static Context context;

    /**
     * 初始化context
     *
     * @param app
     */
    public static void initContext(Application app) {
        context = app.getApplicationContext();
    }

    public static String getString(int resId) {
        if (context == null) {
            return null;
        }
        return context.getResources().getString(resId);
    }

    public static int getColor(int resId) {
        if (context == null) {
            return -1;
        }
        return ContextCompat.getColor(context, resId);
    }

    public static float getDimens(int resId) {
        if (context == null) {
            return -1;
        }
        return context.getResources().getDimension(resId);
    }
}
