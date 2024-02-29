package test;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
/**
 * 测试使用的一些计数的变量--要合理考虑并发
 */
public class CountHelper {
	public static int ThreadNum = 1 ;//线程数
	
	public static AtomicLong startTimeLong = new AtomicLong();//开始时间
	public static AtomicLong endTimeLong = new AtomicLong();//结束时间
	public static AtomicInteger clientRecieveCount ;//客户端接收到的消息数量
	public static AtomicInteger masterRecieveCount ;//master接收到的消息数量
	
	public static Long masterRecieveStartTime;//master开始接收消息的时间
	
	static{
		clientRecieveCount = new AtomicInteger(0);//客户端接收到的消息数量
		masterRecieveCount = new AtomicInteger(0);//master接收到的消息数量
	}

}
