package com.example.zfutils;

import android.util.Base64;

/**
 * author: zhufu
 * email : zhufui@sina.com
 * time  : 2018/03/29
 * desc  : Base64编码
 * version : 1.0
 */

public final class Base64Util {
    private Base64Util() {
    }

    /**
     * 编码
     *
     * @param input 需要编码的文本
     * @return
     */
    public static String encode2String(String input) {
        return Base64.encodeToString(input.getBytes(), Base64.DEFAULT);
    }

    /**
     * 编码
     *
     * @param input
     * @return
     */
    public static String encode2String(byte[] input) {
        return Base64.encodeToString(input, Base64.DEFAULT);
    }

    /**
     * 编码
     *
     * @param input
     * @return
     */
    public static byte[] encode(byte[] input) {
        return Base64.encode(input, Base64.DEFAULT);
    }

    /**
     * 编码
     *
     * @param input
     * @return
     */
    public static byte[] encode(String input) {
        return Base64.encode(input.getBytes(), Base64.DEFAULT);
    }

    /**
     * 解码
     *
     * @param input 编码过的文本
     * @return
     */
    public static String decode2String(String input) {
        return new String(decode(input));
    }

    /**
     * 解码
     *
     * @param input
     * @return
     */
    public static byte[] decode(String input) {
        return Base64.decode(input.getBytes(), Base64.DEFAULT);
    }
}
