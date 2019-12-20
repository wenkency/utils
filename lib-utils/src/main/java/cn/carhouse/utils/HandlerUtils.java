package cn.carhouse.utils;

import android.os.Handler;
import android.os.Looper;

/**
 * Handler单例
 */
public class HandlerUtils {
    private static Handler mHandler = new Handler(Looper.getMainLooper());

    public static Handler getHandler() {
        return mHandler;
    }

    /**
     * 是否运行在主线程
     *
     * @return
     */
    public static boolean isRunInMainThread() {
        return Looper.myLooper() == Looper.getMainLooper();
    }

    /**
     * 在主线程执行一个任务
     *
     * @param runnable
     */
    public static void runInMainThread(Runnable runnable) {
        if (isRunInMainThread()) {
            // 在主线程
            runnable.run();
        } else {
            // 不在主线程
            mHandler.post(runnable);
        }
    }
}
