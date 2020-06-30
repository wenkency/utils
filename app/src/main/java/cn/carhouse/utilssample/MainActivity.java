package cn.carhouse.utilssample;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import javax.crypto.Cipher;

import cn.carhouse.utils.DateUtils;
import cn.carhouse.utils.DensityUtils;
import cn.carhouse.utils.SPUtils;
import cn.carhouse.utils.TSUtils;
import cn.carhouse.utils.crypt.MessageDigestUtils;
import cn.carhouse.utils.crypt.symmetric.AESUtil;

public class MainActivity extends AppCompatActivity {

    private TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv = findViewById(R.id.tv);
        SPUtils.putString("key", "test");
    }

    public void test(View view) {
        TSUtils.show("我测试一下咯");
        int screenHeight = DensityUtils.getScreenHeight();
        int dp = DensityUtils.dp2px(10);
        String test = SPUtils.getString("key");
        StringBuffer sb=new StringBuffer("screenHeight:" + screenHeight
                + "\ndp:" + dp
                + "\nvalue:" + test
                + "\ngetTime:" + DateUtils.getTime(System.currentTimeMillis())+"\n");
        String value = "刘付文LVen";
        String password = "asd123456789jkl123456789";
        String encode = AESUtil.aes(value, password, Cipher.ENCRYPT_MODE);
        sb.append("AES encode:"+encode+"\n");
        sb.append("AES decode:"+AESUtil.aes(encode, password, Cipher.DECRYPT_MODE)+"\n");

        sb.append("MD5:" + MessageDigestUtils.MD5(value) + ":" + MessageDigestUtils.MD5(value).length()+"\n");
        sb.append("SHA1:"+MessageDigestUtils.SHA1(value)+ ":" +MessageDigestUtils.SHA1(value).length()+"\n");
        sb.append("SHA256:"+MessageDigestUtils.SHA256(value)+ ":" +MessageDigestUtils.SHA256(value).length()+"\n");
        tv.setText(sb.toString());
    }
}
