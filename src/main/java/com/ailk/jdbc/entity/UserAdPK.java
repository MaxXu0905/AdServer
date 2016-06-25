package com.ailk.jdbc.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * 用户名与用户ID的映射表主键
 * 
 * @author xugq
 * 
 */
@SuppressWarnings("serial")
@Embeddable
public class UserAdPK implements Serializable, Cloneable {

	@Column(name = "user_id", nullable = false)
	private long userId;

	@Column(name = "ad_style", nullable = false)
	private short adStyle;

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public short getAdStyle() {
		return adStyle;
	}

	public void setAdStyle(short adStyle) {
		this.adStyle = adStyle;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + adStyle;
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
		UserAdPK other = (UserAdPK) obj;
		if (adStyle != other.adStyle)
			return false;
		if (userId != other.userId)
			return false;
		return true;
	}

	@Override
	public UserAdPK clone() {
		try {
			return (UserAdPK) super.clone();
		} catch (CloneNotSupportedException e) {
			return null;
		}
	}
}
