package cn.itcast.googleplay10.manager;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by zhengping on 2016/12/8,9:10.
 * 线程池的管理类，单例
 */

public class MyThreadPoolManager {

    private MyThreadPoolManager(){}

    private static MyThreadPoolManager instance ;

    public synchronized  static MyThreadPoolManager getInstance() {
        if(instance == null) {
            instance = new MyThreadPoolManager();
        }
        return instance;
    }

    //线程池  线程池的使用场景：用一个线程池来管理一个模块，比如线程池管理下载的模块
    //如果使用线程池来管理这个程序中所有的子线程，有可能会造成最基本的功能（网络数据的加载）都使用不了了
    private ThreadPoolExecutor executor;

    //线程池中存放的是线程，我们对于线程池需要做什么操作呢？
    //我们所需要做的事情就是往线程池里面丢任务
    //Thread
    //Runnable
    public void execute(Runnable r){
        if(executor == null) {
            /**
             int corePoolSize,核心线程的数量，在正常情况下，线程池中同时运行的线程的数量
             int maximumPoolSize,最大线程的数量，在非正常的情况下（等待区域满了的情况下），线程池中同时运行的线程的数量
             long keepAliveTime,空闲时间  5
             TimeUnit unit,空闲时间的单位
             BlockingQueue<Runnable> workQueue,等待区域
             ThreadFactory threadFactory,线程创建的工厂
             RejectedExecutionHandler handler 异常处理机制
             */
            executor = new ThreadPoolExecutor(
                    3,5,0,
                    TimeUnit.SECONDS,
                    new ArrayBlockingQueue<Runnable>(20),
                    Executors.defaultThreadFactory(),
                    new ThreadPoolExecutor.AbortPolicy());
            int cpuCount = Runtime.getRuntime().availableProcessors();
            int corePoolSize = cpuCount*2 + 1;

        }
        //把任务丢到线程池里面去
        executor.execute(r);

    }

    public void cancle(Runnable runnable) {
        if(executor != null) {
            executor.getQueue().remove(runnable);
        }
    }
}
