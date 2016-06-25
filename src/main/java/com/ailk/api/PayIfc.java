package com.ailk.api;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import com.ailk.api.UserIfc.Asset;
import com.ailk.api.impl.BaseRequest;
import com.ailk.api.impl.BaseResponse;
import com.ailk.common.ConstVariables;

/**
 * 支付分五种类型： 1. 锁屏 a. 用户发起的支付请求 b. 不进行锁定处理 c. 用户解锁后，直接发起，或者累计到一定值后发起计费处理 d.
 * 如果失败，则直接忽略，对用户有一定影响 2. 首屏 a. 用户发起的支付请求 b. 不进行锁定处理 c.
 * 用户观看后，直接发起，在无网的情况下，保存状态，待有网后发起 d. 如果失败，则直接忽略，对用户无影响。 3. 视频 a. 用户发起的支付请求 b.
 * 需进行锁定处理 c. 用户观看前，需要锁定，一天只锁定一次，有效期一天；在离线下载情况下，则不锁定；锁定过程可能会部分失败，需要重新提交 d.
 * 待播放完毕后，可批量提交，提交的视频可能锁定，也可能未锁定，需要判断决定 e.
 * 如果未锁定，而且在一天中还可能产生收入，则需要预先锁定，保证分次付费的视频可以获得收入 4. 活动 a. 用户发起的支付请求 b. 需进行锁定处理 c.
 * 用户参与活动前，需要锁定，同一个广告，未提交前不允许重复锁定，锁定有效期4小时，不允许离线操作 d. 在有效期内未提交活动结果，系统自动取消 e.
 * 用户也可以主动取消活动，取消后，锁定的费用自动释放 f. 活动产生的收入不立即计算，需要等到审核通过后计算 5. 推荐 a. 系统发起的支付请求 b.
 * 不进行锁定处理 c. 每一个有效的推荐，都会有相应的收入，以批量处理的方式进行，不提供实时方式 d.
 * 被推荐人提现时，推荐人也会获得相应的收入，以批量处理的方式进行，不提供实时方式
 * 
 * @author xugq
 * 
 */
public interface PayIfc {

	public static final String LOCK_AD_ATTR = "PayIfc.lockAd";
	public static final long MILLIS_PER_HOUR = 3600000;
	public static final int VALIDS_PER_HOUR = 3;

	static class LockAdSession {
		private List<Long> timestamps; // 锁屏广告时间戳
		private int minIndex; // 最小时间戳索引

		public LockAdSession() {
			timestamps = new ArrayList<Long>();
			minIndex = -1;
		}

		public List<Long> getTimestamps() {
			return timestamps;
		}

		public void setTimestamps(List<Long> timestamps) {
			this.timestamps = timestamps;
		}

		public int getMinIndex() {
			return minIndex;
		}

		public void setMinIndex(int minIndex) {
			this.minIndex = minIndex;
		}
	}

	static class PayItem {
		private int adId; // 广告ID
		private int seq; // 观看次序
		private long lastCommit; // 观看时间戳，到毫秒

		public int getAdId() {
			return adId;
		}

		public void setAdId(int adId) {
			this.adId = adId;
		}

		public int getSeq() {
			return seq;
		}

		public void setSeq(int seq) {
			this.seq = seq;
		}

		public long getLastCommit() {
			return lastCommit;
		}

		public void setLastCommit(long lastCommit) {
			this.lastCommit = lastCommit;
		}
	}

	static class LockRequest extends BaseRequest {
		private long sysdate; // 系统日期
		private List<Integer> items; // 锁定记录列表

		public long getSysdate() {
			return sysdate;
		}

		public void setSysdate(long sysdate) {
			this.sysdate = sysdate;
		}

		public List<Integer> getItems() {
			return items;
		}

		public void setItems(List<Integer> items) {
			this.items = items;
		}
	}

	static class LockResponse extends BaseResponse {
		private List<Integer> fails; // 失败的广告顺序号
		private List<Short> statuses; // 失败的广告原因

		public List<Integer> getFails() {
			return fails;
		}

		public void setFails(List<Integer> fails) {
			this.fails = fails;
		}

		public List<Short> getStatuses() {
			return statuses;
		}

		public void setStatuses(List<Short> statuses) {
			this.statuses = statuses;
		}
	}

	/**
	 * 锁定费用
	 * 
	 * @param httpSession
	 *            会话
	 * @param request
	 *            请求
	 * @return 加锁状态，异常时返回错误，如果有部分失败，则返回失败列表
	 */
	public LockResponse lock(HttpSession httpSession, LockRequest request);

	static class UnlockRequest extends BaseRequest {
		private List<PayItem> items;

		public List<PayItem> getItems() {
			return items;
		}

		public void setItems(List<PayItem> items) {
			this.items = items;
		}
	}

	/**
	 * 解锁费用
	 * 
	 * @param httpSession
	 *            会话
	 * @param request
	 *            请求
	 * @return 解锁状态
	 */
	public BaseResponse unlock(HttpSession httpSession, UnlockRequest request);

	static class ChargeRequest extends BaseRequest {
		private long sysdate; // 当前日期
		private String devId; // 设备ID
		private Boolean lockDetail; // 是否为解锁详情
		private List<PayItem> items; // 计费记录列表

		public long getSysdate() {
			return sysdate;
		}

		public void setSysdate(long sysdate) {
			this.sysdate = sysdate;
		}

		public String getDevId() {
			return devId;
		}

		public void setDevId(String devId) {
			this.devId = devId;
		}

		public Boolean getLockDetail() {
			return lockDetail;
		}

		public void setLockDetail(Boolean lockDetail) {
			this.lockDetail = lockDetail;
		}

		public List<PayItem> getItems() {
			return items;
		}

		public void setItems(List<PayItem> items) {
			this.items = items;
		}
	}

	static class ChargeResponseItem {
		private short status; // 广告状态
		private long profit; // 收入
		private int times; // 次数，-1表示不需更新
		
		public ChargeResponseItem () {
			status = ConstVariables.STATUS_PASS;
			profit = 0;
			times = -1;
		}

		public short getStatus() {
			return status;
		}

		public void setStatus(short status) {
			this.status = status;
		}

		public long getProfit() {
			return profit;
		}

		public void setProfit(long profit) {
			this.profit = profit;
		}

		public int getTimes() {
			return times;
		}

		public void setTimes(int times) {
			this.times = times;
		}
	}

	static class ChargeResponse extends BaseResponse {
		private List<ChargeResponseItem> items; // 结果应答
		private Asset asset; // 资产

		public List<ChargeResponseItem> getItems() {
			return items;
		}

		public void setItems(List<ChargeResponseItem> items) {
			this.items = items;
		}

		public Asset getAsset() {
			return asset;
		}

		public void setAsset(Asset asset) {
			this.asset = asset;
		}
	}

	/**
	 * 计费，该费用可能已经锁定，也可能未锁定，如果未锁定，则可能需要预先锁定，保证分次付费的视频可在后续观看时能获得收入。涉及到账户余额表、广告余额表、
	 * 用户统计表、广告锁表
	 * 
	 * @param httpSession
	 *            会话
	 * @param request
	 *            请求
	 * @return 是否成功
	 */
	public ChargeResponse charge(HttpSession httpSession, ChargeRequest request);

	static class BalanceResponse extends BaseResponse {
		private long budget; // 预算
		private long remain; // 可用余额
		private long locked; // 锁定余额

		public long getBudget() {
			return budget;
		}

		public void setBudget(long budget) {
			this.budget = budget;
		}

		public long getRemain() {
			return remain;
		}

		public void setRemain(long remain) {
			this.remain = remain;
		}

		public long getLocked() {
			return locked;
		}

		public void setLocked(long locked) {
			this.locked = locked;
		}
	}

	static class CommitPromotionRequest extends BaseRequest {
		private int adId; // 广告ID
		private long actionDate; // 发生时间
		private String investigations; // 调查问卷答案
		private String answers; // 每次必填答案
		private boolean complete; // 是否完整

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

		public boolean isComplete() {
			return complete;
		}

		public void setComplete(boolean complete) {
			this.complete = complete;
		}
	}

	static class CommitPromotionResponse extends BaseResponse {
		private long id; // 活动日志ID
		private short status; // 状态
		private String reason; // 拒绝原因

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

		public String getReason() {
			return reason;
		}

		public void setReason(String reason) {
			this.reason = reason;
		}
	}

	/**
	 * 提交活动
	 * 
	 * @param httpSession
	 *            会话
	 * @param request
	 *            请求
	 * @return 是否成功
	 */
	public CommitPromotionResponse commitPromotion(HttpSession httpSession, CommitPromotionRequest request);

	/**
	 * 获取账户余额
	 * 
	 * @param adId
	 *            广告ID
	 * @return 是否成功
	 */
	public BalanceResponse getBalance(int adId);

	static class RqstPayRequest extends BaseRequest {
		private String password; // 密码
		private int payType; // 支付方式
		private String payName; // 账号名称
		private String payId; // 账号
		private int amount; // 金额

		public String getPassword() {
			return password;
		}

		public void setPassword(String password) {
			this.password = password;
		}

		public int getPayType() {
			return payType;
		}

		public void setPayType(int payType) {
			this.payType = payType;
		}

		public String getPayName() {
			return payName;
		}

		public void setPayName(String payName) {
			this.payName = payName;
		}

		public String getPayId() {
			return payId;
		}

		public void setPayId(String payId) {
			this.payId = payId;
		}

		public int getAmount() {
			return amount;
		}

		public void setAmount(int amount) {
			this.amount = amount;
		}
	}

	static class RqstPayResponse extends BaseResponse {
		private Asset asset; // 资产

		public Asset getAsset() {
			return asset;
		}

		public void setAsset(Asset asset) {
			this.asset = asset;
		}
	}

	/**
	 * 请求提现
	 * 
	 * @param httpSession
	 *            会话
	 * @param request
	 *            请求
	 * @return 是否成功
	 */
	public RqstPayResponse rqstPay(HttpSession httpSession, RqstPayRequest request);

	static class GetPayRequest extends BaseRequest {
		private long maxId; // 最大ID

		public long getMaxId() {
			return maxId;
		}

		public void setMaxId(long maxId) {
			this.maxId = maxId;
		}
	}

	static class GetPayItem {
		private long id; // ID
		private int payType; // 支付类型
		private int amount; // 金额
		private short status; // 状态
		private String reason; // 原因
		private long rqstDate; // 请求日期

		public long getId() {
			return id;
		}

		public void setId(long id) {
			this.id = id;
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

		public long getRqstDate() {
			return rqstDate;
		}

		public void setRqstDate(long rqstDate) {
			this.rqstDate = rqstDate;
		}
	}

	static class GetPayResponse extends BaseResponse {
		private boolean end; // 是否结束
		private List<GetPayItem> items; // 支付列表

		public boolean isEnd() {
			return end;
		}

		public void setEnd(boolean end) {
			this.end = end;
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

}
