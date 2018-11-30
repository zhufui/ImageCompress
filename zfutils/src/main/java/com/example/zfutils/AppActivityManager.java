package com.example.zfutils;

import android.app.Activity;

import java.util.Iterator;
import java.util.Stack;

/**
 * author  : zhufu
 * email   : zhufui@sina.com
 * time    : 2018/03/08
 * desc    : 应用Activitiy管理类
 *           使用时建议在onCrtea中使用add(),onDestory中使用remove()
 * version : 1.0
 */

public final class AppActivityManager {
    private AppActivityManager() {
    }

    public Stack<Activity> mActivityStack;
    private static AppActivityManager instance;

    public static AppActivityManager getInstance() {
        if (instance == null) {
            synchronized (AppActivityManager.class) {
                if (instance == null) {
                    instance = new AppActivityManager();
                }
            }
        }
        return instance;
    }

    /**
     * 往stack中添加activity
     *
     * @param activity
     */
    public void add(Activity activity) {
        if (mActivityStack == null) {
            mActivityStack = new Stack<>();
        }
        mActivityStack.add(activity);
    }

    /**
     * 从stack中移除activity
     *
     * @param activity
     */
    public void remove(Activity activity) {
        if (mActivityStack == null) {
            return;
        }
        mActivityStack.remove(activity);
    }

    /**
     * 从stack中移除全部activity
     */
    public void removeAll() {
        if (mActivityStack == null) {
            return;
        }
        mActivityStack.clear();
    }

    /**
     * 关闭指定的activity
     *
     * @param cls
     */
    public void finish(Class<?> cls) {
        finish(cls, false);
    }

    /**
     * 关闭全部activity
     */
    public void finishAll() {
        finish(null, true);
    }

    /**
     * 关闭activity
     *
     * @param cls         指定的activity类名
     * @param isFinishAll 是否全部关闭
     */
    private void finish(Class<?> cls, boolean isFinishAll) {
        if (mActivityStack == null) {
            return;
        }
        Iterator<Activity> it = mActivityStack.iterator();
        while (it.hasNext()) {
            Activity activity = it.next();
            if (isFinishAll) {
                activity.finish();
                it.remove();
            } else {
                if (activity.getClass().equals(cls)) {
                    activity.finish();
                    it.remove();
                }
            }
        }
    }

}
