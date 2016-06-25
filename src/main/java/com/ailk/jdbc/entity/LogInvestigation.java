package com.ailk.jdbc.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@SuppressWarnings("serial")
@Entity
@Table(name = "log_investigation")
public class LogInvestigation implements Serializable, Cloneable {

	@EmbeddedId
	private LogInvestigationPK logInvestigationPK;

	@Column(name = "investigations")
	private String investigations;

	public LogInvestigationPK getLogInvestigationPK() {
		return logInvestigationPK;
	}

	public void setLogInvestigationPK(LogInvestigationPK logInvestigationPK) {
		this.logInvestigationPK = logInvestigationPK;
	}

	public String getInvestigations() {
		return investigations;
	}

	public void setInvestigations(String investigations) {
		this.investigations = investigations;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((investigations == null) ? 0 : investigations.hashCode());
		result = prime * result + ((logInvestigationPK == null) ? 0 : logInvestigationPK.hashCode());
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
		LogInvestigation other = (LogInvestigation) obj;
		if (investigations == null) {
			if (other.investigations != null)
				return false;
		} else if (!investigations.equals(other.investigations))
			return false;
		if (logInvestigationPK == null) {
			if (other.logInvestigationPK != null)
				return false;
		} else if (!logInvestigationPK.equals(other.logInvestigationPK))
			return false;
		return true;
	}

	@Override
	public LogInvestigation clone() {
		try {
			return (LogInvestigation) super.clone();
		} catch (CloneNotSupportedException e) {
			return null;
		}
	}

}
