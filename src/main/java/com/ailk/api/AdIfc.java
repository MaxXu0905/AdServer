package com.ailk.api;

import java.util.List;

import javax.servlet.http.HttpSession;

import com.ailk.api.impl.BaseRequest;
import com.ailk.api.impl.BaseResponse;
import com.ailk.common.ConstVariables;
import com.ailk.jdbc.AdParticipantManager;
import com.ailk.jdbc.cache.InfoAdValue;
import com.ailk.jdbc.cache.InfoAdValue.PriceDayItem;
import com.ailk.jdbc.cache.InfoAdValue.PriceItem;
import com.ailk.jdbc.cache.InfoQuizValue;

public interface AdIfc {

	public static final String AD_IDS_ATTR = "AdIfc.AdIds";
	public static final int FLAG_PROMOTION_SHOW_RECOMMEND = 0x1; // 显示推荐活动
	public static final int FLAG_PROMOTION = FLAG_PROMOTION_SHOW_RECOMMEND;

	static class GetAdsRequest extends BaseRequest {
		private long sysdate; // 系统日期
		private short adStyle; // 广告形式
		private short batchId; // 批次ID

		public long getSysdate() {
			return sysdate;
		}

		public void setSysdate(long sysdate) {
			this.sysdate = sysdate;
		}

		public short getAdStyle() {
			return adStyle;
		}

		public void setAdStyle(short adStyle) {
			this.adStyle = adStyle;
		}

		public short getBatchId() {
			return batchId;
		}

		public void setBatchId(short batchId) {
			this.batchId = batchId;
		}
	}

	/**
	 * 单个返回的广告详情
	 * 
	 * @author xugq
	 * 
	 */
	static class InfoAdItem {
		private int adId; // 广告ID
		private String adName; // 广告名称
		private String adDesc; // 广告描述
		private short adType; // 广告类型
		private String officialUrl; // 官网
		private long effDate; // 生效日期
		private long expDate; // 失效日期
		private String video; // 视频URL
		private String image; // 图片URL
		private String webView; // web view URL
		private Short duration; // 时长
		private List<InfoQuizValue> quizValues; // 问题列表
		private List<PriceItem> prices; // 价格列表
		private int times; // 参与次数
		private String cycles; // 审核周期
		private Integer participant; // 参与人数

		public void set(int adId, InfoAdValue value, int day, int times) {
			this.adId = adId;
			adName = value.getAdName();
			adDesc = value.getAdDesc();
			adType = value.getAdType();
			officialUrl = value.getOfficialUrl();
			effDate = value.getEffDate();
			expDate = value.getExpDate();
			video = value.getVideo();
			image = value.getImage();
			webView = value.getWebView();
			duration = value.getDuration();
			quizValues = value.getQuizValues();

			PriceDayItem priceDayItem = value.getPriceDayItem(day);
			if (priceDayItem != null)
				prices = value.getPriceDayItem(day).getItems();
			else
				prices = null;
			this.times = times;
			cycles = value.getCycles();

			// 如果是活动，则需要提供参与人数
			if (value.getAdStyle() == ConstVariables.AD_STYLE_PROMOTION) {
				AdParticipantManager adParticipantManager = AdParticipantManager.getInstance();
				participant = adParticipantManager.getParticipants(adId);
			}
		}

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

		public String getOfficialUrl() {
			return officialUrl;
		}

		public void setOfficialUrl(String officialUrl) {
			this.officialUrl = officialUrl;
		}

		public long getEffDate() {
			return effDate;
		}

		public void setEffDate(long effDate) {
			this.effDate = effDate;
		}

		public long getExpDate() {
			return expDate;
		}

		public void setExpDate(long expDate) {
			this.expDate = expDate;
		}

		public String getVideo() {
			return video;
		}

		public void setVideo(String video) {
			this.video = video;
		}

		public String getImage() {
			return image;
		}

		public void setImage(String image) {
			this.image = image;
		}

		public String getWebView() {
			return webView;
		}

		public void setWebView(String webView) {
			this.webView = webView;
		}

		public Short getDuration() {
			return duration;
		}

		public void setDuration(Short duration) {
			this.duration = duration;
		}

		public List<InfoQuizValue> getQuizValues() {
			return quizValues;
		}

		public void setQuizValues(List<InfoQuizValue> quizValues) {
			this.quizValues = quizValues;
		}

		public List<PriceItem> getPrices() {
			return prices;
		}

		public void setPrices(List<PriceItem> prices) {
			this.prices = prices;
		}

		public int getTimes() {
			return times;
		}

		public void setTimes(int times) {
			this.times = times;
		}

		public String getCycles() {
			return cycles;
		}

		public void setCycles(String cycles) {
			this.cycles = cycles;
		}

		public Integer getParticipant() {
			return participant;
		}

		public void setParticipant(Integer participant) {
			this.participant = participant;
		}
	}

	static class GetAdsResponse extends BaseResponse {
		private Integer flags; // 标记
		private long sysdate; // 系统日期
		private boolean end; // 是否结束
		private List<InfoAdItem> items; // 广告详情列表

		public Integer getFlags() {
			return flags;
		}

		public void setFlags(Integer flags) {
			this.flags = flags;
		}

		public long getSysdate() {
			return sysdate;
		}

		public void setSysdate(long sysdate) {
			this.sysdate = sysdate;
		}

		public boolean isEnd() {
			return end;
		}

		public void setEnd(boolean end) {
			this.end = end;
		}

		public List<InfoAdItem> getItems() {
			return items;
		}

		public void setItems(List<InfoAdItem> items) {
			this.items = items;
		}
	}

	/**
	 * 获取广告信息列表
	 * 
	 * @param httpSession
	 *            会话
	 * @param request
	 *            请求
	 * @return 广告信息列表
	 */
	public GetAdsResponse getAds(HttpSession httpSession, GetAdsRequest request);

	/**
	 * 获取首屏广告信息列表
	 * 
	 * @param request
	 *            请求
	 * @return 广告信息列表
	 */
	public GetAdsResponse getSplashAds(GetAdsRequest request);

	static class GetPromotionsRequest extends BaseRequest {
		private long maxId; // 最大ID
		private short status; // 状态

		public long getMaxId() {
			return maxId;
		}

		public void setMaxId(long maxId) {
			this.maxId = maxId;
		}

		public short getStatus() {
			return status;
		}

		public void setStatus(short status) {
			this.status = status;
		}
	}

	static class GetPromotionsItem {
		private long id; // 唯一ID
		private int adId; // 广告ID
		private long actionDate; // 提交日期
		private String cycles; // 审核周期
		private String title; // 标题
		private Long profit; // 金额
		private String reason; // 拒绝原因
		private long lastUpdate; // 时间戳

		public long getId() {
			return id;
		}

		public void setId(long id) {
			this.id = id;
		}

		public int getAdId() {
			return adId;
		}

		public void setAdId(int adId) {
			this.adId = adId;
		}

		public long getActionDate() {
			return actionDate;
		}

		public void setActionDate(long actionDate) {
			this.actionDate = actionDate;
		}

		public String getCycles() {
			return cycles;
		}

		public void setCycles(String cycles) {
			this.cycles = cycles;
		}

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public Long getProfit() {
			return profit;
		}

		public void setProfit(Long profit) {
			this.profit = profit;
		}

		public String getReason() {
			return reason;
		}

		public void setReason(String reason) {
			this.reason = reason;
		}

		public long getLastUpdate() {
			return lastUpdate;
		}

		public void setLastUpdate(long lastUpdate) {
			this.lastUpdate = lastUpdate;
		}
	}

	static class GetPromotionsResponse extends BaseResponse {
		private boolean end; // 是否结束
		private List<GetPromotionsItem> items; // 参与活动列表

		public boolean isEnd() {
			return end;
		}

		public void setEnd(boolean end) {
			this.end = end;
		}

		public List<GetPromotionsItem> getItems() {
			return items;
		}

		public void setItems(List<GetPromotionsItem> items) {
			this.items = items;
		}
	}

	/**
	 * 获取参与活动的情况
	 * 
	 * @param httpSession
	 *            会话
	 * @param request
	 *            请求
	 * @return 参与活动的情况
	 */
	public GetPromotionsResponse getPromotions(HttpSession httpSession, GetPromotionsRequest request);

}
