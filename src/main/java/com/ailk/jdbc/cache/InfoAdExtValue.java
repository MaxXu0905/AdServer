package com.ailk.jdbc.cache;

import java.util.HashSet;
import java.util.Set;

public class InfoAdExtValue {

	private short attrId;
	private String attrValue;
	private Set<Integer> attrValueSet;

	public short getAttrId() {
		return attrId;
	}

	public void setAttrId(short attrId) {
		this.attrId = attrId;
	}

	public String getAttrValue() {
		return attrValue;
	}

	public void setAttrValue(String attrValue) {
		this.attrValue = attrValue;

		if (attrValueSet == null)
			attrValueSet = new HashSet<Integer>();
		else
			attrValueSet.clear();

		for (String field : attrValue.split(",")) {
			attrValueSet.add(Integer.parseInt(field));
		}
	}

	public Set<Integer> getAttrValueSet() {
		return attrValueSet;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + attrId;
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
		InfoAdExtValue other = (InfoAdExtValue) obj;
		if (attrId != other.attrId)
			return false;
		if (attrValue == null) {
			if (other.attrValue != null)
				return false;
		} else if (!attrValue.equals(other.attrValue))
			return false;
		return true;
	}

}
