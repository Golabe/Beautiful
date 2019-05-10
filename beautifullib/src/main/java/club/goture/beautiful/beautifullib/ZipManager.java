package club.goture.beautiful.beautifullib;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.List;

import club.goture.beautiful.beautifullib.bean.Photo;
import club.goture.beautiful.beautifullib.callback.OnCompressResultListener;
import club.goture.beautiful.beautifullib.callback.OnZipListener;
import club.goture.beautiful.beautifullib.callback.OnZipManagerListener;
import club.goture.beautiful.beautifullib.config.Config;
import club.goture.beautiful.beautifullib.utils.ImageCompressUtil;

public class ZipManager implements OnZipManagerListener {
    private List<Photo> photos;
    private Config config;
    private OnZipListener onZipListener;
    private ImageCompressUtil imageCompressUtil;

    public ZipManager( List<Photo> photos, Config config, OnZipListener onZipListener) {
        this.photos = photos;
        this.config = config;
        this.onZipListener = onZipListener;
        this.imageCompressUtil = new ImageCompressUtil(config);

    }


    @Override
    public void compress() {

        switch (config.getZipType()) {
            case IMAGE:
                compressImage();
                break;
            case FILE:
                break;
            case VIDEO:
                break;
        }
        if (onZipListener!=null){

            onZipListener.onStart();
        }
        compress(photos.get(0));
        if (onZipListener!=null){

            onZipListener.onComplete();
        }
    }

    private void compressImage() {

        if (photos == null || photos.isEmpty()) {
            if (onZipListener!=null){
                onZipListener.onFailure(photos, "list is empty");
            }
            return;
        }

        for (Photo photo : photos) {
            if (photo == null) {
                if (onZipListener!=null){
                    onZipListener.onFailure(photos, "image is empty");
                }
                return;
            }

        }
    }

    private void compress(final Photo photo) {
        //文件为空
        if (TextUtils.isEmpty(photo.getOriginalPath())) {
            continueCompress(photo, false);
            return;
        }

        //文件不存在
        File file = new File(photo.getOriginalPath());
        if (!file.exists() || !file.isFile()) {
            continueCompress(photo, false);
            return;
        }
        //忽略文件
        if (!photo.isFilter()) {
            continueCompress(photo, false);
            return;
        }
        //忽略大小
        if (file.length() < config.getIgnoreSize()) {
            continueCompress(photo, true);
            return;
        }
        //压缩
        imageCompressUtil.compress(photo.getOriginalPath(), new OnCompressResultListener() {
            @Override
            public void onSuccess(String path) {
                photo.setCompressPath(path);
                continueCompress(photo, true);
            }

            @Override
            public void onFailure(String path, String e) {
                continueCompress(photo, false, e);
            }
        });

    }

    private void continueCompress(Photo photo, boolean bool, String... e) {
        photo.setCompressed(bool);
        int index = photos.indexOf(photo);
        if (index == photos.size() - 1) {
            handlerCallback(e);
        } else {
            compress(photos.get(index + 1));
        }
    }

    private void handlerCallback(String... e) {
        if (e.length > 0) {
            if (onZipListener!=null){
                onZipListener.onFailure(photos, e[0]);
                return;
            }
        }
        for (Photo photo : photos) {
            if (!photo.isCompressed()) {
                if (onZipListener!=null){
                    onZipListener.onFailure(photos, photo.getOriginalPath() + "zip failure");
                    return;
                }
            }
        }
        if (onZipListener!=null){
            onZipListener.onSuccess(photos);
        }

    }

}
