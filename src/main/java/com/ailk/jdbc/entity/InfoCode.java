package com.ailk.jdbc.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@SuppressWarnings("serial")
@Entity
@Table(name = "info_code")
public class InfoCode implements Serializable, Cloneable {

	@EmbeddedId
	private InfoCodePK infoCodePK;

	@Column(name = "description", nullable = false)
	private String desc;

	@Column(name = "extra_info")
	private String extraInfo;
	
	@Column(name = "width")
	private Integer width;
	
	public InfoCode() {
		infoCodePK = new InfoCodePK();
	}

	public InfoCodePK getInfoCodePK() {
		return infoCodePK;
	}

	public void setInfoCodePK(InfoCodePK infoCodePK) {
		this.infoCodePK = infoCodePK;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
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
	public InfoCode clone() {
		try {
			return (InfoCode) super.clone();
		} catch (CloneNotSupportedException e) {
			return null;
		}
	}

}
