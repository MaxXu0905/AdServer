package com.ailk.jdbc.entity;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 用户可看的广告列表
 * 
 * @author xugq
 * 
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "user_ad")
public class UserAd implements Serializable, Cloneable {

	@EmbeddedId
	private UserAdPK userAdPK;

	@Column(name = "ads", length = 4000)
	private String ads;

	@Column(name = "old_ads", length = 4000)
	private String oldAds;

	@Column(name = "sysdate", nullable = false)
	private long sysdate;

	@Column(name = "last_update", nullable = false)
	private Timestamp lastUpdate;

	public UserAd() {
		userAdPK = new UserAdPK();
	}

	public UserAdPK getUserAdPK() {
		return userAdPK;
	}

	public void setUserAdPK(UserAdPK userAdPK) {
		this.userAdPK = userAdPK;
	}

	public String getAds() {
		return ads;
	}

	public void setAds(String ads) {
		this.ads = ads;
	}

	public String getOldAds() {
		return oldAds;
	}

	public void setOldAds(String oldAds) {
		this.oldAds = oldAds;
	}

	public long getSysdate() {
		return sysdate;
	}

	public void setSysdate(long sysdate) {
		this.sysdate = sysdate;
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
		result = prime * result + ((ads == null) ? 0 : ads.hashCode());
		result = prime * result + ((lastUpdate == null) ? 0 : lastUpdate.hashCode());
		result = prime * result + ((oldAds == null) ? 0 : oldAds.hashCode());
		result = prime * result + (int) (sysdate ^ (sysdate >>> 32));
		result = prime * result + ((userAdPK == null) ? 0 : userAdPK.hashCode());
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
		UserAd other = (UserAd) obj;
		if (ads == null) {
			if (other.ads != null)
				return false;
		} else if (!ads.equals(other.ads))
			return false;
		if (lastUpdate == null) {
			if (other.lastUpdate != null)
				return false;
		} else if (!lastUpdate.equals(other.lastUpdate))
			return false;
		if (oldAds == null) {
			if (other.oldAds != null)
				return false;
		} else if (!oldAds.equals(other.oldAds))
			return false;
		if (sysdate != other.sysdate)
			return false;
		if (userAdPK == null) {
			if (other.userAdPK != null)
				return false;
		} else if (!userAdPK.equals(other.userAdPK))
			return false;
		return true;
	}

	@Override
	public UserAd clone() {
		try {
			return (UserAd) super.clone();
		} catch (CloneNotSupportedException e) {
			return null;
		}
	}

}
