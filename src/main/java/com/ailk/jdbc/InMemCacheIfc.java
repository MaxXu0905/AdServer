package com.ailk.jdbc;

/**
 * 私有内存缓存接口，需要自动加载到私有内存的类需要实现该接口
 * 
 * @author xugq
 * 
 */
public interface InMemCacheIfc {

	/**
	 * 加载数据到缓存
	 */
	public void load();

}
