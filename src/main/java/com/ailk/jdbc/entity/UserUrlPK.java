package com.ailk.jdbc.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * 用户查看详情表主键
 * 
 * @author xugq
 * 
 */
@SuppressWarnings("serial")
@Embeddable
public class UserUrlPK implements Serializable, Cloneable {

	@Column(name = "user_id", nullable = false)
	private long userId;

	@Column(name = "url", nullable = false)
	private String url;

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((url == null) ? 0 : url.hashCode());
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
		UserUrlPK other = (UserUrlPK) obj;
		if (url == null) {
			if (other.url != null)
				return false;
		} else if (!url.equals(other.url))
			return false;
		if (userId != other.userId)
			return false;
		return true;
	}

	@Override
	public UserUrlPK clone() {
		try {
			return (UserUrlPK) super.clone();
		} catch (CloneNotSupportedException e) {
			return null;
		}
	}
}
