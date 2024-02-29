package gate.base.domain;
/**
 * 本地缓存统一接口
 * @Description: 
 */
public interface LocalCache {
	
	Object get(Object key);//获取缓存

	void set(Object key ,Object value);//设置缓存
	
	boolean del(Object key);//删除缓存
}
