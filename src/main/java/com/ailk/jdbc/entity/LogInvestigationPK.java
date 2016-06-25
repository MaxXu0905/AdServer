package com.ailk.jdbc.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@SuppressWarnings("serial")
@Embeddable
public class LogInvestigationPK implements Serializable, Cloneable {
	@Column(name = "user_id", nullable = false)
	private long userId;

	@Column(name = "ad_id", nullable = false)
	private int adId;

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public int getAdId() {
		return adId;
	}

	public void setAdId(int adId) {
		this.adId = adId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + adId;
		result = prime * result + (int) (userId ^ (userId >>> 32));
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
		LogInvestigationPK other = (LogInvestigationPK) obj;
		if (adId != other.adId)
			return false;
		if (userId != other.userId)
			return false;
		return true;
	}

	@Override
	public LogInvestigationPK clone() {
		try {
			return (LogInvestigationPK) super.clone();
		} catch (CloneNotSupportedException e) {
			return null;
		}
	}
}
