package com.ailk.api.impl;

public class BaseRequest {

	private short osType; // �ͻ��˲���ϵͳ����
	private int version; // �ͻ��˰汾��
	private long timestamp; // �ͻ���ʱ���

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
