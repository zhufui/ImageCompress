package com.example.zfutils;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.io.Closeable;
import java.io.IOException;

/**
 * author: zhufu
 * email : zhufui@sina.com
 * time  : 2018/03/27
 * desc  : 关闭工具类
 * version : 1.0
 */

public final class CloseUtil {
    private CloseUtil() {
    }

    /**
     * 关闭cursor
     *
     * @param cursors
     */
    public static void closeCursor(Cursor... cursors) {
        if (cursors == null) return;
        for (Cursor cursor : cursors) {
            if (cursor != null) {
                cursor.close();
                cursor = null;
            }
        }
    }

    /**
     * 关闭数据库
     *
     * @param dbs
     */
    public static void closeDb(SQLiteDatabase... dbs) {
        if (dbs == null) return;
        for (SQLiteDatabase db : dbs) {
            if (db != null) {
                db.close();
                db = null;
            }
        }
    }

    /**
     * 关闭IO
     *
     * @param closeables closeable
     */
    public static void closeIO(Closeable... closeables) {
        if (closeables == null) return;
        for (Closeable closeable : closeables) {
            if (closeable != null) {
                try {
                    closeable.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 安静关闭IO
     *
     * @param closeables closeable
     */
    public static void closeIOQuietly(Closeable... closeables) {
        if (closeables == null) return;
        for (Closeable closeable : closeables) {
            if (closeable != null) {
                try {
                    closeable.close();
                } catch (IOException ignored) {
                }
            }
        }
    }
}
