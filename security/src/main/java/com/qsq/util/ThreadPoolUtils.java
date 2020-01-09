package com.qsq.util;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author QSQ
 * @create 2019/4/12 13:46
 * No, again
 * 〈 线程池 〉
 */
public class ThreadPoolUtils {
    private static final ThreadPoolExecutor POOL = create(10);

    /**
     * @param size
     * @return
     */
    public static ThreadPoolExecutor create(int size) {
        return new ThreadPoolExecutor(size, size * 2,
                60L, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(1000),
                new ThreadPoolExecutor.CallerRunsPolicy());
    }

    public static void execute(Runnable runnable) {
        POOL.execute(runnable);
    }
}
