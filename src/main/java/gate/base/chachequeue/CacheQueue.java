package gate.base.chachequeue;


import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.LinkedBlockingQueue;

import gate.base.domain.ChannelData;
import io.netty.channel.Channel;
/**
 * 数据流转容器
 */
public class CacheQueue {
	static volatile int index = 0; // 当前缓存队列的索引
	/**
	 * 记录集中器对应的连接次数
	 */
	public static ConcurrentHashMap<String, Integer> ipCountRelationCache ;// 集中器ip和连接次数的对应关系
	/**
	 * 缓存前置channel
	 * 网关连接到前置之后 将对应的channel缓存起来 通过ip获取
	 */
	private static ConcurrentHashMap<String, Channel> masterChannelCache ;
	/**
	 * 轮询策略
	 */
	private static CopyOnWriteArrayList<Channel> roundCache ;// 缓存前置channel
	
	/**
	 * Server4Terminel接收到消息之后 将消息存放到up2MasterQueue队列中
	 */
	public static BlockingQueue<ChannelData> up2MasterQueue;//<ChannelData>
	// BlockingQueue表示一个阻塞队列，它支持两个附加操作：插入和移除。
	// 插入操作：如果当队列满时调用，它会阻塞直到队列不满。
	// 移除操作：如果当队列为空时调用，它会阻塞直到队列不空。
	
	public static BlockingQueue<Object> down2TmnlQueue;//<ChannelData>
	static{
		
		ipCountRelationCache = new ConcurrentHashMap<String, Integer>();// 集中器ip和连接次数的对应关系
		masterChannelCache = new ConcurrentHashMap<String, Channel>();// 缓存前置channel
		roundCache = new CopyOnWriteArrayList<Channel>();// 轮询策略
		up2MasterQueue = new LinkedBlockingQueue<ChannelData>();//<ChannelData>
		down2TmnlQueue = new LinkedBlockingQueue<Object>();//<ChannelData>
		
		System.out.println("GATE 初始化CacheQueue完成......");
		
	}
	
	
	
	/**
	 * 获取缓存中的master的channel对象
	 * @return
	 */
	public static Channel choiceMasterChannel(){// 轮询策略
		//TODO 轮寻策略
		int masterNum = CacheQueue.masterChannelCache.size();// 获取前置连接数量
		if(masterNum > 0){// 判断前置连接数量是否大于0
			int nextIndex = (index + 1) % masterNum;// 获取下一个连接序号
			index = nextIndex;// 更新当前连接序号
			return roundCache.get(nextIndex);// 获取下一个连接
		}
		return null;
	}
	/**
	 * 清空  终端连接序号缓存
	 */
	public static void clearIpCountRelationCache(){// 集中器ip和连接次数的对应关系
		ipCountRelationCache.clear();// 清除缓存
		System.out.println("GATE 清除缓存......");
	}
	/**
	 * 清空 前置连接以及前置会话策略缓存
	 */
	public static void clearMasterChannelCache(){// 缓存前置channel
		masterChannelCache.clear();// 清除缓存
		roundCache.clear();// 清除轮询策略缓存
	}
	
	public static void addMasterChannel2LocalCache(String key ,Channel channel ){// 缓存前置channel
		masterChannelCache.put(key, channel);// 缓存前置channel
		roundCache.add(channel);// 缓存前置channel
		System.out.println("GATE 缓存前置channel......");
		
	}
	public static void removeMasterChannelFromLocalCache(String key ){
		Channel removedChannel = masterChannelCache.remove(key);// 清除缓存
		roundCache.remove(removedChannel);// 清除缓存
		System.out.println("GATE 清除缓存......");
	}
}
