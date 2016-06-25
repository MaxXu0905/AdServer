package com.ailk.jdbc.entity;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 用户广告状态表
 * 
 * @author xugq
 * 
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "user_url")
public class UserUrl implements Serializable, Cloneable {

	@EmbeddedId
	private UserUrlPK userUrlPK;
	
	public UserUrl() {
		userUrlPK = new UserUrlPK();
	}

	public UserUrlPK getUserUrlPK() {
		return userUrlPK;
	}

	public void setUserUrlPK(UserUrlPK userUrlPK) {
		this.userUrlPK = userUrlPK;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((userUrlPK == null) ? 0 : userUrlPK.hashCode());
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
		UserUrl other = (UserUrl) obj;
		if (userUrlPK == null) {
			if (other.userUrlPK != null)
				return false;
		} else if (!userUrlPK.equals(other.userUrlPK))
			return false;
		return true;
	}

	public UserUrl clone() {
		try {
			return (UserUrl) super.clone();
		} catch (CloneNotSupportedException e) {
			return null;
		}
	}

}
