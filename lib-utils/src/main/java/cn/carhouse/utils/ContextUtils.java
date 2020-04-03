package cn.carhouse.utils;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;

import java.util.List;

/**
 * Context帮助类
 */
public class ContextUtils {
    private static Application mApplication;

    public static void init(Application application) {
        mApplication = application;
    }

    private static void checkInit() {
        if (mApplication == null) {
            init(ApplicationUtils.getApplication());
        }
    }

    public static Context getContext() {
        checkInit();
        return mApplication.getApplicationContext();
    }


    public static Application getApplication() {
        checkInit();
        return mApplication;
    }

    /**
     * 是不是主进程
     */
    public static boolean isMainProcess() {
        checkInit();
        ActivityManager am = ((ActivityManager) mApplication.getSystemService(Context.ACTIVITY_SERVICE));
        List<ActivityManager.RunningAppProcessInfo> processInfos = am.getRunningAppProcesses();
        String mainProcessName = mApplication.getPackageName();
        int myPid = android.os.Process.myPid();
        for (ActivityManager.RunningAppProcessInfo info : processInfos) {
            if (info.pid == myPid && mainProcessName.equals(info.processName)) {
                return true;
            }
        }
        return false;
    }
}
