package com.example.zfutils;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

public class MediaStoreUtil {

    /**
     * 获取真实的文件路径
     * 去媒体库中查询真实的文件路径
     *
     * @param context
     * @param uri     文件的uri，例如：Uri uri = Uri.parse("content://media/external/images/media/221");
     * @return
     */
    public static String getRealFilePath(Context context, Uri uri) {
        String realFilePath = null;
        Cursor c = null;
        try {
            String[] filePathColumns = {MediaStore.Images.Media.DATA};
            c = context.getContentResolver().query(uri, filePathColumns, null, null, null);
            if (c != null && c.moveToFirst()) {
                int columnIndex = c.getColumnIndex(filePathColumns[0]);
                realFilePath = c.getString(columnIndex);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            CloseUtil.closeCursor(c);
        }
        return realFilePath;
    }
}
