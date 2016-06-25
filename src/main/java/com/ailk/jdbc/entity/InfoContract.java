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
@Table(name = "info_contract")
public class InfoContract implements Serializable, Cloneable {

	@Id
	@GeneratedValue
	@Column(name = "contract_id")
	private int contractId;

	@Column(name = "cust_id", nullable = false)
	private long custId;

	@Column(name = "reg_date", nullable = false)
	private Timestamp regDate;

	@Column(name = "status", nullable = false)
	private short status;

	@Column(name = "reason")
	private String reason;

	@Column(name = "oper_id", nullable = false)
	private long operId;

	@Column(name = "oper_date", nullable = false)
	private Timestamp operDate;

	public int getContractId() {
		return contractId;
	}

	public void setContractId(int contractId) {
		this.contractId = contractId;
	}

	public long getCustId() {
		return custId;
	}

	public void setCustId(long custId) {
		this.custId = custId;
	}

	public Timestamp getRegDate() {
		return regDate;
	}

	public void setRegDate(Timestamp regDate) {
		this.regDate = regDate;
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

	public long getOperId() {
		return operId;
	}

	public void setOperId(long operId) {
		this.operId = operId;
	}

	public Timestamp getOperDate() {
		return operDate;
	}

	public void setOperDate(Timestamp operDate) {
		this.operDate = operDate;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + contractId;
		result = prime * result + (int) (custId ^ (custId >>> 32));
		result = prime * result + ((operDate == null) ? 0 : operDate.hashCode());
		result = prime * result + (int) (operId ^ (operId >>> 32));
		result = prime * result + ((reason == null) ? 0 : reason.hashCode());
		result = prime * result + ((regDate == null) ? 0 : regDate.hashCode());
		result = prime * result + status;
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
		InfoContract other = (InfoContract) obj;
		if (contractId != other.contractId)
			return false;
		if (custId != other.custId)
			return false;
		if (operDate == null) {
			if (other.operDate != null)
				return false;
		} else if (!operDate.equals(other.operDate))
			return false;
		if (operId != other.operId)
			return false;
		if (reason == null) {
			if (other.reason != null)
				return false;
		} else if (!reason.equals(other.reason))
			return false;
		if (regDate == null) {
			if (other.regDate != null)
				return false;
		} else if (!regDate.equals(other.regDate))
			return false;
		if (status != other.status)
			return false;
		return true;
	}

	@Override
	public InfoContract clone() {
		try {
			return (InfoContract) super.clone();
		} catch (CloneNotSupportedException e) {
			return null;
		}
	}

}
