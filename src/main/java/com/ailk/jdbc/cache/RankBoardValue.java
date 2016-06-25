package com.ailk.jdbc.cache;

import com.ailk.jdbc.entity.RankBoard;

public class RankBoardValue {

	private String portrait; // ͷ��
	private String nickName; // �ǳ�
	private Integer level; // ����
	private long profit; // ����
	private long userId; // �û�ID
	
	public void set(RankBoard entity) {
		portrait = entity.getPortrait();
		nickName = entity.getNickName();
		level = entity.getLevel();
		profit = entity.getProfit();
		userId = entity.getUserId();
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

}
