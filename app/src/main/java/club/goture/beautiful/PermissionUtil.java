package club.goture.beautiful;

import android.app.Activity;
import android.content.Context;

import pub.devrel.easypermissions.EasyPermissions;

public class PermissionUtil {
    /**
     *
     * @param context
     * return true:已经获取权限
     * return false: 未获取权限，主动请求权限
     */
    public static boolean checkPermission(Context context, String perm[]){
        return EasyPermissions.hasPermissions(context,perm);
    }

    /**
     * 请求权限
     * @param context
     */
    public static void requestPermission(Activity context, String tip, int requestCode, String[] perms) {
        EasyPermissions.requestPermissions(context, tip,requestCode,perms);
    }

}
