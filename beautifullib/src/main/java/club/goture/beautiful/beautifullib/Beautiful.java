package club.goture.beautiful.beautifullib;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import club.goture.beautiful.beautifullib.bean.Photo;
import club.goture.beautiful.beautifullib.callback.OnZipFilterListener;
import club.goture.beautiful.beautifullib.callback.OnZipListener;
import club.goture.beautiful.beautifullib.config.Config;
import club.goture.beautiful.beautifullib.enums.ZipType;
import club.goture.beautiful.beautifullib.utils.Constants;

public class Beautiful {

    private static final String TAG = "Beautiful";
    private static Activity activity;


    private Beautiful(Builder builder) {
        Config config = new Config();
        config.setEnableOriginal(builder.enableOriginal);
        config.setEnablePixelZip(builder.enablePixelZip);
        config.setEnableProgress(builder.enableProgress);
        config.setEnableQualityZip(builder.enableQualityZip);
        config.setIgnoreMinPixel(builder.ignoreMinPixel);
        config.setIgnoreSize(builder.ignoreSize);
        config.setMaxPixel(builder.maxPixel);
        config.setQuality(builder.quality);
        config.setZipCacheDir(builder.zipCacheDir);
        config.setZipType(builder.zipType);
        new ZipManager( builder.photos, config, builder.onZipListener)
                .compress();
        Log.d(TAG, "with: " + builder.photos);
    }




    public static class Builder {
        private List<Photo> photos = new ArrayList<>();
        private int ignoreMinPixel = 1000;
        private int ignoreSize = 100 * 1024;
        private boolean enableProgress = false;
        private int maxPixel = 1000;
        private boolean enablePixelZip = true;
        private boolean enableQualityZip = true;
        private int quality = 90;
        private boolean enableOriginal = false;
        private String zipCacheDir = Constants.BASE_CACHE_PATH + Constants.COMPRESS_PATH;
        private ZipType zipType = ZipType.IMAGE;
        private OnZipListener onZipListener = null;


        /**
         * 忽略最小像素
         *
         * @param pixel
         * @return
         */
        public Builder ignoreMinPixel(int pixel) {
            this.ignoreMinPixel = pixel;
            return this;
        }


        /**
         * 忽略大小 KB
         *
         * @param size
         * @return
         */
        public Builder ignoreSize(int size) {
            this.ignoreSize = size * 1024;
            return this;
        }

        /**
         * 是否显示进度条
         *
         * @param enable
         * @return
         */
        public Builder enableProgress(boolean enable) {
            this.enableProgress = enable;
            return this;
        }

        /**
         * 长宽不超过最大像素
         *
         * @param pixel
         * @return
         */
        public Builder maxPixel(int pixel) {
            this.maxPixel = pixel;
            return this;
        }

        /**
         * 加载文件
         *
         * @param path
         * @return
         */
        public Builder load(String path) {
            this.photos.clear();
            Photo photo = new Photo();
            photo.setOriginalPath(path);
            this.photos.add(photo);
            return this;
        }

        /**
         * 加载文件
         *
         * @param paths
         * @return
         */
        public Builder load(List<String> paths) {
            this.photos.clear();
            for (String path : paths) {
                Photo photo = new Photo();
                photo.setOriginalPath(path);
                this.photos.add(photo);
            }
            return this;
        }

        /**
         * 是否开启像素压缩
         *
         * @param enable
         * @return
         */
        public Builder enablePixelZip(boolean enable) {
            this.enablePixelZip = enable;
            return this;
        }

        /**
         * 是否开启质量压缩
         *
         * @param enable
         * @return
         */
        public Builder enableQualityZip(boolean enable) {
            this.enableQualityZip = enable;
            return this;
        }

        /**
         * 质量 最大100 最小0
         *
         * @param quality
         * @return
         */
        public Builder quality(int quality) {
            this.quality = quality;
            return this;
        }

        /**
         * 是否保留原始文件
         *
         * @param enable
         * @return
         */
        public Builder enableOriginal(boolean enable) {
            this.enableOriginal = enable;
            return this;
        }

        /**
         * 压缩临时目录
         *
         * @param cache
         * @return
         */
        public Builder zipCacheDir(String cache) {
            this.zipCacheDir = cache;
            return this;
        }

        /**
         * 过滤条件
         *
         * @param onZipFilterListener
         * @return
         */
        public Builder filter(OnZipFilterListener onZipFilterListener) {
            if (photos == null || photos.isEmpty()) return this;
            for (Photo photo : photos) {
                boolean apply = onZipFilterListener.apply(photo.getOriginalPath());
                photo.setFilter(apply);
            }
            return this;
        }

        /**
         * 重命名
         *
         * @return
         */
        public Builder rename() {
            return this;
        }

        /**
         * 压缩类型 IMAGE ,FILE ,VIDEO
         *
         * @param type
         * @return
         */
        public Builder zipType(ZipType type) {
            this.zipType = type;
            return this;
        }

        /**
         * 开始压缩
         *
         * @param onZipListener
         * @return
         */
        public Beautiful zip(OnZipListener onZipListener) {
            this.onZipListener = onZipListener;
            return new Beautiful(this);
        }
    }
}
