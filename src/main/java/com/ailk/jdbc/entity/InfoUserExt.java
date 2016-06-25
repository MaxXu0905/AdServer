package com.ailk.jdbc.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@SuppressWarnings("serial")
@Entity
@Table(name = "info_user_ext")
public class InfoUserExt implements Serializable, Cloneable {

	@EmbeddedId
	private InfoUserExtPK infoUserExtPK;

	@Column(name = "attr_value")
	private int attrValue;
	
	public InfoUserExt() {
		infoUserExtPK = new InfoUserExtPK();
	}

	public InfoUserExtPK getInfoUserExtPK() {
		return infoUserExtPK;
	}

	public void setInfoUserExtPK(InfoUserExtPK infoUserExtPK) {
		this.infoUserExtPK = infoUserExtPK;
	}

	public int getAttrValue() {
		return attrValue;
	}

	public void setAttrValue(int attrValue) {
		this.attrValue = attrValue;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + attrValue;
		result = prime * result + ((infoUserExtPK == null) ? 0 : infoUserExtPK.hashCode());
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
		InfoUserExt other = (InfoUserExt) obj;
		if (attrValue != other.attrValue)
			return false;
		if (infoUserExtPK == null) {
			if (other.infoUserExtPK != null)
				return false;
		} else if (!infoUserExtPK.equals(other.infoUserExtPK))
			return false;
		return true;
	}

	@Override
	public InfoUserExt clone() {
		try {
			return (InfoUserExt) super.clone();
		} catch (CloneNotSupportedException e) {
			return null;
		}
	}

}
