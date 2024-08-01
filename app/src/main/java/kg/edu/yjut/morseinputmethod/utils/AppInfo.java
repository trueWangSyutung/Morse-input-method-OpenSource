package kg.edu.yjut.morseinputmethod.utils;


import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Date;

import kg.edu.yjut.morseinputmethod.view.AboutActivity;

public class AppInfo {
    /**
     * 获取当前本地apk的版本
     *
     * @param mContext
     * @return
     */
    public static int getVersionCode(Context mContext) {
        int versionCode = 0;
        try {
            //获取软件版本号，对应AndroidManifest.xml下android:versionCode
            versionCode = mContext.getPackageManager().
                    getPackageInfo(mContext.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode;
    }

    /**
     * 获取版本号名称
     *
     * @param context 上下文
     * @return
     */
    public static String getVerName(Context context) {
        String verName = "";
        try {
            verName = context.getPackageManager().
                    getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return verName;
    }

    /**
     * 获取编译类型
     *
     * @param context 上下文
     * @return
     */
    public static String getBuildType(Context context) {
        String buildType = "";
        // 获取 元数据

        return "release";
    }

    public static String getLastTime(Context context) {
        String buildType = "";
        try {
            SimpleDateFormat dataFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            buildType =  dataFormat.format(new Date(context.getPackageManager().
                    getPackageInfo(context.getPackageName(), 0).lastUpdateTime));
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return buildType;
    }

    @NotNull
    public static String getPackageName(Context context) {
        return context.getPackageName();
    }
}