package com.ailk.jdbc.entity;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@SuppressWarnings("serial")
@Entity
@Table(name = "ad_balance")
public class AdBalance implements Serializable, Cloneable {

	@Id
	@Column(name = "ad_id", nullable = false)
	private int adId;

	@Column(name = "budget", nullable = false)
	private long budget;

	@Column(name = "remain", nullable = false)
	private long remain;

	@Column(name = "locked", nullable = false)
	private long locked;

	@Column(name = "last_update", nullable = false)
	private Timestamp lastUpdate;

	public int getAdId() {
		return adId;
	}

	public void setAdId(int adId) {
		this.adId = adId;
	}

	public long getBudget() {
		return budget;
	}

	public void setBudget(long budget) {
		this.budget = budget;
	}

	public long getRemain() {
		return remain;
	}

	public void setRemain(long remain) {
		this.remain = remain;
	}

	public long getLocked() {
		return locked;
	}

	public void setLocked(long locked) {
		this.locked = locked;
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
		result = prime * result + adId;
		result = prime * result + (int) (budget ^ (budget >>> 32));
		result = prime * result + ((lastUpdate == null) ? 0 : lastUpdate.hashCode());
		result = prime * result + (int) (locked ^ (locked >>> 32));
		result = prime * result + (int) (remain ^ (remain >>> 32));
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
		AdBalance other = (AdBalance) obj;
		if (adId != other.adId)
			return false;
		if (budget != other.budget)
			return false;
		if (lastUpdate == null) {
			if (other.lastUpdate != null)
				return false;
		} else if (!lastUpdate.equals(other.lastUpdate))
			return false;
		if (locked != other.locked)
			return false;
		if (remain != other.remain)
			return false;
		return true;
	}

	@Override
	public AdBalance clone() {
		try {
			return (AdBalance) super.clone();
		} catch (CloneNotSupportedException e) {
			return null;
		}
	}

}
