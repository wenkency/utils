package cn.carhouse.utils.crypt;

import java.security.MessageDigest;

/**
 * 签名工具类
 */
public class MessageDigestUtils {

    public static final String MD5(String content) {
        return digest(content, "MD5");
    }


    public static final String SHA1(String content) {
        return digest(content, "SHA-1");
    }

    public static final String SHA256(String content) {
        return digest(content, "SHA-256");
    }

    private static final String digest(String content, String algorithm) {
        try {
            MessageDigest digest = MessageDigest.getInstance(algorithm);
            byte[] bytes = digest.digest(content.getBytes("UTF-8"));
            return toHex(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return content;
    }

    // 转成16进制
    private static String toHex(byte[] bytes) {
        StringBuffer sb = new StringBuffer();
        for (byte value : bytes) {
            int hex = value & 255;
            String hexStr = Integer.toHexString(hex);
            // 如果长度只有1就默认前面加0补
            if (hexStr.length() == 1) {
                sb.append("0");
            }
            sb.append(hexStr);
        }
        return sb.toString();
    }
}
