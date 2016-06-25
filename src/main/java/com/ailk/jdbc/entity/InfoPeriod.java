package com.ailk.jdbc.entity;

import java.io.Serializable;
import java.sql.Time;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@SuppressWarnings("serial")
@Entity
@Table(name = "info_period")
public class InfoPeriod implements Serializable, Cloneable {

	@Id
	@Column(name = "period_id")
	private int periodId;

	@Column(name = "description", nullable = false)
	private String desc;

	@Column(name = "begin_time", nullable = false)
	private Time beginTime;

	@Column(name = "end_time", nullable = false)
	private Time endTime;

	@Column(name = "period_type", nullable = false)
	private short periodType;

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

	public Time getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(Time beginTime) {
		this.beginTime = beginTime;
	}

	public Time getEndTime() {
		return endTime;
	}

	public void setEndTime(Time endTime) {
		this.endTime = endTime;
	}

	public short getPeriodType() {
		return periodType;
	}

	public void setPeriodType(short periodType) {
		this.periodType = periodType;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((beginTime == null) ? 0 : beginTime.hashCode());
		result = prime * result + ((desc == null) ? 0 : desc.hashCode());
		result = prime * result + ((endTime == null) ? 0 : endTime.hashCode());
		result = prime * result + periodId;
		result = prime * result + periodType;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		InfoPeriod other = (InfoPeriod) obj;
		if (beginTime == null) {
			if (other.beginTime != null)
				return false;
		} else if (!beginTime.equals(other.beginTime))
			return false;
		if (desc == null) {
			if (other.desc != null)
				return false;
		} else if (!desc.equals(other.desc))
			return false;
		if (endTime == null) {
			if (other.endTime != null)
				return false;
		} else if (!endTime.equals(other.endTime))
			return false;
		if (periodId != other.periodId)
			return false;
		if (periodType != other.periodType)
			return false;
		return true;
	}

	@Override
	public InfoPeriod clone() {
		try {
			return (InfoPeriod) super.clone();
		} catch (CloneNotSupportedException e) {
			return null;
		}
	}

}
