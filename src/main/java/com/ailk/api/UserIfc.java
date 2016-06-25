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
		private String userName; // �û���
		private String verifyCode; // ��֤��

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
		private int adId; // ���ID
		private int days; // �Ѿ��ۿ�����
		private int times; // �Ѿ��ۿ�����

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
		long userId; // �û�ID
		String devId; // �豸��
		long sysdate; // �Ự����ʱ�ĵ�ǰ����
		Map<Short, List<AdItem>> adMap; // ����б�ӳ��

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
		private String userName; // �û���
		private boolean registered; // �Ƿ�����ע��

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
	 * ����û��Ƿ����
	 * 
	 * @param request
	 *            ����
	 * @return �����򷵻���ȷ������ʧ��
	 */
	public BaseResponse exists(ExistsRequest request);

	static class RegisterRequest extends BaseRequest {
		private String verifyCode; // ��֤��
		private String bdUserId; // �ٶ��û�ID
		private InfoUser infoUser; // �û���Ϣ

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
		private long userId; // �û�ID
		private String userName; // �û���
		private boolean registered; // �Ƿ�Ϊע���û�
		private String password; // ����
		private String nickName; // �ǳ�
		private String portrait; // ͷ��
		private Short gender; // �Ա�
		private int level; // �ȼ�
		private int maxVideos; // ���ɹۿ���Ƶ��
		private String inviteCode; // ������
		private Asset asset; // �ʲ�

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
	 * �û�ע��
	 * 
	 * @param httpSession
	 *            �Ự
	 * @param request
	 *            ����
	 * @return ע��ɹ��������û�ID
	 */
	public RegisterResponse register(HttpSession httpSession, RegisterRequest request);

	static class LoginRequest extends BaseRequest {
		private String userName; // �û���
		private boolean registered; // �Ƿ�Ϊע���û�
		private String password; // ����
		private String devId; // �豸ID
		private String bdUserId; // �ٶ��û�ID

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
		private long userId; // �û�ID
		private String userName; // �û���
		private boolean registered; // �Ƿ�ע��
		private String password; // ����
		private String nickName; // �ǳ�
		private String portrait; // ͷ��
		private Short gender; // �Ա�
		private int level; // �ȼ�
		private int maxVideos; // ���ɹۿ���Ƶ��
		private String inviteCode; // ������
		private Asset asset; // �ʲ�

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
	 * �û���¼
	 * 
	 * @param httpSession
	 *            �Ự
	 * @param request
	 *            ����
	 * @return ��¼�ɹ��������û�ID
	 */
	public LoginResponse login(HttpSession httpSession, LoginRequest request);

	static class LogoutRequest extends BaseRequest {
		private long userId; // �û�ID
		private String devId; // �豸��

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
	 * �û�ע��
	 * 
	 * @param httpSession
	 *            �Ự
	 * @param request
	 *            ����
	 */
	public BaseResponse logout(HttpSession httpSession, LogoutRequest request);

	static class SendVerifyRequest extends BaseRequest {
		private String userName; // �û���������Ϊ�ֻ�

		public String getUserName() {
			return userName;
		}

		public void setUserName(String userName) {
			this.userName = userName;
		}
	}

	/**
	 * ��ȡ��֤��
	 * 
	 * @param httpSession
	 *            �Ự
	 * @param request
	 *            ����
	 * @return ע��ɹ�
	 */
	public BaseResponse sendVerify(HttpSession httpSession, SendVerifyRequest request);

	static class GetAssetRequest extends BaseRequest {
	}

	static class ProfitItem {
		private int times; // ����
		private long profit; // ����

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
		private int version; // �汾
		private int rank; // ����
		private int checkin; // ����ǩ������
		private int reviews; // �鿴�������
		private long profit; // �۳����ֺ������
		private ProfitItem lock; // ��������
		private ProfitItem video; // ��Ƶ����
		private ProfitItem promotion; // �����
		private ProfitItem recom; // �Ƽ�����

		public Asset(UserStatus userStatus) {
			version = userStatus.getVersion();
			rank = userStatus.getRank();
			checkin = userStatus.getCheckin() + 1; // ������Ҫ��1
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
		private Asset asset; // �ʲ�

		public Asset getAsset() {
			return asset;
		}

		public void setAsset(Asset asset) {
			this.asset = asset;
		}
	}

	/**
	 * ��ȡ�û���õ�����
	 * 
	 * @param httpSession
	 *            �Ự
	 * @param request
	 *            ����
	 * @return �û�����
	 */
	public GetAssetResponse getAsset(HttpSession httpSession, GetAssetRequest request);

	static class UpdateRequest extends BaseRequest {
		private String verifyCode; // ��֤��
		private String oldPassword; // ԭ����
		private InfoUser infoUser; // �û������Ϣ

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
	 * �û�����
	 * 
	 * @param httpSession
	 *            �Ự
	 * @param request
	 *            ����
	 * @return �Ƿ�ɹ�
	 */
	public BaseResponse update(HttpSession httpSession, UpdateRequest request);

	static class ResetPasswordRequest extends BaseRequest {
		private String userName; // �û���
		private String verifyCode; // ��֤��
		private String password; // ����

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
	 * ��������
	 * 
	 * @param httpSession
	 *            �Ự
	 * @param request
	 *            ����
	 * @return �Ƿ�ɹ�
	 */
	public BaseResponse resetPassword(HttpSession httpSession, ResetPasswordRequest request);

	static class GetRankBoardRequest extends BaseRequest {
	}

	static class GetRankBoardResponse extends BaseResponse {
		private List<RankBoardValue> items; // ���а��б�

		public List<RankBoardValue> getItems() {
			return items;
		}

		public void setItems(List<RankBoardValue> items) {
			this.items = items;
		}
	}

	/**
	 * ��ȡ���а�
	 * 
	 * @param request
	 *            ����
	 * @return ���а�����
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
	 * ��ӷ���
	 * 
	 * @param httpSession
	 *            �Ự
	 * @param request
	 *            ����
	 * @return �Ƿ�ɹ�
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
		private List<Behavior> items; // ��Ϊ�б�

		public List<Behavior> getItems() {
			return items;
		}

		public void setItems(List<Behavior> items) {
			this.items = items;
		}
	}

	/**
	 * ����û���Ϊ
	 * 
	 * @param httpSession
	 *            �Ự
	 * @param request
	 *            ����
	 * @return �Ƿ�ɹ�
	 */
	public BaseResponse addBehaviors(HttpSession httpSession, String request);

}
