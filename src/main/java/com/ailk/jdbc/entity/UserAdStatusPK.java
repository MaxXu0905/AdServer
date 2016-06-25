package com.ailk.jdbc.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * 用户广告状态表主键
 * 
 * @author xugq
 * 
 */
@SuppressWarnings("serial")
@Embeddable
public class UserAdStatusPK implements Serializable, Cloneable {

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
		UserAdStatusPK other = (UserAdStatusPK) obj;
		if (adId != other.adId)
			return false;
		if (userId != other.userId)
			return false;
		return true;
	}

	@Override
	public UserAdStatusPK clone() {
		try {
			return (UserAdStatusPK) super.clone();
		} catch (CloneNotSupportedException e) {
			return null;
		}
	}
}
