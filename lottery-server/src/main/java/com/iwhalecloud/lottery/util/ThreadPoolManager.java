package com.iwhalecloud.lottery.util;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * <p>Title: ThreadPoolManager</p>
 * <p>Company:浩鲸云计算科技股份有限公司 </p>
 * <p>Description:
 * 描述：
 * </p>
 *
 * @author jinpu.shi
 * @version v1.0.0
 * @since 2020-11-22 13:59
 */
public final class ThreadPoolManager {

    private static ThreadPoolManager instance = new ThreadPoolManager();
    private ThreadPoolExecutor executor;

    private ThreadPoolManager() {
        int corePoolSize = Runtime.getRuntime().availableProcessors() * 2 + 1;
        long keepAliveTime = 10;
        TimeUnit unit = TimeUnit.MINUTES;
        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder().setNameFormat("thread-pool-%d").build();
        executor = new ThreadPoolExecutor(corePoolSize, corePoolSize, keepAliveTime, unit,
                new LinkedBlockingQueue<>(), namedThreadFactory, new ThreadPoolExecutor.AbortPolicy());
        executor.allowCoreThreadTimeOut(true);
    }

    public static synchronized ThreadPoolManager getInstance() {
        if (instance == null) {
            instance = new ThreadPoolManager();
        }
        return instance;
    }

    /**
     * 执行任务
     */
    public void execute(Runnable runnable) {
        if (runnable == null) {
            return;
        }
        executor.execute(runnable);
    }

    public <T> Future<T> submit(Callable<T> task) {
        if (task == null) {
            return null;
        }
        return executor.submit(task);
    }

}
