package com.ailk.jdbc.entity;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@SuppressWarnings("serial")
@Entity
@Table(name = "info_ad")
public class InfoAd implements Serializable, Cloneable {

	@Id
	@GeneratedValue
	@Column(name = "ad_id")
	private int adId;

	@Column(name = "ad_name", nullable = false)
	private String adName;

	@Column(name = "ad_desc", nullable = false)
	private String adDesc;

	@Column(name = "ad_type", nullable = false)
	private short adType;

	@Column(name = "ad_style", nullable = false)
	private short adStyle;

	@Column(name = "delivery_type", nullable = false)
	private short deliveryType;

	@Column(name = "official_url")
	private String officialUrl;

	@Column(name = "recyclable")
	private Boolean recyclable;

	@Column(name = "price_plan")
	private String pricePlan;

	@Column(name = "budget", nullable = false)
	private long budget;

	@Column(name = "duration")
	private Short duration;

	@Column(name = "quiz_list")
	private String quizList;

	@Column(name = "cust_id", nullable = false)
	private long custId;

	@Column(name = "status", nullable = false)
	private short status;

	@Column(name = "reason")
	private String reason;

	@Column(name = "eff_date", nullable = false)
	private Timestamp effDate;

	@Column(name = "exp_date", nullable = false)
	private Timestamp expDate;

	@Column(name = "video")
	private String video;

	@Column(name = "web_view")
	private String webView;

	@Column(name = "image")
	private String image;

	@Column(name = "cycles")
	private String cycles; // 审核周期

	@Column(name = "expire")
	private Long expire; // 申请后超时

	@Column(name = "oper_id", nullable = false)
	private long operId;

	@Column(name = "oper_date", nullable = false)
	private Timestamp operDate;

	public int getAdId() {
		return adId;
	}

	public void setAdId(int adId) {
		this.adId = adId;
	}

	public String getAdName() {
		return adName;
	}

	public void setAdName(String adName) {
		this.adName = adName;
	}

	public String getAdDesc() {
		return adDesc;
	}

	public void setAdDesc(String adDesc) {
		this.adDesc = adDesc;
	}

	public short getAdType() {
		return adType;
	}

	public void setAdType(short adType) {
		this.adType = adType;
	}

	public short getAdStyle() {
		return adStyle;
	}

	public void setAdStyle(short adStyle) {
		this.adStyle = adStyle;
	}

	public short getDeliveryType() {
		return deliveryType;
	}

	public void setDeliveryType(short deliveryType) {
		this.deliveryType = deliveryType;
	}

	public String getOfficialUrl() {
		return officialUrl;
	}

	public void setOfficialUrl(String officialUrl) {
		this.officialUrl = officialUrl;
	}

	public Boolean getRecyclable() {
		return recyclable;
	}

	public void setRecyclable(Boolean recyclable) {
		this.recyclable = recyclable;
	}

	public String getPricePlan() {
		return pricePlan;
	}

	public void setPricePlan(String pricePlan) {
		this.pricePlan = pricePlan;
	}

	public long getBudget() {
		return budget;
	}

	public void setBudget(long budget) {
		this.budget = budget;
	}

	public Short getDuration() {
		return duration;
	}

	public void setDuration(Short duration) {
		this.duration = duration;
	}

	public String getQuizList() {
		return quizList;
	}

	public void setQuizList(String quizList) {
		this.quizList = quizList;
	}

	public long getCustId() {
		return custId;
	}

	public void setCustId(long custId) {
		this.custId = custId;
	}

	public short getStatus() {
		return status;
	}

	public void setStatus(short status) {
		this.status = status;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public Timestamp getEffDate() {
		return effDate;
	}

	public void setEffDate(Timestamp effDate) {
		this.effDate = effDate;
	}

	public Timestamp getExpDate() {
		return expDate;
	}

	public void setExpDate(Timestamp expDate) {
		this.expDate = expDate;
	}

	public String getVideo() {
		return video;
	}

	public void setVideo(String video) {
		this.video = video;
	}

	public String getWebView() {
		return webView;
	}

	public void setWebView(String webView) {
		this.webView = webView;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getCycles() {
		return cycles;
	}

	public void setCycles(String cycles) {
		this.cycles = cycles;
	}

	public Long getExpire() {
		return expire;
	}

	public void setExpire(Long expire) {
		this.expire = expire;
	}

	public long getOperId() {
		return operId;
	}

	public void setOperId(long operId) {
		this.operId = operId;
	}

	public Timestamp getOperDate() {
		return operDate;
	}

	public void setOperDate(Timestamp operDate) {
		this.operDate = operDate;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((adDesc == null) ? 0 : adDesc.hashCode());
		result = prime * result + adId;
		result = prime * result + ((adName == null) ? 0 : adName.hashCode());
		result = prime * result + adStyle;
		result = prime * result + adType;
		result = prime * result + (int) (budget ^ (budget >>> 32));
		result = prime * result + (int) (custId ^ (custId >>> 32));
		result = prime * result + ((cycles == null) ? 0 : cycles.hashCode());
		result = prime * result + deliveryType;
		result = prime * result + ((duration == null) ? 0 : duration.hashCode());
		result = prime * result + ((effDate == null) ? 0 : effDate.hashCode());
		result = prime * result + ((expDate == null) ? 0 : expDate.hashCode());
		result = prime * result + ((expire == null) ? 0 : expire.hashCode());
		result = prime * result + ((image == null) ? 0 : image.hashCode());
		result = prime * result + ((officialUrl == null) ? 0 : officialUrl.hashCode());
		result = prime * result + ((operDate == null) ? 0 : operDate.hashCode());
		result = prime * result + (int) (operId ^ (operId >>> 32));
		result = prime * result + ((pricePlan == null) ? 0 : pricePlan.hashCode());
		result = prime * result + ((quizList == null) ? 0 : quizList.hashCode());
		result = prime * result + ((reason == null) ? 0 : reason.hashCode());
		result = prime * result + ((recyclable == null) ? 0 : recyclable.hashCode());
		result = prime * result + status;
		result = prime * result + ((video == null) ? 0 : video.hashCode());
		result = prime * result + ((webView == null) ? 0 : webView.hashCode());
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
		InfoAd other = (InfoAd) obj;
		if (adDesc == null) {
			if (other.adDesc != null)
				return false;
		} else if (!adDesc.equals(other.adDesc))
			return false;
		if (adId != other.adId)
			return false;
		if (adName == null) {
			if (other.adName != null)
				return false;
		} else if (!adName.equals(other.adName))
			return false;
		if (adStyle != other.adStyle)
			return false;
		if (adType != other.adType)
			return false;
		if (budget != other.budget)
			return false;
		if (custId != other.custId)
			return false;
		if (cycles == null) {
			if (other.cycles != null)
				return false;
		} else if (!cycles.equals(other.cycles))
			return false;
		if (deliveryType != other.deliveryType)
			return false;
		if (duration == null) {
			if (other.duration != null)
				return false;
		} else if (!duration.equals(other.duration))
			return false;
		if (effDate == null) {
			if (other.effDate != null)
				return false;
		} else if (!effDate.equals(other.effDate))
			return false;
		if (expDate == null) {
			if (other.expDate != null)
				return false;
		} else if (!expDate.equals(other.expDate))
			return false;
		if (expire == null) {
			if (other.expire != null)
				return false;
		} else if (!expire.equals(other.expire))
			return false;
		if (image == null) {
			if (other.image != null)
				return false;
		} else if (!image.equals(other.image))
			return false;
		if (officialUrl == null) {
			if (other.officialUrl != null)
				return false;
		} else if (!officialUrl.equals(other.officialUrl))
			return false;
		if (operDate == null) {
			if (other.operDate != null)
				return false;
		} else if (!operDate.equals(other.operDate))
			return false;
		if (operId != other.operId)
			return false;
		if (pricePlan == null) {
			if (other.pricePlan != null)
				return false;
		} else if (!pricePlan.equals(other.pricePlan))
			return false;
		if (quizList == null) {
			if (other.quizList != null)
				return false;
		} else if (!quizList.equals(other.quizList))
			return false;
		if (reason == null) {
			if (other.reason != null)
				return false;
		} else if (!reason.equals(other.reason))
			return false;
		if (recyclable == null) {
			if (other.recyclable != null)
				return false;
		} else if (!recyclable.equals(other.recyclable))
			return false;
		if (status != other.status)
			return false;
		if (video == null) {
			if (other.video != null)
				return false;
		} else if (!video.equals(other.video))
			return false;
		if (webView == null) {
			if (other.webView != null)
				return false;
		} else if (!webView.equals(other.webView))
			return false;
		return true;
	}

	@Override
	public InfoAd clone() {
		try {
			return (InfoAd) super.clone();
		} catch (CloneNotSupportedException e) {
			return null;
		}
	}

}
