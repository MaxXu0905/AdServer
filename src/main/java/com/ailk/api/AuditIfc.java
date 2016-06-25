package com.ailk.api;

public interface AuditIfc {

	/**
	 * 保存从手机客户端发送过来的日志信息
	 * 
	 * @param userId
	 *            用户ID
	 * @param data
	 *            日志信息
	 * @return 是否成功
	 */
	public void syslog(long userId, String data);

	/**
	 * 记录重要的信息，用于数据稽核
	 * 
	 * @param userId
	 *            用户ID
	 * @param data
	 *            数据
	 */
	public void audit(long userId, String data);

}
