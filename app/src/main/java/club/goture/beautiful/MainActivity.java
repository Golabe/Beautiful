package club.goture.beautiful;

import android.Manifest;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.entity.LocalMedia;

import java.io.File;
import java.util.List;

import club.goture.beautiful.beautifullib.Beautiful;
import club.goture.beautiful.beautifullib.bean.Photo;
import club.goture.beautiful.beautifullib.callback.OnZipFilterListener;
import club.goture.beautiful.beautifullib.callback.OnZipListener;
import club.goture.beautiful.beautifullib.enums.ZipType;
import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {
    private static final String TAG = "MainActivity";
    private static final String[] prems = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (PermissionUtil.checkPermission(this, prems)) {

        } else {
            PermissionUtil.requestPermission(this, "需要权限", 0, prems);
        }

        findViewById(R.id.btn_camera).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PictureUtils.openCameraNoCrop(MainActivity.this, 1111);
            }
        });


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1111) {
                List<LocalMedia> photoChoose = PictureSelector.obtainMultipleResult(data);
                String path = photoChoose.get(0).getPath();
                Log.d(TAG, "onActivityResult: " + path);
                compress(path);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {

    }

    private void compress(String path) {
        new  Beautiful.Builder()
                .load(path)
                .zipType(ZipType.IMAGE)
                .ignoreMinPixel(1000)
                .filter(new OnZipFilterListener() {
                    @Override
                    public boolean apply(String path) {
                        return !(TextUtils.isEmpty(path) || path.toLowerCase().endsWith(".gif"));
                    }
                })
                .rename()
                .maxPixel(1000)
                .enablePixelZip(true)
                .enableQualityZip(true)
                .quality(100)
                .enableOriginal(true)
//                .zipCacheDir("")
                .ignoreSize(200)
                .enableProgress(true)
                .zip(new OnZipListener() {
                    @Override
                    public void onStart() {
                        Log.d(TAG, "onStart: ");
                    }

                    @Override
                    public void onProgress(int progress) {
                        Log.d(TAG, "onProgress: ");
                    }

                    @Override
                    public void onSuccess(List<Photo> photos) {
                        for (int i = 0; i < photos.size(); i++) {
                            Log.d(TAG, "onSuccess: " + photos.get(i).getCompressPath());
                        }
                    }

                    @Override
                    public void onFailure(List<Photo> photos, String e) {
                        Log.d(TAG, "onFailure: ==============" + e);
                        for (int i = 0; i < photos.size(); i++) {
                            Log.d(TAG, "onFailure: " + photos.get(i).getOriginalPath());
                        }
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete: ");
                    }
                });
    }
}
