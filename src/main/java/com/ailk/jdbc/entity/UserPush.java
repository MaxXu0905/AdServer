package com.ailk.jdbc.entity;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 用户推送账号表
 * 
 * @author xugq
 * 
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "user_push")
public class UserPush implements Serializable, Cloneable {

	@Id
	@Column(name = "dev_id", nullable = false)
	private String devId;

	@Column(name = "bd_user_id")
	private String bdUserId;

	@Column(name = "last_update")
	private Timestamp lastUpdate;

	public String getDevId() {
		return devId;
	}

	public void setDevId(String devId) {
		this.devId = devId;
	}

	public String getBdUserId() {
		return bdUserId;
	}

	public void setBdUserId(String bdUserId) {
		this.bdUserId = bdUserId;
	}

	public Timestamp getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(Timestamp lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((bdUserId == null) ? 0 : bdUserId.hashCode());
		result = prime * result + ((devId == null) ? 0 : devId.hashCode());
		result = prime * result + ((lastUpdate == null) ? 0 : lastUpdate.hashCode());
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
		UserPush other = (UserPush) obj;
		if (bdUserId == null) {
			if (other.bdUserId != null)
				return false;
		} else if (!bdUserId.equals(other.bdUserId))
			return false;
		if (devId == null) {
			if (other.devId != null)
				return false;
		} else if (!devId.equals(other.devId))
			return false;
		if (lastUpdate == null) {
			if (other.lastUpdate != null)
				return false;
		} else if (!lastUpdate.equals(other.lastUpdate))
			return false;
		return true;
	}

	public UserPush clone() {
		try {
			return (UserPush) super.clone();
		} catch (CloneNotSupportedException e) {
			return null;
		}
	}

}
