package gate.concurrent;


import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;
/**
 * 自定义的ThreadFactory实现类
 */
public class ThreadFactoryImpl implements ThreadFactory{
	AtomicInteger index = new AtomicInteger(0);//原子操作类
	private String threadNamePrefix;//线程名称前缀
	private boolean isDaemon;//是否为守护线程
	
	public ThreadFactoryImpl(String threadNamePrefix,boolean isDaemon) {
		super();
		this.threadNamePrefix = threadNamePrefix;//线程名称前缀
		this.isDaemon = isDaemon;
	}
	
	
	@Override
	public Thread newThread(Runnable r) {//创建新线程
		
		Thread thread =  new Thread(r,threadNamePrefix+index.addAndGet(1));//创建新线程
		thread.setDaemon(isDaemon);//设置为守护线程
		return thread;
		
	}
	
	

}
