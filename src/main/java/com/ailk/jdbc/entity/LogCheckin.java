package com.ailk.jdbc.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;

/**
 * Ç©µ½ÈÕÖ¾
 * 
 * @author xugq
 * 
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "log_checkin")
public class LogCheckin implements Serializable, Cloneable {

	@Id
	@Column(name = "user_id")
	private long userId;

	@Column(name = "lastUpdate", nullable = false)
	private long lastUpdate;

	@Version
	@Column(name = "version")
	private int version;

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public long getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(long lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (lastUpdate ^ (lastUpdate >>> 32));
		result = prime * result + (int) (userId ^ (userId >>> 32));
		result = prime * result + version;
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
		LogCheckin other = (LogCheckin) obj;
		if (lastUpdate != other.lastUpdate)
			return false;
		if (userId != other.userId)
			return false;
		if (version != other.version)
			return false;
		return true;
	}

	@Override
	public InfoUser clone() {
		try {
			return (InfoUser) super.clone();
		} catch (CloneNotSupportedException e) {
			return null;
		}
	}

}