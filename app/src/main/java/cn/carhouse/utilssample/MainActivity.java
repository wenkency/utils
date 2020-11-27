package cn.carhouse.utilssample;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

import javax.crypto.Cipher;

import cn.carhouse.utils.DateUtils;
import cn.carhouse.utils.DensityUtils;
import cn.carhouse.utils.LG;
import cn.carhouse.utils.SPUtils;
import cn.carhouse.utils.TSUtils;
import cn.carhouse.utils.ThreadPoolUtils;
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


        new DownloadFilesTask().execute("key", "test");
    }

    private class DownloadFilesTask extends AsyncTask<String, Integer, Long> {
        public DownloadFilesTask() {
            super();
        }

        @Override
        protected void onPreExecute() {

        }

        protected Long doInBackground(String... urls) {
            int count = urls.length;
            long totalSize = 0;
            for (int i = 0; i < count; i++) {
                totalSize += 1;
                publishProgress((int) ((i / (float) count) * 100));
                // Escape early if cancel() is called
                if (isCancelled()) break;
            }
            return totalSize;
        }

        protected void onProgressUpdate(Integer... progress) {
            int pro = progress[0];
        }

        protected void onPostExecute(Long result) {

        }
    }


    public void test(View view) {
        TSUtils.show("我测试一下咯");
        int screenHeight = DensityUtils.getScreenHeight();
        int dp = DensityUtils.dp2px(10);
        String test = SPUtils.getString("key");
        StringBuffer sb = new StringBuffer("screenHeight:" + screenHeight
                + "\ndp:" + dp
                + "\nvalue:" + test
                + "\ngetTime:" + DateUtils.getTime(System.currentTimeMillis()) + "\n");
        String value = "刘付文LVen";
        String password = "asd123456789jkl123456789";
        String encode = AESUtil.aes(value, password, Cipher.ENCRYPT_MODE);
        sb.append("AES encode:" + encode + "\n");
        sb.append("AES decode:" + AESUtil.aes(encode, password, Cipher.DECRYPT_MODE) + "\n");

        sb.append("MD5:" + MessageDigestUtils.MD5(value) + ":" + MessageDigestUtils.MD5(value).length() + "\n");
        sb.append("SHA1:" + MessageDigestUtils.SHA1(value) + ":" + MessageDigestUtils.SHA1(value).length() + "\n");
        sb.append("SHA256:" + MessageDigestUtils.SHA256(value) + ":" + MessageDigestUtils.SHA256(value).length() + "\n");
        tv.setText(sb.toString());


        // ThreadPoolUtils.getInstance().execute(new TestTask(2));

        // thread();
        threadPoolTest();
    }

    private void thread() {
        LG.setDebug(true);
        // 1. 创建Callable
        Callable<String> callable = new Callable<String>() {
            @Override
            public String call() throws Exception {
                // 在这里做耗时操作
                LG.e("callable");
                SystemClock.sleep(200);
                return "callable";
            }
        };
        // 2. 创建FutureTask
        FutureTask<String> task = new FutureTask<String>(callable) {
            @Override
            protected void done() {
                try {
                    // 在这里可以拿到结果
                    LG.e("done:" + get());
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        };
        // 3. 开线程执行
        ThreadPoolUtils.getInstance().execute(task);
        // ThreadPoolUtils.getInstance().submit(task);

    }
    private void threadPoolTest(){
        for (int i = 0; i < 100; i++) {
            ThreadPoolUtils.getInstance().execute(new TestTask(i));
            SystemClock.sleep(20);
        }
    }
    class TestTask implements Runnable {
        private int task = 0;

        public TestTask(int task) {
            this.task = task;
        }

        @Override
        public void run() {
            SystemClock.sleep(150 );
            Log.e("TAG",
                    Thread.currentThread().getName() + ":"
                            + Thread.currentThread().getId()
                            + ":" + task);
        }
    }
}
