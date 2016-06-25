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
@Table(name = "log_promotion")
public class LogPromotion implements Serializable, Cloneable {

	@Id
	@GeneratedValue
	@Column(name = "id")
	private long id;

	@Column(name = "user_id", nullable = false)
	private long userId;

	@Column(name = "ad_id", nullable = false)
	private int adId;

	@Column(name = "action_date", nullable = false)
	private Timestamp actionDate;

	@Column(name = "investigations")
	private String investigations;

	@Column(name = "answers", nullable = false)
	private String answers;

	@Column(name = "status", nullable = false)
	private short status;

	@Column(name = "profit")
	private Long profit;

	@Column(name = "reason")
	private String reason;

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

	public int getAdId() {
		return adId;
	}

	public void setAdId(int adId) {
		this.adId = adId;
	}

	public Timestamp getActionDate() {
		return actionDate;
	}

	public void setActionDate(Timestamp actionDate) {
		this.actionDate = actionDate;
	}

	public String getInvestigations() {
		return investigations;
	}

	public void setInvestigations(String investigations) {
		this.investigations = investigations;
	}

	public String getAnswers() {
		return answers;
	}

	public void setAnswers(String answers) {
		this.answers = answers;
	}

	public short getStatus() {
		return status;
	}

	public void setStatus(short status) {
		this.status = status;
	}

	public Long getProfit() {
		return profit;
	}

	public void setProfit(Long profit) {
		this.profit = profit;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
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
		result = prime * result + ((actionDate == null) ? 0 : actionDate.hashCode());
		result = prime * result + adId;
		result = prime * result + ((answers == null) ? 0 : answers.hashCode());
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + ((investigations == null) ? 0 : investigations.hashCode());
		result = prime * result + ((lastUpdate == null) ? 0 : lastUpdate.hashCode());
		result = prime * result + ((profit == null) ? 0 : profit.hashCode());
		result = prime * result + ((reason == null) ? 0 : reason.hashCode());
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
		LogPromotion other = (LogPromotion) obj;
		if (actionDate == null) {
			if (other.actionDate != null)
				return false;
		} else if (!actionDate.equals(other.actionDate))
			return false;
		if (adId != other.adId)
			return false;
		if (answers == null) {
			if (other.answers != null)
				return false;
		} else if (!answers.equals(other.answers))
			return false;
		if (id != other.id)
			return false;
		if (investigations == null) {
			if (other.investigations != null)
				return false;
		} else if (!investigations.equals(other.investigations))
			return false;
		if (lastUpdate == null) {
			if (other.lastUpdate != null)
				return false;
		} else if (!lastUpdate.equals(other.lastUpdate))
			return false;
		if (profit == null) {
			if (other.profit != null)
				return false;
		} else if (!profit.equals(other.profit))
			return false;
		if (reason == null) {
			if (other.reason != null)
				return false;
		} else if (!reason.equals(other.reason))
			return false;
		if (status != other.status)
			return false;
		if (userId != other.userId)
			return false;
		return true;
	}

	@Override
	public LogPromotion clone() {
		try {
			return (LogPromotion) super.clone();
		} catch (CloneNotSupportedException e) {
			return null;
		}
	}

}
