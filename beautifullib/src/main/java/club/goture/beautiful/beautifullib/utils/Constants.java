package club.goture.beautiful.beautifullib.utils;

import android.os.Environment;

import java.io.File;

public class Constants {
    public static final String BASE_CACHE_PATH = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath() + File.separator;
    public static final String COMPRESS_PATH = "compress_cache";
}
