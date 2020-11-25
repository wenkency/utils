package cn.carhouse.utils;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/*
 *  @文件名:   ThreadPoolUtils
 *  @描述：    线程管理类，管理并发执行的任务。访OkHttp Dispatcher写的
 */
public class ThreadPoolUtils {
    // 线程池
    private ExecutorService executorService;
    // 准备执行的任务
    private final Deque<ThreadTask> readyTasks = new ArrayDeque<>();
    // 正在执行的任务
    private final Deque<ThreadTask> runningTasks = new ArrayDeque<>();
    // 队列默认并发任务个数最大值为10
    private static int MAX_TASK = 10;

    private static volatile ThreadPoolUtils instance;

    private ThreadPoolUtils() {
    }

    public static ThreadPoolUtils getInstance() {
        if (instance == null) {
            synchronized (ThreadPoolUtils.class) {
                if (instance == null) {
                    instance = new ThreadPoolUtils();
                }
            }
        }
        return instance;
    }

    private ExecutorService executorService() {
        if (executorService == null) {
            synchronized (this) {
                if (executorService == null) {
                    executorService = new ThreadPoolExecutor(
                            0,
                            Integer.MAX_VALUE,
                            60,
                            TimeUnit.SECONDS,
                            // 1、直接提交队列：设置为SynchronousQueue队列，SynchronousQueue是一个特殊的BlockingQueue，
                            // 它没有容量，没执行一个插入操作就会阻塞，需要再执行一个删除操作才会被唤醒，
                            // 反之每一个删除操作也都要等待对应的插入操作
                            new SynchronousQueue<Runnable>(),
                            threadFactory()
                    );
                }
            }
        }
        return executorService;
    }

    public void execute(ThreadTask task) {
        synchronized (this) {
            // 添加到准备的任务里面
            readyTasks.add(task);
        }

        // 去执行
        promoteAndExecute();
    }

    private void promoteAndExecute() {
        // 如果没有准备的任务或者执行的任务大于任务数量
        synchronized (this) {
            if (readyTasks.size() <= 0 || runningTasks.size() >= MAX_TASK) {
                return;
            }
        }
        // 创建一个List
        List<ThreadTask> executableTasks = new ArrayList<>();
        synchronized (this) {
            for (ThreadTask readyTask : readyTasks) {
                // 如果任务数大于最大数量
                if (runningTasks.size() >= MAX_TASK) {
                    break;
                }
                // 移除
                readyTasks.remove(readyTask);
                // 添加到执行的任务集合
                runningTasks.add(readyTask);
                executableTasks.add(readyTask);
            }
        }

        for (Runnable executableCall : executableTasks) {
            executeOn(executableCall);
        }
    }

    private void executeOn(Runnable task) {
        boolean success = false;
        try {
            // 开线程后，立马到finally
            executorService().execute(task);
            success = true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (!success) {
                finishTask(task);
            }
        }

    }

    public void finishTask(Runnable task) {
        synchronized (this) {
            runningTasks.remove(task);
        }
        promoteAndExecute();
    }

    private final ThreadFactory threadFactory() {
        return new ThreadFactory() {
            @Override
            public Thread newThread(Runnable runnable) {
                Thread result = new Thread(runnable, "ThreadPoolUtils");
                result.setDaemon(false);
                return result;
            }
        };
    }

    /**
     * 这个比较少用，直接执行
     */
    public Future<?> submit(Runnable task) {
        return executorService().submit(task);
    }

    /**
     * 设置任务并发最大数量，如果要改变设置调用一次即可
     *
     * @param maxTask
     */
    public static void setMaxTask(int maxTask) {
        MAX_TASK = maxTask;
    }

}
