package com.ailk.jdbc.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * ÅÅÐÐ°ñ±í
 * 
 * @author xugq
 * 
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "rank_board")
public class RankBoard implements Serializable, Cloneable {

	@Id
	@Column(name = "rank")
	private int rank;

	@Column(name = "portrait")
	private String portrait;

	@Column(name = "nickName")
	private String nickName;

	@Column(name = "level")
	private Integer level;

	@Column(name = "profit")
	private long profit;

	@Column(name = "user_id")
	private long userId;

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	public String getPortrait() {
		return portrait;
	}

	public void setPortrait(String portrait) {
		this.portrait = portrait;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public long getProfit() {
		return profit;
	}

	public void setProfit(long profit) {
		this.profit = profit;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((level == null) ? 0 : level.hashCode());
		result = prime * result + ((nickName == null) ? 0 : nickName.hashCode());
		result = prime * result + ((portrait == null) ? 0 : portrait.hashCode());
		result = prime * result + (int) (profit ^ (profit >>> 32));
		result = prime * result + rank;
		result = prime * result + (int) (userId ^ (userId >>> 32));
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
		RankBoard other = (RankBoard) obj;
		if (level == null) {
			if (other.level != null)
				return false;
		} else if (!level.equals(other.level))
			return false;
		if (nickName == null) {
			if (other.nickName != null)
				return false;
		} else if (!nickName.equals(other.nickName))
			return false;
		if (portrait == null) {
			if (other.portrait != null)
				return false;
		} else if (!portrait.equals(other.portrait))
			return false;
		if (profit != other.profit)
			return false;
		if (rank != other.rank)
			return false;
		if (userId != other.userId)
			return false;
		return true;
	}

	public RankBoard clone() {
		try {
			return (RankBoard) super.clone();
		} catch (CloneNotSupportedException e) {
			return null;
		}
	}

}
