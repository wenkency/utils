package cn.carhouse.utils;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;

/**
 * Handler单例
 */
public class HandlerUtils {
    private static volatile HandlerUtils instance;
    private Handler mHandler = new Handler(Looper.getMainLooper());

    private HandlerUtils() {
    }

    public static HandlerUtils getInstance() {
        if (instance == null) {
            synchronized (HandlerUtils.class) {
                if (instance == null) {
                    instance = new HandlerUtils();
                }
            }
        }
        return instance;
    }

    /**
     * 是否运行在主线程
     *
     * @return
     */
    public boolean isRunInMainThread() {
        return Looper.myLooper() == Looper.getMainLooper();
    }

    /**
     * 在主线程执行一个任务
     *
     * @param runnable
     */
    public void runInMainThread(Runnable runnable) {
        if (isRunInMainThread()) {
            // 在主线程
            runnable.run();
        } else {
            Activity currentActivity = ActivityUtils.getInstance().getCurrentActivity();
            if (currentActivity != null) {
                currentActivity.runOnUiThread(runnable);
                return;
            }
            // 不在主线程
            mHandler.post(runnable);
        }
    }

    public void postDelayed(Runnable runnable, long delayMillis) {
        mHandler.postDelayed(runnable, delayMillis);
    }

    public void post(Runnable runnable) {
        mHandler.post(runnable);
    }
}
