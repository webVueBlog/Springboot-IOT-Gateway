package gate.base.cache;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import gate.concurrent.ThreadFactoryImpl;
import gate.server.Server4Terminal;

/**
 * 规约相关缓存
 */
public class ProtocalStrategyCache {
	private static final ScheduledExecutorService scheduledExecutor = Executors.newSingleThreadScheduledExecutor(
			new ThreadFactoryImpl("persistent_ProtocalStrategy_", true));// 定时任务线程池
	private ProtocalStrategyCache(){// 防止外部实例化
		throw new AssertionError();// 抛出断言异常
	}
	public static ConcurrentHashMap<String, String> protocalStrategyCache ;// protocalStrategyCache作用是
	// 缓存protocalStrategy的className和classUrl
	public static ConcurrentHashMap<String, Server4Terminal> protocalServerCache ;// 网关获取终端报文
	public static ConcurrentHashMap<String, String> protocalStrategyClassUrlCache ;
	
	static{
		protocalStrategyCache = new ConcurrentHashMap<>();
		protocalServerCache = new ConcurrentHashMap<>();
		protocalStrategyClassUrlCache = new ConcurrentHashMap<>();// 缓存protocalStrategy的className和classUrl
	}
	
	/**
	 * 规约规则落地
	 */
	private static void persistentProtocalStrategy(){// 定时任务，每隔3分钟执行一次
		System.out.println("定时任务执行了");
		// TODO 规约规则落地
		// 定时任务
		// 1. 获取当前时间
		// 2. 获取当前时间所在的小时数
		// 3. 获取当前时间所在的小时数对应的规约规则
		// 4. 获取当前时间所在的小时数对应的规约规则的className
		// 5. 获取当前时间所在的小时数对应的规约规则的classUrl
		// 6. 判断className是否为空，如果为空，则不执行任何操作，如果className不为空
		scheduledExecutor.scheduleAtFixedRate(new Runnable() {// 定时任务
			// scheduleAtFixedRate作用是
			// 1. 每隔3分钟执行一次
			
			@Override
			public void run() {
				// TODO 规约规则落地
				// 1. 获取当前时间
				
			}
		}, 2, 3, TimeUnit.MINUTES);// 每隔3分钟执行一次
	}
	
}
