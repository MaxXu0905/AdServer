package com.ailk.jdbc.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@SuppressWarnings("serial")
@Embeddable
public class InfoUserExtPK implements Serializable, Cloneable {
	@Column(name = "user_id")
	private long userId;

	@Column(name = "attr_id")
	private short attrId;

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public short getAttrId() {
		return attrId;
	}

	public void setAttrId(short attrId) {
		this.attrId = attrId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + attrId;
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
		InfoUserExtPK other = (InfoUserExtPK) obj;
		if (attrId != other.attrId)
			return false;
		if (userId != other.userId)
			return false;
		return true;
	}

	@Override
	public InfoUserExtPK clone() {
		try {
			return (InfoUserExtPK) super.clone();
		} catch (CloneNotSupportedException e) {
			return null;
		}
	}
}
