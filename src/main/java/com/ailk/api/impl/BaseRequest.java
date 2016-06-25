package com.ailk.api.impl;

public class BaseRequest {

	private short osType; // 客户端操作系统类型
	private int version; // 客户端版本号
	private long timestamp; // 客户端时间戳

	public short getOsType() {
		return osType;
	}

	public void setOsType(short osType) {
		this.osType = osType;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

}
