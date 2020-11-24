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
 *  @文件名:   ThreadUtils
 *  @创建者:   Administrator
 *  @创建时间:  2015/11/23 11:27
 *  @描述：    线程管理类，管理线程池，一个应用中有多个线程池，每个线程池做自己相关的业务
 */
public class ThreadPoolUtils {
    private ThreadPoolExecutor executorService;
    // 准备执行的任务
    private final Deque<Runnable> readyTasks = new ArrayDeque<>();
    // 正在执行的任务
    private final Deque<Runnable> runningTasks = new ArrayDeque<>();
    // 队列默认任务个数为10
    private static int MAX_TASK = 10;
    private static ThreadPoolUtils instance;

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

    private ThreadPoolExecutor executorService() {
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

    public synchronized void execute(Runnable task) {
        // 添加到准备的任务里面
        readyTasks.add(task);
        // 去执行
        promoteAndExecute();
    }

    private void promoteAndExecute() {
        // 如果没有准备的任务或者执行的任务大于任务数量
        if (readyTasks.size() <= 0 || runningTasks.size() >= MAX_TASK) {
            return;
        }
        // 创建一个List
        List<Runnable> executableTasks = new ArrayList<>();
        for (Runnable readyTask : readyTasks) {
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
        for (Runnable executableCall : executableTasks) {
            executeOn(executableCall);
        }
    }

    private void executeOn(Runnable task) {
        try {
            executorService().execute(task);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            finishTask(task);
        }

    }

    private synchronized void finishTask(Runnable task) {
        runningTasks.remove(task);
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


    public Future<?> submit(Runnable task) {
        return executorService().submit(task);
    }

    public void remove(Runnable task) {
        if (!executorService().isShutdown()) {
            executorService().getQueue().remove(task);
        }
    }

    public void clear() {
        executorService().shutdownNow();
        executorService().getQueue().clear();
    }

    public void setMaxTask(int maxTask) {
        MAX_TASK = maxTask;
    }
}
