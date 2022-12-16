package com.lazy.agent.ipinfo.server;

import java.util.concurrent.*;

/**
 * <p>
 *
 * </p>
 *
 * @author lzy
 * @since 2022/12/15.
 */
public class ThreadPoolHolder {

    //核心数
    public static final int nThreads = Runtime.getRuntime().availableProcessors();
    //主线程池
    public static final ExecutorService bootstrapExecutor = Executors.newFixedThreadPool(2);
    //任务线程池
    public static final ExecutorService taskExecutor = new ThreadPoolExecutor(
            nThreads, nThreads, 0L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<>(100),
            new ThreadPoolExecutor.DiscardPolicy());
}
