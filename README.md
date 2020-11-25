# Android 开发常用工具类
1. Android 开发常用一系列工具类：ContextUtils、ActivityUtils、LogUtils、
### 引入

```
allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}

implementation 'com.github.wenkency:utils:1.6.0'

```


### 初始化

```
public class APP extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ConfigUtils.init(this);
    }
}

```