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
@Table(name = "pay_user")
public class PayUser implements Serializable, Cloneable {

	@Id
	@GeneratedValue
	@Column(name = "id")
	private long id;

	@Column(name = "user_id", nullable = false)
	private long userId;

	@Column(name = "pay_amount", nullable = false)
	private int payAmount;

	@Column(name = "pay_type", nullable = false)
	private short payType;

	@Column(name = "pay_date", nullable = false)
	private Timestamp payDate;

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

	public int getPayAmount() {
		return payAmount;
	}

	public void setPayAmount(int payAmount) {
		this.payAmount = payAmount;
	}

	public short getPayType() {
		return payType;
	}

	public void setPayType(short payType) {
		this.payType = payType;
	}

	public Timestamp getPayDate() {
		return payDate;
	}

	public void setPayDate(Timestamp payDate) {
		this.payDate = payDate;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + payAmount;
		result = prime * result + ((payDate == null) ? 0 : payDate.hashCode());
		result = prime * result + payType;
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
		PayUser other = (PayUser) obj;
		if (id != other.id)
			return false;
		if (payAmount != other.payAmount)
			return false;
		if (payDate == null) {
			if (other.payDate != null)
				return false;
		} else if (!payDate.equals(other.payDate))
			return false;
		if (payType != other.payType)
			return false;
		if (userId != other.userId)
			return false;
		return true;
	}

	@Override
	public PayUser clone() {
		try {
			return (PayUser) super.clone();
		} catch (CloneNotSupportedException e) {
			return null;
		}
	}

}
