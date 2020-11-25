package cn.carhouse.utils;

import android.app.Application;

/**
 * 用来配置
 */
public class ConfigUtils {
    public static void init(Application application) {
        // 初始化配置
        if (ContextUtils.isMainProcess(application)) {
            ActivityUtils.register(application);
            ContextUtils.getInstance().init(application);
        }
    }
}
