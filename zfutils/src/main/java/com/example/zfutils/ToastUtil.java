package com.example.zfutils;

import android.content.Context;
import android.widget.Toast;

/**
 * author: zhufu
 * email : zhufui@sina.com
 * time  : 2018/03/10
 * desc  : 吐司工具类
 * version : 1.0
 */

public final class ToastUtil {
    private ToastUtil() {
    }

    private static Toast sToast;

    /**
     * 显示短时吐司
     *
     * @param text 文本
     */
    public static void showShortToast(Context context, CharSequence text) {
        showToast(context, text, Toast.LENGTH_SHORT);
    }

    /**
     * 显示长时吐司
     *
     * @param resId 文本资源id
     */
    public static void showLongToast(Context context, int resId) {
        showToast(context, resId, Toast.LENGTH_SHORT);
    }

    /**
     * 显示长时吐司
     *
     * @param text 文本
     */
    public static void showLongToast(Context context, CharSequence text) {
        showToast(context, text, Toast.LENGTH_SHORT);
    }

    /**
     * 显示短时吐司
     *
     * @param resId 文本资源id
     */
    public static void showShortToast(Context context, int resId) {
        showToast(context, resId, Toast.LENGTH_SHORT);
    }

    /**
     * 显示吐司
     *
     * @param context
     * @param resId    文本资源id
     * @param duration 时长
     */
    private static void showToast(Context context, int resId, int duration) {
        showToast(context, context.getApplicationContext().getResources().getString(resId), duration);
    }

    /**
     * 显示吐司
     *
     * @param context
     * @param text     文本
     * @param duration 时长
     */
    private static void showToast(Context context, CharSequence text, int duration) {
        if (sToast == null) {
            sToast = Toast.makeText(context.getApplicationContext(), text, duration);
        } else {
            sToast.setText(text);
            sToast.setDuration(duration);
        }
        sToast.show();
    }

    /**
     * 取消吐司显示
     */
    public static void cancelToast() {
        if (sToast != null) {
            sToast.cancel();
            sToast = null;
        }
    }
}
