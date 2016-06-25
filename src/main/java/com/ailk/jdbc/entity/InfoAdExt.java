package com.ailk.jdbc.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@SuppressWarnings("serial")
@Entity
@Table(name = "info_ad_ext")
public class InfoAdExt implements Serializable, Cloneable {

	@EmbeddedId
	private InfoAdExtPK infoAdExtPK;

	@Column(name = "attr_values", nullable = false)
	private String attrValues;
	
	public InfoAdExt() {
		infoAdExtPK = new InfoAdExtPK();
	}

	public InfoAdExtPK getInfoAdExtPK() {
		return infoAdExtPK;
	}

	public void setInfoAdExtPK(InfoAdExtPK infoAdExtPK) {
		this.infoAdExtPK = infoAdExtPK;
	}

	public String getAttrValues() {
		return attrValues;
	}

	public void setAttrValues(String attrValues) {
		this.attrValues = attrValues;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((attrValues == null) ? 0 : attrValues.hashCode());
		result = prime * result + ((infoAdExtPK == null) ? 0 : infoAdExtPK.hashCode());
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
		InfoAdExt other = (InfoAdExt) obj;
		if (attrValues == null) {
			if (other.attrValues != null)
				return false;
		} else if (!attrValues.equals(other.attrValues))
			return false;
		if (infoAdExtPK == null) {
			if (other.infoAdExtPK != null)
				return false;
		} else if (!infoAdExtPK.equals(other.infoAdExtPK))
			return false;
		return true;
	}

	@Override
	public InfoAdExt clone() {
		try {
			return (InfoAdExt) super.clone();
		} catch (CloneNotSupportedException e) {
			return null;
		}
	}

}
