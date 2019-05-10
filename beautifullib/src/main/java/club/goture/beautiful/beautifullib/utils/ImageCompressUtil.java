package club.goture.beautiful.beautifullib.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import club.goture.beautiful.beautifullib.callback.OnCompressResultListener;
import club.goture.beautiful.beautifullib.config.Config;

/**
 * 压缩工具
 */
public class ImageCompressUtil implements Handler.Callback {
    private static final String TAG = "ImageCompressUtil";
    private final Config config;
    private Handler mHandler ;
    private OnCompressResultListener onCompressResultListener;
    private Thread thread;

    public ImageCompressUtil(Config config) {
        this.config = config;
        this.mHandler= new Handler(Looper.myLooper(),this);
    }


    public void compress(final String originalPath, final OnCompressResultListener onCompressResultListener) {
        this.onCompressResultListener = onCompressResultListener;
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                compressImage(originalPath);
            }
        });
        thread.start();
    }

    private void compressImage(String originalPath) {

        String compress = compress(originalPath);
        if (mHandler != null) {
            Message msg = mHandler.obtainMessage();
            msg.what = 1;
            msg.obj = compress;
        }
    }

    //图片压缩的方法
    public String compress(String filePath) {

        if (TextUtils.isEmpty(filePath))
            return filePath;

        File file = new File(filePath);
        if (!file.exists())//判断路径是否存在
            return filePath;

        if (file.length() < 1)//文件是否为空
            return null;

        Log.d(TAG, "compress: " + config.getZipCacheDir());
        File tempFile = FileUtil.getDir(config.getZipCacheDir());
        String outImagePath = tempFile.getAbsolutePath(); // 输出图片文件路径


        int degree = getBitmapDegree(filePath); // 检查图片的旋转角度

        //谷歌官网压缩图片
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);

        options.inSampleSize = calculateInSampleSize(options, config.getMaxPixel(), config.getMaxPixel());
        options.inJustDecodeBounds = false;
        Bitmap bitmap = BitmapFactory.decodeFile(filePath, options);

        // 旋转:这步处理主要是为了处理三星手机拍的照片
        if (degree > 0) {
            bitmap = rotateBitmapByDegree(bitmap, degree);
        }

        // 写入文件
        FileOutputStream fos;
        try {
            fos = new FileOutputStream(tempFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, config.getQuality(), fos);
            fos.flush();
            fos.close();
            bitmap.recycle();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return filePath;
        } catch (Exception e) {
            e.printStackTrace();
            return filePath;
        }

        return outImagePath;
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int picheight = options.outHeight;
        final int picwidth = options.outWidth;

        int targetheight = picheight;
        int targetwidth = picwidth;
        int inSampleSize = 1;
        if (targetheight > reqHeight || targetwidth > reqWidth) {
            while (targetheight >= reqHeight && targetwidth >= reqWidth) {
                inSampleSize += 1;
                targetheight = picheight / inSampleSize;
                targetwidth = picwidth / inSampleSize;
            }
        }

        Log.i("--->", "最终压缩比例:" + inSampleSize + "倍/新尺寸:" + targetwidth + "*" + targetheight);
        return inSampleSize;
    }

    /**
     * 读取图片的旋转的角度
     *
     * @param path 图片绝对路径
     * @return 图片的旋转角度
     */
    public static int getBitmapDegree(String path) {
        int degree = 0;
        try {
            // 从指定路径下读取图片，并获取其EXIF信息
            ExifInterface exifInterface = new ExifInterface(path);
            // 获取图片的旋转信息
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return degree;
    }

    /**
     * 将图片按照某个角度进行旋转
     *
     * @param bm     需要旋转的图片
     * @param degree 旋转角度
     * @return 旋转后的图片
     */
    public static Bitmap rotateBitmapByDegree(Bitmap bm, int degree) {
        Bitmap returnBm = null;

        // 根据旋转角度，生成旋转矩阵
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        try {
            // 将原始图片按照旋转矩阵进行旋转，并得到新的图片
            returnBm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
            //当内存溢出时，利用递归进行重新旋转
            while (returnBm == null) {
                System.gc();
                System.runFinalization();
                returnBm = rotateBitmapByDegree(bm, degree);
            }
        }
        if (returnBm == null) {
            returnBm = bm;
        }
        if (bm != returnBm) {
            bm.recycle();
        }
        return returnBm;
    }

    @Override
    public boolean handleMessage(Message msg) {
        if (onCompressResultListener!=null){
            onCompressResultListener.onSuccess((String) msg.obj);
        }
        return false;

    }
}
