package com.ailk.jdbc.entity;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
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
@Table(name = "user_ad_status")
public class UserAdStatus implements Serializable, Cloneable {

	@EmbeddedId
	private UserAdStatusPK userAdStatusPK;

	@Column(name = "sysdate")
	private long sysdate;

	@Column(name = "days")
	private int days;

	@Column(name = "times")
	private int times;

	@Column(name = "times2")
	private int times2;

	@Column(name = "lock_amount")
	private long lockAmount;

	@Column(name = "rejected")
	private boolean rejected;

	@Column(name = "expire_date")
	private Timestamp expireDate;

	@Column(name = "last_update")
	private Timestamp lastUpdate;

	public UserAdStatus() {
		userAdStatusPK = new UserAdStatusPK();
	}

	public UserAdStatusPK getUserAdStatusPK() {
		return userAdStatusPK;
	}

	public void setUserAdStatusPK(UserAdStatusPK userAdStatusPK) {
		this.userAdStatusPK = userAdStatusPK;
	}

	public long getSysdate() {
		return sysdate;
	}

	public void setSysdate(long sysdate) {
		this.sysdate = sysdate;
	}

	public int getDays() {
		return days;
	}

	public void setDays(int days) {
		this.days = days;
	}

	public int getTimes() {
		return times;
	}

	public void setTimes(int times) {
		this.times = times;
	}

	public int getTimes2() {
		return times2;
	}

	public void setTimes2(int times2) {
		this.times2 = times2;
	}

	public long getLockAmount() {
		return lockAmount;
	}

	public void setLockAmount(long lockAmount) {
		this.lockAmount = lockAmount;
	}

	public boolean isRejected() {
		return rejected;
	}

	public void setRejected(boolean rejected) {
		this.rejected = rejected;
	}

	public Timestamp getExpireDate() {
		return expireDate;
	}

	public void setExpireDate(Timestamp expireDate) {
		this.expireDate = expireDate;
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
		result = prime * result + days;
		result = prime * result + ((expireDate == null) ? 0 : expireDate.hashCode());
		result = prime * result + ((lastUpdate == null) ? 0 : lastUpdate.hashCode());
		result = prime * result + (int) (lockAmount ^ (lockAmount >>> 32));
		result = prime * result + (rejected ? 1231 : 1237);
		result = prime * result + (int) (sysdate ^ (sysdate >>> 32));
		result = prime * result + times;
		result = prime * result + times2;
		result = prime * result + ((userAdStatusPK == null) ? 0 : userAdStatusPK.hashCode());
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
		UserAdStatus other = (UserAdStatus) obj;
		if (days != other.days)
			return false;
		if (expireDate == null) {
			if (other.expireDate != null)
				return false;
		} else if (!expireDate.equals(other.expireDate))
			return false;
		if (lastUpdate == null) {
			if (other.lastUpdate != null)
				return false;
		} else if (!lastUpdate.equals(other.lastUpdate))
			return false;
		if (lockAmount != other.lockAmount)
			return false;
		if (rejected != other.rejected)
			return false;
		if (sysdate != other.sysdate)
			return false;
		if (times != other.times)
			return false;
		if (times2 != other.times2)
			return false;
		if (userAdStatusPK == null) {
			if (other.userAdStatusPK != null)
				return false;
		} else if (!userAdStatusPK.equals(other.userAdStatusPK))
			return false;
		return true;
	}

	public UserAdStatus clone() {
		try {
			return (UserAdStatus) super.clone();
		} catch (CloneNotSupportedException e) {
			return null;
		}
	}

}
