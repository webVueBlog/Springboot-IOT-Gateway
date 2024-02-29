package gate.base.cache;

import gate.base.domain.LocalCache;
/**
 * 缓存网关与前置连接的会话
 */
public class Cli2MasterLocalCache implements LocalCache{//作用是缓存网关与前置连接的会话

	private Cli2MasterLocalCache(){//禁止创建gate.base.cache.Cli2MasterLocalCache对象
		if(inner.cli2MasterLocalCache != null){
			throw new IllegalStateException("禁止创建gate.base.cache.Cli2MasterLocalCache对象！");
		}
	}
	
	
	static class inner{//禁止创建gate.base.cache.Cli2MasterLocalCache对象的内部类
		//Cli2MasterLocalCache作用是缓存网关与前置连接的会话
		static Cli2MasterLocalCache cli2MasterLocalCache = new Cli2MasterLocalCache();
		
	}
	

	
	public static Cli2MasterLocalCache getInstance(){//获取Cli2MasterLocalCache的实例
		return Cli2MasterLocalCache.inner.cli2MasterLocalCache;//返回inner的cli2MasterLocalCache属性
	}



	@Override
	public Object get(Object key) {//获取缓存中的数据
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	public void set(Object key, Object value) {//设置缓存中的数据
		// TODO Auto-generated method stub
	}



	@Override
	public boolean del(Object key) {//删除缓存中的数据
		// TODO Auto-generated method stub
		return false;
	}

	
	

}
