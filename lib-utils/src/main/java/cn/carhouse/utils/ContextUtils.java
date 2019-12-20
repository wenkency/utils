package cn.carhouse.utils;

import android.app.Application;
import android.content.Context;

/**
 * Context帮助类
 */
public class ContextUtils {
    private static Application mApplication;

    public static void init(Application application) {
        mApplication = application;
    }

    public static Context getContext() {
        return mApplication.getApplicationContext();
    }

    public static Application getApplication() {
        return mApplication;
    }


}
