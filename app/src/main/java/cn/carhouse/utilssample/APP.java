package cn.carhouse.utilssample;

import android.app.Application;

import cn.carhouse.utils.ActivityUtils;
import cn.carhouse.utils.ConfigUtils;

public class APP extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ConfigUtils.init(this);
    }
}
