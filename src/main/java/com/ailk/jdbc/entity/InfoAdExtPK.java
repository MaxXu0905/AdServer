package com.ailk.jdbc.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@SuppressWarnings("serial")
@Embeddable
public class InfoAdExtPK implements Serializable, Cloneable {

	@Column(name = "ad_id")
	private int adId;

	@Column(name = "attr_id")
	private short attrId;

	public int getAdId() {
		return adId;
	}

	public void setAdId(int adId) {
		this.adId = adId;
	}

	public short getAttrId() {
		return attrId;
	}

	public void setAttrId(short attrId) {
		this.attrId = attrId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + adId;
		result = prime * result + attrId;
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
		InfoAdExtPK other = (InfoAdExtPK) obj;
		if (adId != other.adId)
			return false;
		if (attrId != other.attrId)
			return false;
		return true;
	}

	@Override
	public InfoAdExtPK clone() {
		try {
			return (InfoAdExtPK) super.clone();
		} catch (CloneNotSupportedException e) {
			return null;
		}
	}

}
