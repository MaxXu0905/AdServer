package com.ailk.jdbc.cache;

import com.ailk.jdbc.entity.InfoPeriod;

public class InfoPeriodValue {
	
	private int periodId;
	private String desc;
	private long beginTime;
	private long endTime;
	private short periodType;
	
	public void set(InfoPeriod entity) {
		periodId = entity.getPeriodId();
		desc = entity.getDesc();
		beginTime = entity.getBeginTime().getTime();
		endTime = entity.getEndTime().getTime();
		periodType = entity.getPeriodType();
	}

	public int getPeriodId() {
		return periodId;
	}

	public void setPeriodId(int periodId) {
		this.periodId = periodId;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public long getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(long beginTime) {
		this.beginTime = beginTime;
	}

	public long getEndTime() {
		return endTime;
	}

	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}

	public short getPeriodType() {
		return periodType;
	}

	public void setPeriodType(short periodType) {
		this.periodType = periodType;
	}

}
