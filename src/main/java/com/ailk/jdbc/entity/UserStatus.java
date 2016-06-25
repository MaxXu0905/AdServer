package com.ailk.jdbc.entity;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;

/**
 * 用户广告状态表
 * 
 * @author xugq
 * 
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "user_status")
public class UserStatus implements Serializable, Cloneable {

	@Id
	@Column(name = "user_id")
	private long userId;

	@Column(name = "dev_id")
	private String devId;

	@Column(name = "bd_user_id")
	private String bdUserId;

	@Column(name = "login_time")
	private Timestamp loginTime;

	@Column(name = "last_update")
	private Timestamp lastUpdate; // 计费更新时间

	@Column(name = "rank", nullable = false)
	private int rank; // 排名

	@Column(name = "checkin", nullable = false)
	private int checkin; // 连续签到

	@Column(name = "cashed", nullable = false)
	private long cashed; // 提现金额

	@Column(name = "lock_times", nullable = false)
	private int lockTimes; // 锁屏次数

	@Column(name = "lock_profit", nullable = false)
	private long lockProfit; // 锁屏收入

	@Column(name = "slash_times", nullable = false)
	private int slashTimes; // 首屏次数

	@Column(name = "slash_profit", nullable = false)
	private long slashProfit; // 首屏收入

	@Column(name = "video_times", nullable = false)
	private int videoTimes; // 视频次数

	@Column(name = "video_profit", nullable = false)
	private long videoProfit; // 视频收入

	@Column(name = "promotion_times", nullable = false)
	private int promotionTimes; // 活动次数

	@Column(name = "promotion_profit", nullable = false)
	private long promotionProfit; // 活动收入

	@Column(name = "recom_times", nullable = false)
	private int recomTimes; // 推荐次数

	@Column(name = "recom_profit", nullable = false)
	private long recomProfit; // 推荐收入

	@Column(name = "reviews", nullable = false)
	private int reviews; // 广告详情查看次数

	@Version
	@Column(name = "version")
	private int version;

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getDevId() {
		return devId;
	}

	public void setDevId(String devId) {
		this.devId = devId;
	}

	public String getBdUserId() {
		return bdUserId;
	}

	public void setBdUserId(String bdUserId) {
		this.bdUserId = bdUserId;
	}

	public Timestamp getLoginTime() {
		return loginTime;
	}

	public void setLoginTime(Timestamp loginTime) {
		this.loginTime = loginTime;
	}

	public Timestamp getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(Timestamp lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	public int getCheckin() {
		return checkin;
	}

	public void setCheckin(int checkin) {
		this.checkin = checkin;
	}

	public long getCashed() {
		return cashed;
	}

	public void setCashed(long cashed) {
		this.cashed = cashed;
	}

	public int getLockTimes() {
		return lockTimes;
	}

	public void setLockTimes(int lockTimes) {
		this.lockTimes = lockTimes;
	}

	public long getLockProfit() {
		return lockProfit;
	}

	public void setLockProfit(long lockProfit) {
		this.lockProfit = lockProfit;
	}

	public int getSlashTimes() {
		return slashTimes;
	}

	public void setSlashTimes(int slashTimes) {
		this.slashTimes = slashTimes;
	}

	public long getSlashProfit() {
		return slashProfit;
	}

	public void setSlashProfit(long slashProfit) {
		this.slashProfit = slashProfit;
	}

	public int getVideoTimes() {
		return videoTimes;
	}

	public void setVideoTimes(int videoTimes) {
		this.videoTimes = videoTimes;
	}

	public long getVideoProfit() {
		return videoProfit;
	}

	public void setVideoProfit(long videoProfit) {
		this.videoProfit = videoProfit;
	}

	public int getPromotionTimes() {
		return promotionTimes;
	}

	public void setPromotionTimes(int promotionTimes) {
		this.promotionTimes = promotionTimes;
	}

	public long getPromotionProfit() {
		return promotionProfit;
	}

	public void setPromotionProfit(long promotionProfit) {
		this.promotionProfit = promotionProfit;
	}

	public int getRecomTimes() {
		return recomTimes;
	}

	public void setRecomTimes(int recomTimes) {
		this.recomTimes = recomTimes;
	}

	public long getRecomProfit() {
		return recomProfit;
	}

	public void setRecomProfit(long recomProfit) {
		this.recomProfit = recomProfit;
	}

	public int getReviews() {
		return reviews;
	}

	public void setReviews(int reviews) {
		this.reviews = reviews;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((bdUserId == null) ? 0 : bdUserId.hashCode());
		result = prime * result + (int) (cashed ^ (cashed >>> 32));
		result = prime * result + checkin;
		result = prime * result + ((devId == null) ? 0 : devId.hashCode());
		result = prime * result + ((lastUpdate == null) ? 0 : lastUpdate.hashCode());
		result = prime * result + (int) (lockProfit ^ (lockProfit >>> 32));
		result = prime * result + lockTimes;
		result = prime * result + ((loginTime == null) ? 0 : loginTime.hashCode());
		result = prime * result + (int) (promotionProfit ^ (promotionProfit >>> 32));
		result = prime * result + promotionTimes;
		result = prime * result + rank;
		result = prime * result + (int) (recomProfit ^ (recomProfit >>> 32));
		result = prime * result + recomTimes;
		result = prime * result + reviews;
		result = prime * result + (int) (slashProfit ^ (slashProfit >>> 32));
		result = prime * result + slashTimes;
		result = prime * result + (int) (userId ^ (userId >>> 32));
		result = prime * result + version;
		result = prime * result + (int) (videoProfit ^ (videoProfit >>> 32));
		result = prime * result + videoTimes;
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
		UserStatus other = (UserStatus) obj;
		if (bdUserId == null) {
			if (other.bdUserId != null)
				return false;
		} else if (!bdUserId.equals(other.bdUserId))
			return false;
		if (cashed != other.cashed)
			return false;
		if (checkin != other.checkin)
			return false;
		if (devId == null) {
			if (other.devId != null)
				return false;
		} else if (!devId.equals(other.devId))
			return false;
		if (lastUpdate == null) {
			if (other.lastUpdate != null)
				return false;
		} else if (!lastUpdate.equals(other.lastUpdate))
			return false;
		if (lockProfit != other.lockProfit)
			return false;
		if (lockTimes != other.lockTimes)
			return false;
		if (loginTime == null) {
			if (other.loginTime != null)
				return false;
		} else if (!loginTime.equals(other.loginTime))
			return false;
		if (promotionProfit != other.promotionProfit)
			return false;
		if (promotionTimes != other.promotionTimes)
			return false;
		if (rank != other.rank)
			return false;
		if (recomProfit != other.recomProfit)
			return false;
		if (recomTimes != other.recomTimes)
			return false;
		if (reviews != other.reviews)
			return false;
		if (slashProfit != other.slashProfit)
			return false;
		if (slashTimes != other.slashTimes)
			return false;
		if (userId != other.userId)
			return false;
		if (version != other.version)
			return false;
		if (videoProfit != other.videoProfit)
			return false;
		if (videoTimes != other.videoTimes)
			return false;
		return true;
	}

	public UserStatus clone() {
		try {
			return (UserStatus) super.clone();
		} catch (CloneNotSupportedException e) {
			return null;
		}
	}

}
