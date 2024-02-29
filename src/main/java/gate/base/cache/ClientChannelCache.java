package gate.base.cache;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import io.netty.channel.Channel;

/**
 * 该缓存类 用于缓存所有连接到网关的终端“ip:channel”信息,并提供了访问缓存的方法
 */
public class ClientChannelCache {
	private static  List<ConcurrentHashMap<String, Channel>> cacheList = new ArrayList<ConcurrentHashMap<String,Channel>>();
	//存储终端 channel 的缓存
	// channel是netty的channel对象
	// key是终端ip:channelId
	// value是netty的channel对象
	private static  int scale = 10;//缓存大小
	
	static{
		initClientChannelCache();//初始化缓存
		System.out.println("GATE 初始化ClientChannel缓存完成......");
	}
	/**
	 * 初始化存储终端channel的缓存
	 */
	public static void initClientChannelCache(){//初始化缓存
		for(int i = 0 ; i < scale ; i++){//初始化缓存
			cacheList.add(new ConcurrentHashMap<String, Channel>());//初始化缓存
		}
	}
	/**
	 * 根据key  获取缓存数据的ConcurrentHashMap实体
	 * @param key
	 * @return
	 */
	private static  ConcurrentHashMap<String, Channel> getCacheInstance(String key){//根据key  获取缓存数据的ConcurrentHashMap实体
		int hashCode = key.hashCode();//获取key的hashCode值
		//System.out.println("hashCode:"+hashCode);
		hashCode = (hashCode < 0) ? -hashCode : hashCode;//取正
		//System.out.println("hashCode:"+hashCode);
		int index = hashCode % scale;//获取缓存索引
		//System.out.println("index:"+index);
		return cacheList.get(index);//根据索引获取缓存
	}
	/**
	 * 插入数据
	 * @param key
	 * @param value
	 */
	public static  void set(String key , Channel value){//插入数据
		
		Channel chanel= getCacheInstance(key).putIfAbsent(key, value);//如果key不存在，则插入数据
		if(chanel == null){
			System.out.println("插入数据成功......");
		}else{
			System.out.println("插入数据失败......");
		}
	}
	/**
	 * 获取数据
	 * @param key
	 */
	public static Channel get(String key){//获取数据
		System.out.println("获取数据......");
		//System.out.println("key:"+key);
		//System.out.println("cacheList:"+cacheList);
		//System.out.println("cacheList.size():"+cacheList.size());
		//System.out.println("getCacheInstance(key):"+getCacheInstance(key));
		
		return getCacheInstance(key).get(key);//根据key获取数据
		
	}
	
	public static void removeOne(String key){//删除数据
		System.out.println("删除数据......");
		System.out.println("key:"+key);
		System.out.println("cacheList:"+cacheList);
		System.out.println("cacheList.size():"+cacheList.size());
		System.out.println("getCacheInstance(key):"+getCacheInstance(key));
		
		getCacheInstance(key).remove(key);//根据key删除数据
	}
	/**
	 * 清空所有缓存所有数据
	 */
	public static void clearAll(){//清空所有缓存所有数据
		System.out.println("清空所有缓存所有数据......");
		System.out.println("cacheList:"+cacheList);
		System.out.println("cacheList.size():"+cacheList.size());
		for (ConcurrentHashMap<String, Channel> concurrentHashMap : cacheList) {//遍历缓存列表
			concurrentHashMap.clear();//清空缓存
		}
	}
	private static void logOut(String msg){//打印缓存列表
		System.out.println("打印缓存列表......");
		System.out.println(msg);
		for(int i = 0 ; i <cacheList.size() ; i ++){//遍历缓存列表
			System.out.println("缓存监控.............ClientChannelMap"+i+"=="+cacheList.get(i).size());
		}
	}
}
