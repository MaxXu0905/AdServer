package com.ailk.jdbc.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 用户广告状态表
 * 
 * @author xugq
 * 
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "ad_participant")
public class AdParticipant implements Serializable, Cloneable {

	@Id
	@Column(name = "ad_id")
	private int adId;

	@Column(name = "participants", nullable = false)
	private int participants; // 参与人数

	public int getAdId() {
		return adId;
	}

	public void setAdId(int adId) {
		this.adId = adId;
	}

	public int getParticipants() {
		return participants;
	}

	public void setParticipants(int participants) {
		this.participants = participants;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + adId;
		result = prime * result + participants;
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
		AdParticipant other = (AdParticipant) obj;
		if (adId != other.adId)
			return false;
		if (participants != other.participants)
			return false;
		return true;
	}

	public AdParticipant clone() {
		try {
			return (AdParticipant) super.clone();
		} catch (CloneNotSupportedException e) {
			return null;
		}
	}

}
