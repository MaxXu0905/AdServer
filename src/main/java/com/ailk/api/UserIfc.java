package com.ailk.api;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import com.ailk.api.impl.BaseRequest;
import com.ailk.api.impl.BaseResponse;
import com.ailk.jdbc.cache.RankBoardValue;
import com.ailk.jdbc.entity.InfoUser;
import com.ailk.jdbc.entity.UserStatus;

public interface UserIfc {

	public static final String USER_STAT_ATTR = "UserIfc.userStat";
	public static final String VERIFY_CODE_ATTR = "UserIfc.verifyCode";

	static class VerifyCodeSession {
		private String userName; // 用户名
		private String verifyCode; // 验证码

		public String getUserName() {
			return userName;
		}

		public void setUserName(String userName) {
			this.userName = userName;
		}

		public String getVerifyCode() {
			return verifyCode;
		}

		public void setVerifyCode(String verifyCode) {
			this.verifyCode = verifyCode;
		}
	}

	static class AdItem {
		private int adId; // 广告ID
		private int days; // 已经观看天数
		private int times; // 已经观看次数

		public int getAdId() {
			return adId;
		}

		public void setAdId(int adId) {
			this.adId = adId;
		}

		public int getDays() {
			return days;
		}

		public void setDays(int days) {
			this.days = days;
		}

		public int getTimes() {
			return times;
		}

		public void setTimes(int times) {
			this.times = times;
		}
	}

	static class UserStatSession {
		long userId; // 用户ID
		String devId; // 设备号
		long sysdate; // 会话建立时的当前日期
		Map<Short, List<AdItem>> adMap; // 广告列表映射

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

		public long getSysdate() {
			return sysdate;
		}

		public void setSysdate(long sysdate) {
			this.sysdate = sysdate;
		}

		public Map<Short, List<AdItem>> getAdMap() {
			return adMap;
		}

		public void setAdMap(Map<Short, List<AdItem>> adMap) {
			this.adMap = adMap;
		}
	}

	static class ExistsRequest extends BaseRequest {
		private String userName; // 用户名
		private boolean registered; // 是否主动注册

		public String getUserName() {
			return userName;
		}

		public void setUserName(String userName) {
			this.userName = userName;
		}

		public boolean isRegistered() {
			return registered;
		}

		public void setRegistered(boolean registered) {
			this.registered = registered;
		}
	}

	/**
	 * 检查用户是否存在
	 * 
	 * @param request
	 *            请求
	 * @return 存在则返回正确，否则失败
	 */
	public BaseResponse exists(ExistsRequest request);

	static class RegisterRequest extends BaseRequest {
		private String verifyCode; // 验证码
		private String bdUserId; // 百度用户ID
		private InfoUser infoUser; // 用户信息

		public String getVerifyCode() {
			return verifyCode;
		}

		public void setVerifyCode(String verifyCode) {
			this.verifyCode = verifyCode;
		}

		public String getBdUserId() {
			return bdUserId;
		}

		public void setBdUserId(String bdUserId) {
			this.bdUserId = bdUserId;
		}

		public InfoUser getInfoUser() {
			return infoUser;
		}

		public void setInfoUser(InfoUser infoUser) {
			this.infoUser = infoUser;
		}
	}

	static class RegisterResponse extends BaseResponse {
		private long userId; // 用户ID
		private String userName; // 用户名
		private boolean registered; // 是否为注册用户
		private String password; // 密码
		private String nickName; // 昵称
		private String portrait; // 头像
		private Short gender; // 性别
		private int level; // 等级
		private int maxVideos; // 最大可观看视频数
		private String inviteCode; // 邀请码
		private Asset asset; // 资产

		public long getUserId() {
			return userId;
		}

		public void setUserId(long userId) {
			this.userId = userId;
		}

		public String getUserName() {
			return userName;
		}

		public void setUserName(String userName) {
			this.userName = userName;
		}

		public boolean isRegistered() {
			return registered;
		}

		public void setRegistered(boolean registered) {
			this.registered = registered;
		}

		public String getPassword() {
			return password;
		}

		public void setPassword(String password) {
			this.password = password;
		}

		public String getNickName() {
			return nickName;
		}

		public void setNickName(String nickName) {
			this.nickName = nickName;
		}

		public String getPortrait() {
			return portrait;
		}

		public void setPortrait(String portrait) {
			this.portrait = portrait;
		}

		public Short getGender() {
			return gender;
		}

		public void setGender(Short gender) {
			this.gender = gender;
		}

		public int getLevel() {
			return level;
		}

		public void setLevel(int level) {
			this.level = level;
		}

		public int getMaxVideos() {
			return maxVideos;
		}

		public void setMaxVideos(int maxVideos) {
			this.maxVideos = maxVideos;
		}

		public String getInviteCode() {
			return inviteCode;
		}

		public void setInviteCode(String inviteCode) {
			this.inviteCode = inviteCode;
		}

		public Asset getAsset() {
			return asset;
		}

		public void setAsset(Asset asset) {
			this.asset = asset;
		}
	}

	/**
	 * 用户注册
	 * 
	 * @param httpSession
	 *            会话
	 * @param request
	 *            请求
	 * @return 注册成功，返回用户ID
	 */
	public RegisterResponse register(HttpSession httpSession, RegisterRequest request);

	static class LoginRequest extends BaseRequest {
		private String userName; // 用户名
		private boolean registered; // 是否为注册用户
		private String password; // 密码
		private String devId; // 设备ID
		private String bdUserId; // 百度用户ID

		public String getUserName() {
			return userName;
		}

		public void setUserName(String userName) {
			this.userName = userName;
		}

		public boolean isRegistered() {
			return registered;
		}

		public void setRegistered(boolean registered) {
			this.registered = registered;
		}

		public String getPassword() {
			return password;
		}

		public void setPassword(String password) {
			this.password = password;
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
	}

	static class LoginResponse extends BaseResponse {
		private long userId; // 用户ID
		private String userName; // 用户名
		private boolean registered; // 是否注册
		private String password; // 密码
		private String nickName; // 昵称
		private String portrait; // 头像
		private Short gender; // 性别
		private int level; // 等级
		private int maxVideos; // 最大可观看视频数
		private String inviteCode; // 邀请码
		private Asset asset; // 资产

		public long getUserId() {
			return userId;
		}

		public void setUserId(long userId) {
			this.userId = userId;
		}

		public String getUserName() {
			return userName;
		}

		public void setUserName(String userName) {
			this.userName = userName;
		}

		public boolean getRegistered() {
			return registered;
		}

		public void setRegistered(boolean registered) {
			this.registered = registered;
		}

		public String getPassword() {
			return password;
		}

		public void setPassword(String password) {
			this.password = password;
		}

		public String getNickName() {
			return nickName;
		}

		public void setNickName(String nickName) {
			this.nickName = nickName;
		}

		public String getPortrait() {
			return portrait;
		}

		public void setPortrait(String portrait) {
			this.portrait = portrait;
		}

		public Short getGender() {
			return gender;
		}

		public void setGender(Short gender) {
			this.gender = gender;
		}

		public int getLevel() {
			return level;
		}

		public void setLevel(int level) {
			this.level = level;
		}

		public int getMaxVideos() {
			return maxVideos;
		}

		public void setMaxVideos(int maxVideos) {
			this.maxVideos = maxVideos;
		}

		public String getInviteCode() {
			return inviteCode;
		}

		public void setInviteCode(String inviteCode) {
			this.inviteCode = inviteCode;
		}

		public Asset getAsset() {
			return asset;
		}

		public void setAsset(Asset asset) {
			this.asset = asset;
		}
	}

	/**
	 * 用户登录
	 * 
	 * @param httpSession
	 *            会话
	 * @param request
	 *            请求
	 * @return 登录成功，返回用户ID
	 */
	public LoginResponse login(HttpSession httpSession, LoginRequest request);

	static class LogoutRequest extends BaseRequest {
		private long userId; // 用户ID
		private String devId; // 设备号

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
	}

	/**
	 * 用户注销
	 * 
	 * @param httpSession
	 *            会话
	 * @param request
	 *            请求
	 */
	public BaseResponse logout(HttpSession httpSession, LogoutRequest request);

	static class SendVerifyRequest extends BaseRequest {
		private String userName; // 用户名，必须为手机

		public String getUserName() {
			return userName;
		}

		public void setUserName(String userName) {
			this.userName = userName;
		}
	}

	/**
	 * 获取验证码
	 * 
	 * @param httpSession
	 *            会话
	 * @param request
	 *            请求
	 * @return 注册成功
	 */
	public BaseResponse sendVerify(HttpSession httpSession, SendVerifyRequest request);

	static class GetAssetRequest extends BaseRequest {
	}

	static class ProfitItem {
		private int times; // 次数
		private long profit; // 收入

		public int getTimes() {
			return times;
		}

		public void setTimes(int times) {
			this.times = times;
		}

		public long getProfit() {
			return profit;
		}

		public void setProfit(long profit) {
			this.profit = profit;
		}
	}

	static class Asset {
		private int version; // 版本
		private int rank; // 排名
		private int checkin; // 连续签到天数
		private int reviews; // 查看详情次数
		private long profit; // 扣除提现后的收入
		private ProfitItem lock; // 锁屏收入
		private ProfitItem video; // 视频收入
		private ProfitItem promotion; // 活动收入
		private ProfitItem recom; // 推荐收入

		public Asset(UserStatus userStatus) {
			version = userStatus.getVersion();
			rank = userStatus.getRank();
			checkin = userStatus.getCheckin() + 1; // 当天需要加1
			reviews = userStatus.getReviews();
			profit = userStatus.getLockProfit() + userStatus.getVideoProfit() + userStatus.getPromotionProfit()
					+ userStatus.getRecomProfit() - userStatus.getCashed();

			lock = new ProfitItem();
			lock.setTimes(userStatus.getLockTimes());
			lock.setProfit(userStatus.getLockProfit());

			video = new ProfitItem();
			video.setTimes(userStatus.getVideoTimes());
			video.setProfit(userStatus.getVideoProfit());

			promotion = new ProfitItem();
			promotion.setTimes(userStatus.getPromotionTimes());
			promotion.setProfit(userStatus.getPromotionProfit());

			recom = new ProfitItem();
			recom.setTimes(userStatus.getRecomTimes());
			recom.setProfit(userStatus.getRecomProfit());
		}

		public int getVersion() {
			return version;
		}

		public void setVersion(int version) {
			this.version = version;
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

		public int getReviews() {
			return reviews;
		}

		public void setReviews(int reviews) {
			this.reviews = reviews;
		}

		public long getProfit() {
			return profit;
		}

		public void setProfit(long profit) {
			this.profit = profit;
		}

		public ProfitItem getLock() {
			return lock;
		}

		public void setLock(ProfitItem lock) {
			this.lock = lock;
		}

		public ProfitItem getVideo() {
			return video;
		}

		public void setVideo(ProfitItem video) {
			this.video = video;
		}

		public ProfitItem getPromotion() {
			return promotion;
		}

		public void setPromotion(ProfitItem promotion) {
			this.promotion = promotion;
		}

		public ProfitItem getRecom() {
			return recom;
		}

		public void setRecom(ProfitItem recom) {
			this.recom = recom;
		}
	}

	static class GetAssetResponse extends BaseResponse {
		private Asset asset; // 资产

		public Asset getAsset() {
			return asset;
		}

		public void setAsset(Asset asset) {
			this.asset = asset;
		}
	}

	/**
	 * 获取用户获得的收益
	 * 
	 * @param httpSession
	 *            会话
	 * @param request
	 *            请求
	 * @return 用户收益
	 */
	public GetAssetResponse getAsset(HttpSession httpSession, GetAssetRequest request);

	static class UpdateRequest extends BaseRequest {
		private String verifyCode; // 验证码
		private String oldPassword; // 原密码
		private InfoUser infoUser; // 用户变更信息

		public String getVerifyCode() {
			return verifyCode;
		}

		public void setVerifyCode(String verifyCode) {
			this.verifyCode = verifyCode;
		}

		public String getOldPassword() {
			return oldPassword;
		}

		public void setOldPassword(String oldPassword) {
			this.oldPassword = oldPassword;
		}

		public InfoUser getInfoUser() {
			return infoUser;
		}

		public void setInfoUser(InfoUser infoUser) {
			this.infoUser = infoUser;
		}
	}

	/**
	 * 用户更新
	 * 
	 * @param httpSession
	 *            会话
	 * @param request
	 *            请求
	 * @return 是否成功
	 */
	public BaseResponse update(HttpSession httpSession, UpdateRequest request);

	static class ResetPasswordRequest extends BaseRequest {
		private String userName; // 用户名
		private String verifyCode; // 验证码
		private String password; // 密码

		public String getUserName() {
			return userName;
		}

		public void setUserName(String userName) {
			this.userName = userName;
		}

		public String getVerifyCode() {
			return verifyCode;
		}

		public void setVerifyCode(String verifyCode) {
			this.verifyCode = verifyCode;
		}

		public String getPassword() {
			return password;
		}

		public void setPassword(String password) {
			this.password = password;
		}
	}

	/**
	 * 重置密码
	 * 
	 * @param httpSession
	 *            会话
	 * @param request
	 *            请求
	 * @return 是否成功
	 */
	public BaseResponse resetPassword(HttpSession httpSession, ResetPasswordRequest request);

	static class GetRankBoardRequest extends BaseRequest {
	}

	static class GetRankBoardResponse extends BaseResponse {
		private List<RankBoardValue> items; // 排行榜列表

		public List<RankBoardValue> getItems() {
			return items;
		}

		public void setItems(List<RankBoardValue> items) {
			this.items = items;
		}
	}

	/**
	 * 获取排行榜
	 * 
	 * @param request
	 *            请求
	 * @return 排行榜详情
	 */
	public GetRankBoardResponse getRankBoard(GetRankBoardRequest request);

	static class AddFeedbackRequest extends BaseRequest {
		private String content;

		public String getContent() {
			return content;
		}

		public void setContent(String content) {
			this.content = content;
		}
	}

	/**
	 * 添加反馈
	 * 
	 * @param httpSession
	 *            会话
	 * @param request
	 *            请求
	 * @return 是否成功
	 */
	public BaseResponse addFeedback(HttpSession httpSession, AddFeedbackRequest request);

	static class Behavior {
		int behavior;
		String content;

		public int getBehavior() {
			return behavior;
		}

		public void setBehavior(int behavior) {
			this.behavior = behavior;
		}

		public String getContent() {
			return content;
		}

		public void setContent(String content) {
			this.content = content;
		}
	}

	static class BehaviorsRequest {
		private List<Behavior> items; // 行为列表

		public List<Behavior> getItems() {
			return items;
		}

		public void setItems(List<Behavior> items) {
			this.items = items;
		}
	}

	/**
	 * 添加用户行为
	 * 
	 * @param httpSession
	 *            会话
	 * @param request
	 *            请求
	 * @return 是否成功
	 */
	public BaseResponse addBehaviors(HttpSession httpSession, String request);

}
