package cn.carhouse.utils;

import android.text.TextUtils;

/**
 * String处理工具类
 */
public class StringUtils {
    /**
     * 判断字符串是否有值
     */
    public static boolean isEmpty(String value) {
        return !TextUtils.isEmpty(value) && !"null".equalsIgnoreCase(value) && !"NaN".equalsIgnoreCase(value);
    }


}
