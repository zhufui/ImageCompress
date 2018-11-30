package com.example.admin.imagecompress.manager;

import com.example.zfutils.FileUtil;
import com.example.zfutils.SDCardUtil;

import java.io.File;

public class FileManager {
    public static final String TAG = FileManager.class.getSimpleName();

    public static void initDir() {
        String dir = SDCardUtil.getSDCardPath() + "imageCompress/";
        FileUtil.createDirs(new File(dir));
    }

    /**
     * @return 目录
     */
    public static String getDir() {
        return SDCardUtil.getSDCardPath() + "imageCompress/";
    }

}
