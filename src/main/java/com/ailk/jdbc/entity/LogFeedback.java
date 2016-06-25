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
@Table(name = "log_feedback")
public class LogFeedback implements Serializable, Cloneable {

	@Id
	@GeneratedValue
	@Column(name = "id")
	private long id;

	@Column(name = "user_id", nullable = false)
	private long userId;

	@Column(name = "content", nullable = false)
	private String content;

	@Column(name = "action_date", nullable = false)
	private Timestamp actionDate;

	@Column(name = "status", nullable = false)
	private short status;

	@Column(name = "reason")
	private String reason;

	@Column(name = "oper_id")
	private Long operId;

	@Column(name = "oper_date")
	private Timestamp operDate;

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

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Timestamp getActionDate() {
		return actionDate;
	}

	public void setActionDate(Timestamp actionDate) {
		this.actionDate = actionDate;
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

	public Long getOperId() {
		return operId;
	}

	public void setOperId(Long operId) {
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
		result = prime * result + ((actionDate == null) ? 0 : actionDate.hashCode());
		result = prime * result + ((content == null) ? 0 : content.hashCode());
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + ((operDate == null) ? 0 : operDate.hashCode());
		result = prime * result + ((operId == null) ? 0 : operId.hashCode());
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
		LogFeedback other = (LogFeedback) obj;
		if (actionDate == null) {
			if (other.actionDate != null)
				return false;
		} else if (!actionDate.equals(other.actionDate))
			return false;
		if (content == null) {
			if (other.content != null)
				return false;
		} else if (!content.equals(other.content))
			return false;
		if (id != other.id)
			return false;
		if (operDate == null) {
			if (other.operDate != null)
				return false;
		} else if (!operDate.equals(other.operDate))
			return false;
		if (operId == null) {
			if (other.operId != null)
				return false;
		} else if (!operId.equals(other.operId))
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
	public LogFeedback clone() {
		try {
			return (LogFeedback) super.clone();
		} catch (CloneNotSupportedException e) {
			return null;
		}
	}

}
