package gate.concurrent;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
/**
 * 基本线程池
 */
public class BasicThreadPoolTaskExecutor {
	private static ExecutorService  service=null;//线程池

	/**
	 *  私有构造器
	 */
	private BasicThreadPoolTaskExecutor(){
		throw new AssertionError();//防止被实例化
	}

	/**
	 *  静态代码块
	 */
	static{
		service = Executors.newCachedThreadPool(new ThreadFactoryImpl("basicExecutor_", false));
	}

	/**
	 *  获取线程池
	 * @return
	 */
	public static ExecutorService getBasicExecutor(){
		return service;
	}
	
}
