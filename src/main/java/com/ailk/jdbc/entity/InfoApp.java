package com.ailk.jdbc.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@SuppressWarnings("serial")
@Entity
@Table(name = "info_app")
public class InfoApp implements Serializable, Cloneable {

	@Id
	@Column(name = "attr_name")
	private String attrName;

	@Column(name = "attr_value", nullable = false)
	private String attrValue;

	public String getAttrName() {
		return attrName;
	}

	public void setAttrName(String attrName) {
		this.attrName = attrName;
	}

	public String getAttrValue() {
		return attrValue;
	}

	public void setAttrValue(String attrValue) {
		this.attrValue = attrValue;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((attrName == null) ? 0 : attrName.hashCode());
		result = prime * result + ((attrValue == null) ? 0 : attrValue.hashCode());
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
		InfoApp other = (InfoApp) obj;
		if (attrName == null) {
			if (other.attrName != null)
				return false;
		} else if (!attrName.equals(other.attrName))
			return false;
		if (attrValue == null) {
			if (other.attrValue != null)
				return false;
		} else if (!attrValue.equals(other.attrValue))
			return false;
		return true;
	}

	@Override
	public InfoApp clone() {
		try {
			return (InfoApp) super.clone();
		} catch (CloneNotSupportedException e) {
			return null;
		}
	}

}
