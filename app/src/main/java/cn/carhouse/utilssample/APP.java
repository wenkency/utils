package cn.carhouse.utilssample;

import android.app.Application;

import cn.carhouse.utils.ActivityUtils;

public class APP extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ActivityUtils.register(this);
    }
}
