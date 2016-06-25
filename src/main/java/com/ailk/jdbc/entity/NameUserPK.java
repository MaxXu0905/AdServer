package com.ailk.jdbc.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * 用户名与用户ID的映射表，在分布式情况下，不能单单通过键索引快速获取
 * 
 * @author xugq
 * 
 */
@SuppressWarnings("serial")
@Embeddable
public class NameUserPK implements Serializable, Cloneable {

	@Column(name = "userName", nullable = false)
	private String userName;

	@Column(name = "registered", nullable = false)
	private boolean registered;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public boolean isRegistered() {
		return registered;
	}

	public void setRegistered(boolean registered) {
		this.registered = registered;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (registered ? 1231 : 1237);
		result = prime * result + ((userName == null) ? 0 : userName.hashCode());
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
		NameUserPK other = (NameUserPK) obj;
		if (registered != other.registered)
			return false;
		if (userName == null) {
			if (other.userName != null)
				return false;
		} else if (!userName.equals(other.userName))
			return false;
		return true;
	}

	@Override
	public NameUserPK clone() {
		try {
			return (NameUserPK) super.clone();
		} catch (CloneNotSupportedException e) {
			return null;
		}
	}
}
