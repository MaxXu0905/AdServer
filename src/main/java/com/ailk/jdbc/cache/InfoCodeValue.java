package com.ailk.jdbc.cache;

import java.util.ArrayList;
import java.util.List;

public class InfoCodeValue {

	private String desc;
	private String extraInfo;
	private Integer width;
	private List<InfoCodeExtValue> items;

	public InfoCodeValue() {
		items = new ArrayList<InfoCodeExtValue>();
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public List<InfoCodeExtValue> getItems() {
		return items;
	}

	public void setItems(List<InfoCodeExtValue> items) {
		this.items = items;
	}

	public String getExtraInfo() {
		return extraInfo;
	}

	public void setExtraInfo(String extraInfo) {
		this.extraInfo = extraInfo;
	}

	public Integer getWidth() {
		return width;
	}

	public void setWidth(Integer width) {
		this.width = width;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((desc == null) ? 0 : desc.hashCode());
		result = prime * result
				+ ((extraInfo == null) ? 0 : extraInfo.hashCode());
		result = prime * result + ((items == null) ? 0 : items.hashCode());
		result = prime * result + ((width == null) ? 0 : width.hashCode());
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
		InfoCodeValue other = (InfoCodeValue) obj;
		if (desc == null) {
			if (other.desc != null)
				return false;
		} else if (!desc.equals(other.desc))
			return false;
		if (extraInfo == null) {
			if (other.extraInfo != null)
				return false;
		} else if (!extraInfo.equals(other.extraInfo))
			return false;
		if (items == null) {
			if (other.items != null)
				return false;
		} else if (!items.equals(other.items))
			return false;
		if (width == null) {
			if (other.width != null)
				return false;
		} else if (!width.equals(other.width))
			return false;
		return true;
	}

}
