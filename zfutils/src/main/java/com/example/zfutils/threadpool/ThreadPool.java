package com.example.zfutils.threadpool;

/**
 * author: zhufu
 * email : zhufui@sina.com
 * time  : 2018/03/29
 * desc  :
 * version : 1.0
 */
public class ThreadPool {
    private static ThreadPoolProxy mNormalThreadPool;

    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
    private static final int CORE_POOL_SIZE = Math.max(2, Math.min(CPU_COUNT - 1, 4));
    private static final int MAXIMUM_POOL_SIZE = CPU_COUNT * 2 + 1;

    /**
     * 得到普通线程池代理对象mNormalThreadPool
     */
    public static ThreadPoolProxy getNormalThreadPool() {
        if (mNormalThreadPool == null) {
            synchronized (ThreadPool.class) {
                if (mNormalThreadPool == null) {
                    mNormalThreadPool = new ThreadPoolProxy(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE);
                }
            }
        }
        return mNormalThreadPool;
    }

    /**
     * 执行任务
     *
     * @param task
     */
    public static void execute(Runnable task) {
        getNormalThreadPool().execute(task);
    }
}
