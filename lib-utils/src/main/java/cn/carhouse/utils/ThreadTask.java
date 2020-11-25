package cn.carhouse.utils;

/**
 * 任务
 */
public abstract class ThreadTask implements Runnable {
    @Override
    public void run() {
        try {
            execute();
        } finally {
            // 从队列移除
            ThreadPoolUtils.getInstance().finishTask(this);
        }
    }

    public abstract void execute();
}
