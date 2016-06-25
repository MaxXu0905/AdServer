package com.ailk.api;

import java.util.List;

import javax.servlet.http.HttpSession;

import com.ailk.api.impl.BaseRequest;
import com.ailk.api.impl.BaseResponse;
import com.ailk.jdbc.cache.InfoAdExtValue;
import com.ailk.jdbc.cache.InfoQuizValue;
import com.ailk.jdbc.entity.InfoAd;
import com.ailk.jdbc.entity.InfoQuiz;

public interface CheckIfc {

	static class SaveAdRequest extends BaseRequest {
		private InfoAd infoAd;
		private List<InfoQuiz> quizList;
		private List<InfoAdExtValue> extValues;

		public InfoAd getInfoAd() {
			return infoAd;
		}

		public void setInfoAd(InfoAd infoAd) {
			this.infoAd = infoAd;
		}

		public List<InfoQuiz> getQuizList() {
			return quizList;
		}

		public void setQuizList(List<InfoQuiz> quizList) {
			this.quizList = quizList;
		}

		public List<InfoAdExtValue> getExtValues() {
			return extValues;
		}

		public void setExtValues(List<InfoAdExtValue> extValues) {
			this.extValues = extValues;
		}
	}

	/**
	 * 添加广告
	 * 
	 * @param httpSession
	 *            会话
	 * @param entity
	 *            广告信息
	 * @return
	 */
	public BaseResponse saveAd(HttpSession httpSession, SaveAdRequest request);

	static class GetAdsRequest extends BaseRequest {
		private Integer adId;
		private short adStyle;
		private String adName;

		public Integer getAdId() {
			return adId;
		}

		public void setAdId(Integer adId) {
			this.adId = adId;
		}

		public short getAdStyle() {
			return adStyle;
		}

		public void setAdStyle(short adStyle) {
			this.adStyle = adStyle;
		}

		public String getAdName() {
			return adName;
		}

		public void setAdName(String adName) {
			this.adName = adName;
		}
	}

	static class GetAdsItem {
		private int adId;
		private String adName;
		private String webView;

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

		public String getWebView() {
			return webView;
		}

		public void setWebView(String webView) {
			this.webView = webView;
		}
	}

	static class GetAdsResponse extends BaseResponse {
		private String ossBaseUrl; // OSS基础URL
		private List<GetAdsItem> items;

		public GetAdsResponse() {
			ossBaseUrl = BaseResponse.OSS_BASE_URL;
		}

		public String getOssBaseUrl() {
			return ossBaseUrl;
		}

		public void setOssBaseUrl(String ossBaseUrl) {
			this.ossBaseUrl = ossBaseUrl;
		}

		public List<GetAdsItem> getItems() {
			return items;
		}

		public void setItems(List<GetAdsItem> items) {
			this.items = items;
		}
	}

	/**
	 * 获取广告列表
	 * 
	 * @param httpSession
	 *            会话
	 * @param request
	 *            请求
	 * @return 广告列表
	 */
	public GetAdsResponse getAds(HttpSession httpSession, GetAdsRequest request);

	static class GetChecksRequest extends BaseRequest {
		private List<Long> saver;
		private int pageSize;
		private String userName;
		private int adId;
		private Short status;

		public List<Long> getSaver() {
			return saver;
		}

		public void setSaver(List<Long> saver) {
			this.saver = saver;
		}

		public int getPageSize() {
			return pageSize;
		}

		public void setPageSize(int pageSize) {
			this.pageSize = pageSize;
		}

		public String getUserName() {
			return userName;
		}

		public void setUserName(String userName) {
			this.userName = userName;
		}

		public int getAdId() {
			return adId;
		}

		public void setAdId(int adId) {
			this.adId = adId;
		}

		public Short getStatus() {
			return status;
		}

		public void setStatus(Short status) {
			this.status = status;
		}
	}

	static class GetChecksItem {
		private String userName;
		private long id;
		private int adId;
		private short status;
		private long lastUpdate;
		private String investigations;
		private String answers;
		private String reason;
		private long profit;

		public String getUserName() {
			return userName;
		}

		public void setUserName(String userName) {
			this.userName = userName;
		}

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

		public short getStatus() {
			return status;
		}

		public void setStatus(short status) {
			this.status = status;
		}

		public long getLastUpdate() {
			return lastUpdate;
		}

		public void setLastUpdate(long lastUpdate) {
			this.lastUpdate = lastUpdate;
		}

		public String getInvestigations() {
			return investigations;
		}

		public void setInvestigations(String investigations) {
			this.investigations = investigations;
		}

		public String getAnswers() {
			return answers;
		}

		public void setAnswers(String answers) {
			this.answers = answers;
		}

		public String getReason() {
			return reason;
		}

		public void setReason(String reason) {
			this.reason = reason;
		}

		public long getProfit() {
			return profit;
		}

		public void setProfit(long profit) {
			this.profit = profit;
		}
	}

	static class GetChecksResponse extends BaseResponse {
		private List<Long> saver;
		private List<InfoQuizValue> quizValues;
		private List<GetChecksItem> items;

		public List<Long> getSaver() {
			return saver;
		}

		public void setSaver(List<Long> saver) {
			this.saver = saver;
		}

		public List<InfoQuizValue> getQuizValues() {
			return quizValues;
		}

		public void setQuizValues(List<InfoQuizValue> quizValues) {
			this.quizValues = quizValues;
		}

		public List<GetChecksItem> getItems() {
			return items;
		}

		public void setItems(List<GetChecksItem> items) {
			this.items = items;
		}
	}

	/**
	 * 获取审核列表
	 * 
	 * @param httpSession
	 *            会话
	 * @param request
	 *            请求
	 * @return 审核列表
	 */
	public GetChecksResponse getChecks(HttpSession httpSession, GetChecksRequest request);

	class AcceptPromotionRequest extends BaseRequest {
		long id; // 唯一标志

		public long getId() {
			return id;
		}

		public void setId(long id) {
			this.id = id;
		}
	}

	/**
	 * 活动审批通过
	 * 
	 * @param httpSession
	 *            会话
	 * @param request
	 *            请求
	 * @return 是否成功
	 */
	public BaseResponse acceptPromotion(HttpSession httpSession, AcceptPromotionRequest request);

	class RejectPromotionRequest extends BaseRequest {
		long id; // 唯一标志
		String reason; // 拒绝原因

		public long getId() {
			return id;
		}

		public void setId(long id) {
			this.id = id;
		}

		public String getReason() {
			return reason;
		}

		public void setReason(String reason) {
			this.reason = reason;
		}
	}

	/**
	 * 活动审批拒绝
	 * 
	 * @param httpSession
	 *            会话
	 * @param request
	 *            请求
	 * @return 是否成功
	 */
	public BaseResponse rejectPromotion(HttpSession httpSession, RejectPromotionRequest request);

	static class GetPayRequest extends BaseRequest {
		private List<Long> saver;
		private int pageSize;
		private short payType;
		private String userName;
		private short status;

		public List<Long> getSaver() {
			return saver;
		}

		public void setSaver(List<Long> saver) {
			this.saver = saver;
		}

		public int getPageSize() {
			return pageSize;
		}

		public void setPageSize(int pageSize) {
			this.pageSize = pageSize;
		}

		public short getPayType() {
			return payType;
		}

		public void setPayType(short payType) {
			this.payType = payType;
		}

		public String getUserName() {
			return userName;
		}

		public void setUserName(String userName) {
			this.userName = userName;
		}

		public short getStatus() {
			return status;
		}

		public void setStatus(short status) {
			this.status = status;
		}
	}

	static class GetPayItem {
		private long id; // ID
		private String userName; // 用户名
		private int payType; // 支付类型
		private int amount; // 金额
		private short status; // 状态
		private String reason; // 原因
		private long lastUpdate; // 请求日期

		public long getId() {
			return id;
		}

		public void setId(long id) {
			this.id = id;
		}

		public String getUserName() {
			return userName;
		}

		public void setUserName(String userName) {
			this.userName = userName;
		}

		public int getPayType() {
			return payType;
		}

		public void setPayType(int payType) {
			this.payType = payType;
		}

		public int getAmount() {
			return amount;
		}

		public void setAmount(int amount) {
			this.amount = amount;
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

		public long getLastUpdate() {
			return lastUpdate;
		}

		public void setLastUpdate(long lastUpdate) {
			this.lastUpdate = lastUpdate;
		}
	}

	static class GetPayResponse extends BaseResponse {
		private List<Long> saver;
		private List<GetPayItem> items; // 支付列表

		public List<Long> getSaver() {
			return saver;
		}

		public void setSaver(List<Long> saver) {
			this.saver = saver;
		}

		public List<GetPayItem> getItems() {
			return items;
		}

		public void setItems(List<GetPayItem> items) {
			this.items = items;
		}
	}

	/**
	 * 获取支付请求
	 * 
	 * @param httpSession
	 *            会话
	 * @param request
	 *            请求
	 * @return 支付请求数据
	 */
	public GetPayResponse getPayRqst(HttpSession httpSession, GetPayRequest request);

	static class AcceptPayRequest extends BaseRequest {
		private long id; // 唯一标志

		public long getId() {
			return id;
		}

		public void setId(long id) {
			this.id = id;
		}
	}

	public BaseResponse acceptPayRqst(HttpSession httpSession, AcceptPayRequest request);

	static class RejectPayRequest extends BaseRequest {
		private long id; // 唯一标志
		private String reason; // 拒绝原因

		public long getId() {
			return id;
		}

		public void setId(long id) {
			this.id = id;
		}

		public String getReason() {
			return reason;
		}

		public void setReason(String reason) {
			this.reason = reason;
		}
	}

	public BaseResponse rejectPayRqst(HttpSession httpSession, RejectPayRequest request);

	static class GetFeedbackRequest extends BaseRequest {
		private List<Long> saver;
		private int pageSize;
		private short status;

		public List<Long> getSaver() {
			return saver;
		}

		public void setSaver(List<Long> saver) {
			this.saver = saver;
		}

		public int getPageSize() {
			return pageSize;
		}

		public void setPageSize(int pageSize) {
			this.pageSize = pageSize;
		}

		public short getStatus() {
			return status;
		}

		public void setStatus(short status) {
			this.status = status;
		}
	}

	static class GetFeedbackItem {
		private String userName;
		private long id;
		private String content;
		private long actionDate;

		public String getUserName() {
			return userName;
		}

		public void setUserName(String userName) {
			this.userName = userName;
		}

		public long getId() {
			return id;
		}

		public void setId(long id) {
			this.id = id;
		}

		public String getContent() {
			return content;
		}

		public void setContent(String content) {
			this.content = content;
		}

		public long getActionDate() {
			return actionDate;
		}

		public void setActionDate(long actionDate) {
			this.actionDate = actionDate;
		}
	}

	static class GetFeedbackResponse extends BaseResponse {
		private List<Long> saver;
		private List<GetFeedbackItem> items;

		public List<Long> getSaver() {
			return saver;
		}

		public void setSaver(List<Long> saver) {
			this.saver = saver;
		}

		public List<GetFeedbackItem> getItems() {
			return items;
		}

		public void setItems(List<GetFeedbackItem> items) {
			this.items = items;
		}
	}

	public GetFeedbackResponse getFeedback(HttpSession httpSession, GetFeedbackRequest request);

	static class HandleFeedbackRequest extends BaseRequest {
		private long id; // 唯一标志
		private short status; // 状态
		private int level; // 奖励等级

		public long getId() {
			return id;
		}

		public void setId(long id) {
			this.id = id;
		}

		public short getStatus() {
			return status;
		}

		public void setStatus(short status) {
			this.status = status;
		}

		public int getLevel() {
			return level;
		}

		public void setLevel(int level) {
			this.level = level;
		}
	}

	/**
	 * 处理反馈
	 * 
	 * @param request
	 *            请求
	 * @return 是否成功
	 */
	public BaseResponse handleFeedback(HttpSession httpSession, HandleFeedbackRequest request);

}
