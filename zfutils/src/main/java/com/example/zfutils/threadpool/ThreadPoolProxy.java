package com.example.zfutils.threadpool;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * author: zhufu
 * email : zhufui@sina.com
 * time  : 2018/03/29
 * desc  : 线程池代理类
 * version : 1.0
 */

public class ThreadPoolProxy {
    ThreadPoolExecutor mExecutor;
    private int mCorePoolSize;
    private int mMaximumPoolSize;
    private long mKeepAliveTime = -1;

    /**
     * @param corePoolSize    核心池的大小
     * @param maximumPoolSize 最大线程数
     */
    public ThreadPoolProxy(int corePoolSize, int maximumPoolSize) {
        mCorePoolSize = corePoolSize;
        mMaximumPoolSize = maximumPoolSize;
    }

    /**
     * @param corePoolSize    核心池的大小
     * @param maximumPoolSize 最大线程数
     * @param keepAliveTime   保持存活时间,单位是秒,默认是30秒
     */
    public ThreadPoolProxy(int corePoolSize, int maximumPoolSize, long keepAliveTime) {
        mCorePoolSize = corePoolSize;
        mMaximumPoolSize = maximumPoolSize;
        mKeepAliveTime = keepAliveTime;
    }

    /**
     * 初始化ThreadPoolExecutor
     * 双重检查加锁,只有在第一次实例化的时候才启用同步机制,提高了性能
     */
    private void initThreadPoolExecutor() {
        if (mExecutor == null || mExecutor.isShutdown() || mExecutor.isTerminated()) {
            synchronized (ThreadPoolProxy.class) {
                if (mExecutor == null || mExecutor.isShutdown() || mExecutor.isTerminated()) {
                    //线程存活时间
                    long keepAliveTime = mKeepAliveTime == -1 ? 30 : mKeepAliveTime;
                    TimeUnit unit = TimeUnit.SECONDS;
                    BlockingQueue<Runnable> workQueue = new LinkedBlockingDeque<>();

                    ThreadFactory threadFactory = new ThreadFactory() {
                        //原子整数，可以在超高并发下正常工作
                        private final AtomicInteger mCount = new AtomicInteger(1);

                        public Thread newThread(Runnable r) {
                            return new Thread(r, "ThreadPool#" + mCount.getAndIncrement());
                        }
                    };
//                    ThreadFactory threadFactory = Executors.defaultThreadFactory();

                    //DiscardPolicy,后续任务连异常都不会抛出，直接忽略掉
                    //DiscardOldestPolicy,把队列里面最旧的提走，然后新的任务顶上去
                    //CallerRunsPolicy,会直接就在当前线程运行了，不会再和队列里面的任务一起等待，如果在主线程执行，这是相当危险的，一般不用这个
                    //AbortPolicy,如果正在执行执行任务的总数量超过最大线程数量加上等待队列长度的时候，后面再进来的任务将不会被执行，会抛出异常让用户去做处理
                    RejectedExecutionHandler handler = new ThreadPoolExecutor.DiscardPolicy();

                    mExecutor = new ThreadPoolExecutor(mCorePoolSize, mMaximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
                }
            }
        }
    }

    /**
     执行任务和提交任务的区别?
     1.有无返回值
     execute->没有返回值
     submit-->有返回值
     2.Future的具体作用?
     1.有方法可以接收一个任务执行完成之后的结果,其实就是get方法,get方法是一个阻塞方法
     2.get方法的签名抛出了异常===>可以处理任务执行过程中可能遇到的异常
     */
    /**
     * 执行任务
     */
    public void execute(Runnable task) {
        initThreadPoolExecutor();
        mExecutor.execute(task);
    }

    /**
     * 提交任务
     */
    public Future<?> submit(Runnable task) {
        initThreadPoolExecutor();
        return mExecutor.submit(task);
    }

    /**
     * 移除任务
     */
    public void remove(Runnable task) {
        initThreadPoolExecutor();
        mExecutor.remove(task);
    }
}
