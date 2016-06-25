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
@Table(name = "pay_cust")
public class PayCust implements Serializable, Cloneable {

	@Id
	@GeneratedValue
	@Column(name = "id")
	private long id;

	@Column(name = "cust_id", nullable = false)
	private long custId;

	@Column(name = "pay_amount", nullable = false)
	private long payAmount;

	@Column(name = "pay_type", nullable = false)
	private short payType;

	@Column(name = "account_id", nullable = false)
	private long accountId;

	@Column(name = "pay_date", nullable = false)
	private Timestamp payDate;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getCustId() {
		return custId;
	}

	public void setCustId(long custId) {
		this.custId = custId;
	}

	public long getPayAmount() {
		return payAmount;
	}

	public void setPayAmount(long payAmount) {
		this.payAmount = payAmount;
	}

	public short getPayType() {
		return payType;
	}

	public void setPayType(short payType) {
		this.payType = payType;
	}

	public long getAccountId() {
		return accountId;
	}

	public void setAccountId(long accountId) {
		this.accountId = accountId;
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
		result = prime * result + (int) (accountId ^ (accountId >>> 32));
		result = prime * result + (int) (custId ^ (custId >>> 32));
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + (int) (payAmount ^ (payAmount >>> 32));
		result = prime * result + ((payDate == null) ? 0 : payDate.hashCode());
		result = prime * result + payType;
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
		PayCust other = (PayCust) obj;
		if (accountId != other.accountId)
			return false;
		if (custId != other.custId)
			return false;
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
		return true;
	}

	@Override
	public PayCust clone() {
		try {
			return (PayCust) super.clone();
		} catch (CloneNotSupportedException e) {
			return null;
		}
	}

}
