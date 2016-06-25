package com.ailk.jdbc;

/**
 * 修改表接口，包括创建索引、修改序列号等，需要修改表结构的类需要实现该接口
 * @author xugq
 *
 */
public interface AlterTableIfc {

	/**
	 * 修改表结构
	 */
	public void alterTable();
	
}
