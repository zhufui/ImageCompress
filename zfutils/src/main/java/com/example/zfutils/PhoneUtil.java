package com.example.zfutils;

import android.content.Context;
import android.content.res.Configuration;
import android.text.TextUtils;

/**
 * author: zhufu
 * email : zhufui@sina.com
 * time  : 2018/03/09
 * desc  : 手机设备相关操作
 * version : 1.0
 */

public final class PhoneUtil {
    private PhoneUtil() {
    }

    /**
     * 隐藏手机号码中间四位
     *
     * @param phoneNumber 12345678901
     * @return 123****8901
     */
    public static String hiddenMiddleNumber(String phoneNumber) {
        StringBuilder sb = new StringBuilder();
        if (TextUtils.isEmpty(phoneNumber)) {
            return sb.toString();
        }

        sb.append(phoneNumber.substring(0, 3));
        sb.append("****");
        sb.append(phoneNumber.substring(7));
        return sb.toString();
    }

    /**
     * 判断是否是平板设备
     *
     * @param context
     * @return true:是平板,false:不是平板,是手机
     */
    private boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >=
                Configuration.SCREENLAYOUT_SIZE_LARGE;
    }
}
