package com.ailk.jdbc.entity;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@SuppressWarnings("serial")
@Entity
@Table(name = "pay_rqst")
public class PayRqst implements Serializable, Cloneable {

	@Id
	@GeneratedValue
	@Column(name = "id")
	private long id;

	@Column(name = "user_id", nullable = false)
	private long userId;

	@Column(name = "pay_type", nullable = false)
	private int payType;

	@Column(name = "pay_name")
	private String payName;

	@Column(name = "pay_id", nullable = false)
	private String payId;

	@Column(name = "amount", nullable = false)
	private int amount;

	@Column(name = "status", nullable = false)
	private short status;

	@Column(name = "reason")
	private String reason;

	@Column(name = "rqst_date", nullable = false)
	private Timestamp rqstDate;

	@Column(name = "last_update", nullable = false)
	private Timestamp lastUpdate;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public int getPayType() {
		return payType;
	}

	public void setPayType(int payType) {
		this.payType = payType;
	}

	public String getPayName() {
		return payName;
	}

	public void setPayName(String payName) {
		this.payName = payName;
	}

	public String getPayId() {
		return payId;
	}

	public void setPayId(String payId) {
		this.payId = payId;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public short getStatus() {
		return status;
	}

	public void setStatus(short status) {
		this.status = status;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public Timestamp getRqstDate() {
		return rqstDate;
	}

	public void setRqstDate(Timestamp rqstDate) {
		this.rqstDate = rqstDate;
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
		result = prime * result + amount;
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + ((lastUpdate == null) ? 0 : lastUpdate.hashCode());
		result = prime * result + ((payId == null) ? 0 : payId.hashCode());
		result = prime * result + ((payName == null) ? 0 : payName.hashCode());
		result = prime * result + payType;
		result = prime * result + ((reason == null) ? 0 : reason.hashCode());
		result = prime * result + ((rqstDate == null) ? 0 : rqstDate.hashCode());
		result = prime * result + status;
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
		PayRqst other = (PayRqst) obj;
		if (amount != other.amount)
			return false;
		if (id != other.id)
			return false;
		if (lastUpdate == null) {
			if (other.lastUpdate != null)
				return false;
		} else if (!lastUpdate.equals(other.lastUpdate))
			return false;
		if (payId == null) {
			if (other.payId != null)
				return false;
		} else if (!payId.equals(other.payId))
			return false;
		if (payName == null) {
			if (other.payName != null)
				return false;
		} else if (!payName.equals(other.payName))
			return false;
		if (payType != other.payType)
			return false;
		if (reason == null) {
			if (other.reason != null)
				return false;
		} else if (!reason.equals(other.reason))
			return false;
		if (rqstDate == null) {
			if (other.rqstDate != null)
				return false;
		} else if (!rqstDate.equals(other.rqstDate))
			return false;
		if (status != other.status)
			return false;
		if (userId != other.userId)
			return false;
		return true;
	}

	@Override
	public PayRqst clone() {
		try {
			return (PayRqst) super.clone();
		} catch (CloneNotSupportedException e) {
			return null;
		}
	}

}
