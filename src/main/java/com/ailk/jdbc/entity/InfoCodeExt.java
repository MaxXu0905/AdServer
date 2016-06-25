package com.ailk.jdbc.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@SuppressWarnings("serial")
@Entity
@Table(name = "info_code_ext")
public class InfoCodeExt implements Serializable, Cloneable {

	@EmbeddedId
	private InfoCodeExtPK infoCodeExtPK;

	@Column(name = "description", nullable = false)
	private String desc;

	public InfoCodeExt() {
		infoCodeExtPK = new InfoCodeExtPK();
	}

	public InfoCodeExtPK getInfoCodeExtPK() {
		return infoCodeExtPK;
	}

	public void setInfoCodeExtPK(InfoCodeExtPK infoCodeExtPK) {
		this.infoCodeExtPK = infoCodeExtPK;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	@Override
	public InfoCodeExt clone() {
		try {
			return (InfoCodeExt) super.clone();
		} catch (CloneNotSupportedException e) {
			return null;
		}
	}

}
