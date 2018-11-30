package com.example.zfutils;

import android.os.Build;
import android.text.TextUtils;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;

/**
 * author: zhufu
 * email : zhufui@sina.com
 * time  : 2018/03/28
 * desc  :
 * version : 1.0
 */

public final class FileUtil {
    private FileUtil() {
    }

    /**
     * 计算文件大小
     *
     * @param path
     * @return
     */
    public static String fileSize(String path) {
        if (TextUtils.isEmpty(path)) {
            return null;
        }
        return fileSize(new File(path));
    }

    /**
     * 计算文件大小
     *
     * @param file
     * @return
     */
    public static String fileSize(File file) {
        if (file == null) {
            return null;
        }

        long fileSize = file.length();
        double dFileSize = fileSize;
        if (dFileSize < 1024) {
            return StringUtil.append(dFileSize, "B");
        }

        dFileSize = dFileSize / 1024d;
        if (dFileSize < 1024) {
            return StringUtil.append(Math.round(dFileSize), "KB");
        }

        dFileSize = dFileSize / 1024d;
        if (dFileSize < 1024) {
            DecimalFormat df = new DecimalFormat("#.##");
            return StringUtil.append(df.format(dFileSize), "MB");
        }

        dFileSize = dFileSize / 1024d;
        if (dFileSize < 1024) {
            DecimalFormat df = new DecimalFormat("#.##");
            return StringUtil.append(df.format(dFileSize), "GB");
        }
        return String.valueOf(fileSize);
    }

    /**
     * byte转文件
     * byte转file
     *
     * @param b          字节数组
     * @param outputFile 输出的文件路径
     * @return
     */
    public static File bytes2File(byte[] b, String outputFile) {
        File ret = null;
        BufferedOutputStream stream = null;
        try {
            ret = new File(outputFile);
            FileOutputStream fstream = new FileOutputStream(ret);
            stream = new BufferedOutputStream(fstream);
            stream.write(b);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return ret;
    }

    /**
     * 文件转byte
     * file转byte
     *
     * @param file
     * @return
     */
    public static byte[] file2Bytes(File file) {
        byte[] ret = null;
        try {
            if (file == null) {
                return null;
            }
            FileInputStream in = new FileInputStream(file);
            ByteArrayOutputStream out = new ByteArrayOutputStream(4096);
            byte[] b = new byte[4096];
            int n;
            while ((n = in.read(b)) != -1) {
                out.write(b, 0, n);
            }
            in.close();
            out.close();
            ret = out.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ret;
    }

    /**
     * 文件转byte
     * file转byte
     *
     * @param path 文件路径
     * @return
     */
    public static byte[] file2Bytes(String path) {
        if (TextUtils.isEmpty(path)) {
            return null;
        }

        return file2Bytes(new File(path));
    }

    /**
     * 判断文件是否存在
     *
     * @param file 文件
     * @return true:存在,false:不存在
     */
    public static boolean isFileExists(File file) {
        return file != null && file.exists();
    }

    /**
     * 重命名文件
     *
     * @param filePath 文件路径
     * @param newName  新名称
     * @return {@code true}: 重命名成功<br>{@code false}: 重命名失败
     */
    public static boolean rename(String filePath, String newName) {
        return rename(new File(filePath), newName);
    }

    /**
     * 重命名文件
     * 注意：在Windows上测试时发现，文件名大小写不敏感
     *
     * @param file    文件
     * @param newName 新名称
     * @return true:重命名成功,false:重命名失败
     */
    public static boolean rename(File file, String newName) {
        if (TextUtils.isEmpty(newName)) {
            return false;
        }
        if (file == null || !file.exists()) {
            return false;
        }
        // 如果文件名没有改变返回true
        if (newName.equals(file.getName())) return true;
        File newFile = new File(file.getParent() + File.separator + newName);
        //如果新文件不存在返回false
        if (!newFile.exists()) {
            return false;
        }
        return file.renameTo(newFile);
    }

    /**
     * 创建目录
     *
     * @param file
     * @return
     */
    public static boolean createDirs(File file) {
        if (file != null && !file.exists()) {
            return file.mkdirs();
        }
        return false;
    }

    /**
     * 创建文件并删除旧文件
     *
     * @param file
     * @return
     */
    public static boolean createFileByDeleteOldFile(File file) {
        if (file != null && file.exists() && file.isFile() && !file.delete()) {
            return false;
        }
        try {
            return file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 获取全路径中的文件名
     *
     * @param file 文件
     * @return 文件名
     */
    public static String getFileName(File file) {
        if (file == null) return null;
        return getFileName(file.getPath());
    }

    /**
     * 获取全路径中的文件名
     *
     * @param filePath 文件路径
     * @return 文件名
     */
    public static String getFileName(String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            return filePath;
        }
        int lastSep = filePath.lastIndexOf(File.separator);
        return lastSep == -1 ? filePath : filePath.substring(lastSep + 1);
    }

    /**
     * 获取全路径中的不带拓展名的文件名
     *
     * @param file 文件
     * @return 不带拓展名的文件名
     */
    public static String getFileNameNoExtension(File file) {
        if (file == null) return null;
        return getFileNameNoExtension(file.getPath());
    }

    /**
     * 获取全路径中的不带拓展名的文件名
     *
     * @param filePath 文件路径
     * @return 不带拓展名的文件名
     */
    public static String getFileNameNoExtension(String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            return filePath;
        }
        int lastPoi = filePath.lastIndexOf('.');
        int lastSep = filePath.lastIndexOf(File.separator);
        if (lastSep == -1) {
            return (lastPoi == -1 ? filePath : filePath.substring(0, lastPoi));
        }
        if (lastPoi == -1 || lastSep > lastPoi) {
            return filePath.substring(lastSep + 1);
        }
        return filePath.substring(lastSep + 1, lastPoi);
    }

    /**
     * 获取路径中的文件拓展名
     *
     * @param file
     * @return
     */
    public static String getFileExtension(File file) {
        return getFileExtension(file.getPath());
    }

    /**
     * 获取路径中的文件拓展名
     *
     * @param filePath 文件路径
     * @return 文件拓展名, 文件名的后缀
     */
    public static String getFileExtension(String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            return filePath;
        }
        int lastPoi = filePath.lastIndexOf('.');
        int lastSep = filePath.lastIndexOf(File.separator);
        if (lastPoi == -1 || lastSep >= lastPoi) return "";
        return filePath.substring(lastPoi + 1);
    }

    /**
     * 获取文件的MD5校验码
     *
     * @param file 文件
     * @return 文件的MD5校验码
     */
    public static String getFileMD5ToString(File file) {
        return ConvertUtil.bytes2HexString(getFileMD5(file));
    }

    /**
     * 获取文件的MD5校验码
     *
     * @param file 文件
     * @return 文件的MD5校验码
     */
    public static byte[] getFileMD5(File file) {
        if (file == null) return null;
        DigestInputStream dis = null;
        try {
            FileInputStream fis = new FileInputStream(file);
            MessageDigest md = MessageDigest.getInstance("MD5");
            dis = new DigestInputStream(fis, md);
            byte[] buffer = new byte[1024 * 256];
            while (dis.read(buffer) > 0) ;
            md = dis.getMessageDigest();
            return md.digest();
        } catch (NoSuchAlgorithmException | IOException e) {
            e.printStackTrace();
        } finally {
            CloseUtil.closeIO(dis);
        }
        return null;
    }

    public static boolean deleteFilesInDir(String path) {
        if (TextUtils.isEmpty(path)) {
            return false;
        }
        return deleteFilesInDir(new File(path));
    }

    /**
     * 删除目录下的所有文件
     *
     * @param dir 目录
     * @return {@code true}: 删除成功<br>{@code false}: 删除失败
     */
    public static boolean deleteFilesInDir(File dir) {
        if (dir == null) return false;
        // 目录不存在返回true
        if (!dir.exists()) return true;
        // 不是目录返回false
        if (!dir.isDirectory()) return false;
        // 现在文件存在且是文件夹
        File[] files = dir.listFiles();
        if (files != null && files.length != 0) {
            for (File file : files) {
                if (file.isFile()) {
                    if (!deleteFile(file)) return false;
                } else if (file.isDirectory()) {
                    if (!deleteDir(file)) return false;
                }
            }
        }
        return true;
    }

    /**
     * 删除目录
     *
     * @param dir 目录
     * @return {@code true}: 删除成功<br>{@code false}: 删除失败
     */
    public static boolean deleteDir(File dir) {
        if (dir == null) return false;
        // 目录不存在返回true
        if (!dir.exists()) return true;
        // 不是目录返回false
        if (!dir.isDirectory()) return false;
        // 现在文件存在且是文件夹
        File[] files = dir.listFiles();
        if (files != null && files.length != 0) {
            for (File file : files) {
                if (file.isFile()) {
                    if (!deleteFile(file)) return false;
                } else if (file.isDirectory()) {
                    if (!deleteDir(file)) return false;
                }
            }
        }
        return dir.delete();
    }

    /**
     * 删除文件
     *
     * @param file 文件
     * @return {@code true}: 删除成功<br>{@code false}: 删除失败
     */
    public static boolean deleteFile(File file) {
        return file != null && (!file.exists() || file.isFile() && file.delete());
    }

    /*****************复制文件的3种方式 start**********************/
    private static void copyFileUsingFileStreams(File source, File dest)
            throws IOException {
        InputStream input = null;
        OutputStream output = null;
        try {
            input = new FileInputStream(source);
            output = new FileOutputStream(dest);
            byte[] buf = new byte[1024];
            int bytesRead;
            while ((bytesRead = input.read(buf)) > 0) {
                output.write(buf, 0, bytesRead);
            }
        } finally {
            CloseUtil.closeIO(input, output);
        }
    }

    /**
     * FileChannel的复制方式是最快的
     *
     * @param source
     * @param dest
     * @throws IOException
     */
    private static void copyFileUsingFileChannels(File source, File dest) throws IOException {
        FileChannel inputChannel = null;
        FileChannel outputChannel = null;
        try {
            inputChannel = new FileInputStream(source).getChannel();
            outputChannel = new FileOutputStream(dest).getChannel();
            outputChannel.transferFrom(inputChannel, 0, inputChannel.size());
        } finally {
            CloseUtil.closeIO(inputChannel, outputChannel);
        }
    }

    /**
     * 调用java 7中的复制方法
     *
     * @param source
     * @param dest
     * @throws IOException
     */
    private static void copyFileUsingJava7Files(File source, File dest)
            throws IOException {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Files.copy(source.toPath(), dest.toPath());
        }
    }
    /*****************复制文件的3种方式 end**********************/
}
