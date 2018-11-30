package com.example.zfutils;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Map;

/**
 * author: zhufu
 * email : zhufui@sina.com
 * time  : 2018/03/28
 * desc  : 操作SharedPreferences,在Application中初始化
 * version : 1.0
 */

public final class SPUtil {
    private static SharedPreferences sharedPreferences;
    private static SharedPreferences.Editor editor;

    public SPUtil(Context context, String spName) {
        sharedPreferences = context.getSharedPreferences(spName, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    /**
     * 保存数据
     *
     * @param key
     * @param object
     */
    public static void put(String key, Object object) {
        if (object instanceof String) {
            editor.putString(key, (String) object);
        } else if (object instanceof Integer) {
            editor.putInt(key, (Integer) object);
        } else if (object instanceof Boolean) {
            editor.putBoolean(key, (Boolean) object);
        } else if (object instanceof Float) {
            editor.putFloat(key, (Float) object);
        } else if (object instanceof Long) {
            editor.putLong(key, (Long) object);
        } else {
            editor.putString(key, object.toString());
        }
        editor.apply();
    }

    /**
     * 获取保存的数据
     *
     * @param key
     * @param defaultObject
     * @return
     */
    public static Object get(String key, Object defaultObject) {
        if (defaultObject instanceof String) {
            return sharedPreferences.getString(key, (String) defaultObject);
        } else if (defaultObject instanceof Integer) {
            return sharedPreferences.getInt(key, (Integer) defaultObject);
        } else if (defaultObject instanceof Boolean) {
            return sharedPreferences.getBoolean(key, (Boolean) defaultObject);
        } else if (defaultObject instanceof Float) {
            return sharedPreferences.getFloat(key, (Float) defaultObject);
        } else if (defaultObject instanceof Long) {
            return sharedPreferences.getLong(key, (Long) defaultObject);
        } else {
            return sharedPreferences.getString(key, null);
        }
    }

    /**
     * 移除某个key值已经对应的值
     *
     * @param key
     */
    public static void remove(String key) {
        editor.remove(key).apply();
    }

    /**
     * 清除所有的数据
     */
    public static void clear() {
        editor.clear().apply();
    }

    /**
     * 查询某个key是否存在
     *
     * @param key
     * @return
     */
    public static boolean contains(String key) {
        return sharedPreferences.contains(key);
    }

    /**
     * 返回所有的键值对
     *
     * @return
     */
    public static Map<String, ?> getAll() {
        return sharedPreferences.getAll();
    }
}
