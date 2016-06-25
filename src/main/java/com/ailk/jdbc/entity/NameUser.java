package com.ailk.jdbc.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 用户信息表
 * 
 * @author xugq
 * 
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "name_user")
public class NameUser implements Serializable, Cloneable {

	@EmbeddedId
	private NameUserPK nameUserPK;

	@Column(name = "user_id", nullable = false)
	private long userId;
	
	public NameUser() {
		nameUserPK = new NameUserPK();
	}
	
	public NameUserPK getNameUserPK() {
		return nameUserPK;
	}

	public void setNameUserPK(NameUserPK nameUserPK) {
		this.nameUserPK = nameUserPK;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((nameUserPK == null) ? 0 : nameUserPK.hashCode());
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
		NameUser other = (NameUser) obj;
		if (nameUserPK == null) {
			if (other.nameUserPK != null)
				return false;
		} else if (!nameUserPK.equals(other.nameUserPK))
			return false;
		if (userId != other.userId)
			return false;
		return true;
	}

	@Override
	public NameUser clone() {
		try {
			return (NameUser) super.clone();
		} catch (CloneNotSupportedException e) {
			return null;
		}
	}

}
