package com.example.admin.imagecompress;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.admin.imagecompress.manager.FileManager;
import com.example.compress.CompressionPredicate;
import com.example.compress.Luban;
import com.example.compress.OnCompressListener;
import com.example.compress.OnRenameListener;
import com.example.zfutils.ConvertUtil;
import com.example.zfutils.FileUtil;
import com.example.zfutils.ImageUtil;
import com.example.zfutils.IntentUtil;
import com.example.zfutils.MediaStoreUtil;
import com.example.zfutils.ToastUtil;

import java.io.File;
import java.io.IOException;
import java.util.List;

import cn.com.zf.library.PermissionCallback;
import cn.com.zf.library.PermissionGroup;
import cn.com.zf.library.ZPermissions;

public class MainActivity extends AppCompatActivity {
    public static final int code = 0;
    Button bt;
    Button bt1;
    EditText et;
    ImageView iv;
    ImageView iv1;
    TextView tv;
    TextView tv1;

    String comFilePath;//压缩后的文件路径

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bt = findViewById(R.id.bt);
        bt1 = findViewById(R.id.bt1);
        et = findViewById(R.id.et);
        iv = findViewById(R.id.iv);
        iv1 = findViewById(R.id.iv1);
        tv = findViewById(R.id.tv);
        tv1 = findViewById(R.id.tv1);
        bt.setOnClickListener(v -> {
            IntentUtil.choosePhoto(this, code);
        });
        //点击按钮，将图片高斯模糊处理
        bt1.setOnClickListener(v -> {
            long ct = System.currentTimeMillis();
            Bitmap bitmap = ImageUtil.fastBlur(MainActivity.this,
                    ConvertUtil.bytes2Bitmap(FileUtil.file2Bytes(comFilePath)), 0.1f, getRadius());
            long c = System.currentTimeMillis() - ct;
            ToastUtil.showLongToast(MainActivity.this, "高斯模糊耗时是" + c + "ms");
            iv1.setImageBitmap(bitmap);
        });
        requestPermission();
    }

    private void requestPermission() {
        ZPermissions.requestPermissions(MainActivity.this,
                PermissionReqCode.CODE0, new PermissionCallback() {
                    @Override
                    public void permissionGrant(int i) {
                        FileManager.initDir();
                    }

                    @Override
                    public void permissionDenied(int i) {

                    }
                }, PermissionGroup.build(
                        PermissionGroup.SD(),
                        PermissionGroup.CAMERA()
                ));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ZPermissions.recycleCallback();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        ZPermissions.onRequestPermissionsResult(requestCode, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            switch (requestCode) {
                case code:
                    Uri uri = data.getData();
                    Log.d("FileManager", "uri = " + uri);
                    String realFilePath = MediaStoreUtil.getRealFilePath(this, uri);
                    Log.d("FileManager", "realFilePath = " + realFilePath);
                    setMasterMap(realFilePath);
                    asyncComp(realFilePath);
                    break;
            }
        }
    }

    private float getRadius() {
        String radius = et.getText().toString();
        if (TextUtils.isEmpty(radius)) {
            return 10f;
        }
        if (radius.equals("0") || radius.equals("25")) {
            return 10f;
        }
        return Float.valueOf(radius);
    }

    /**
     * 设置原图
     */
    private void setMasterMap(String realFilePath) {
        Bitmap bitmap = ConvertUtil.bytes2Bitmap(FileUtil.file2Bytes(realFilePath));
        iv.setImageBitmap(bitmap);
        tv.setText(FileUtil.fileSize(realFilePath));
    }

    /**
     * 设置压缩图片
     *
     * @param path
     */
    private void setComImage(String path) {
        Bitmap bitmap = ConvertUtil.bytes2Bitmap(FileUtil.file2Bytes(path));
        iv1.setImageBitmap(bitmap);
        tv1.setText(FileUtil.fileSize(path));
    }

    /**
     * 同步压缩图片
     *
     * @param realFilePath 真实的文件路径
     */
    private void syncComp(String realFilePath) {
        try {
            List<File> files = Luban.with(this)
                    .load(realFilePath)
//                    .ignoreBy(100)
                    .setTargetDir(FileManager.getDir())
                    .setRenameListener(new OnRenameListener() {
                        @Override
                        public String rename(String filePath) {
                            Log.d("FileManager", "filePath is " + FileUtil.getFileName(filePath));
                            return FileUtil.getFileName(filePath);
                        }
                    })
                    .get();

            if (files.isEmpty()) {
                Log.d("FileManager", "comp file is null");
            } else {
                Log.d("FileManager", "comp file = " + files.get(0).getAbsolutePath());
                setComImage(files.get(0).getAbsolutePath());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 异步压缩图片
     */
    private void asyncComp(String realFilePath) {
        Luban.with(this)
                .load(realFilePath)//导入图片
                .filter(new CompressionPredicate() {
                    @Override
                    public boolean apply(String path) {
                        return !(TextUtils.isEmpty(path) || path.toLowerCase().endsWith(".gif"));
                    }
                })//设置开启压缩条件
                .ignoreBy(100)//不压缩的阈值，单位为K,也就是说低于100k的图片不压缩
                .setTargetDir(FileManager.getDir())//设置返回的压缩图片的父目录
                .setRenameListener(new OnRenameListener() {
                    @Override
                    public String rename(String filePath) {
                        Log.d("FileManager", "filePath is " + FileUtil.getFileName(filePath));
                        return FileUtil.getFileName(filePath);
                    }
                })//设置压缩后的图片的名称
                .setCompressListener(new OnCompressListener() {
                    @Override
                    public void onStart() {//开始压缩
                        Log.d("FileManager", "comp file start");
                    }

                    @Override
                    public void onSuccess(File file) {//压缩成功
                        Log.d("FileManager", "comp file success, file path is " + file.getPath());
                        comFilePath = file.getPath();
                        setComImage(file.getPath());
                    }

                    @Override
                    public void onError(Throwable e) {//压缩失败
                        Log.d("FileManager", "comp file fail");
                    }
                })
                .launch();
    }
}
