package com.ailk.open;

import java.util.List;

import org.apache.log4j.Logger;

import com.ailk.api.UserIfc;
import com.baidu.yun.channel.auth.ChannelKeyPair;
import com.baidu.yun.channel.client.BaiduChannelClient;
import com.baidu.yun.channel.exception.ChannelClientException;
import com.baidu.yun.channel.exception.ChannelServerException;
import com.baidu.yun.channel.model.BindInfo;
import com.baidu.yun.channel.model.PushBroadcastMessageRequest;
import com.baidu.yun.channel.model.PushBroadcastMessageResponse;
import com.baidu.yun.channel.model.PushUnicastMessageRequest;
import com.baidu.yun.channel.model.PushUnicastMessageResponse;
import com.baidu.yun.channel.model.QueryBindListRequest;
import com.baidu.yun.channel.model.QueryBindListResponse;
import com.google.gson.Gson;

/**
 * 推送管理
 * 
 * @author xugq
 * 
 */
public class PushManager {

	private final static Logger logger = Logger.getLogger(PushManager.class);
	private static final String API_KEY = "jWFWLMvDNh0GaeY3rCqdGKjT";
	private static final String SECRET_KEY = "rnHsR6Gitm8fbFzC8FkjhOCuMdjvY18a";

	public static final int MSG_TYPE_STATUS_CHANGE = 1; // 广告状态变更
	public static final int MSG_TYPE_SYSTEM = 2; // 系统消息
	public static final int MSG_TYPE_KICK_USER = 3; // 踢用户

	private static PushManager instance = new PushManager();

	private BaiduChannelClient channelClient;
	private Gson gson;

	/**
	 * 推送应答基类
	 * 
	 * @author xugq
	 * 
	 */
	public static class PushResponse {
		private long timestamp; // 服务端系统时间戳
		private int msgType; // 消息类型
		private long userId; // 用户ID
		
		public PushResponse() {
			timestamp = System.currentTimeMillis();
		}

		public long getTimestamp() {
			return timestamp;
		}

		public void setTimestamp(long timestamp) {
			this.timestamp = timestamp;
		}

		public int getMsgType() {
			return msgType;
		}

		public void setMsgType(int msgType) {
			this.msgType = msgType;
		}

		public long getUserId() {
			return userId;
		}

		public void setUserId(long userId) {
			this.userId = userId;
		}
	}

	public static class StatusChangeResponse extends PushResponse {
		private long id; // 唯一ID
		private int adId; // 广告ID
		private short status; // 状态
		private String title; // 标题
		private Long profit; // 金额
		private String reason; // 拒绝原因
		private long lastUpdate; // 时间戳
		private UserIfc.Asset asset; // 资产

		public StatusChangeResponse() {
			setMsgType(MSG_TYPE_STATUS_CHANGE);
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

		public UserIfc.Asset getAsset() {
			return asset;
		}

		public void setAsset(UserIfc.Asset asset) {
			this.asset = asset;
		}
	}

	public static class SystemResponse extends PushResponse {
		private String message; // 消息
		private long lastUpdate; // 时间戳

		public SystemResponse() {
			setMsgType(MSG_TYPE_SYSTEM);
		}

		public String getMessage() {
			return message;
		}

		public void setMessage(String message) {
			this.message = message;
		}

		public long getLastUpdate() {
			return lastUpdate;
		}

		public void setLastUpdate(long lastUpdate) {
			this.lastUpdate = lastUpdate;
		}
	}

	public static class KickUserResponse extends PushResponse {
		private long lastUpdate; // 时间戳
		
		public KickUserResponse() {
			setMsgType(MSG_TYPE_KICK_USER);
		}

		public long getLastUpdate() {
			return lastUpdate;
		}

		public void setLastUpdate(long lastUpdate) {
			this.lastUpdate = lastUpdate;
		}
	}

	/**
	 * 获取推送实例
	 * 
	 * @return
	 */
	public static PushManager getInstance() {
		return instance;
	}

	/**
	 * 构造函数
	 */
	private PushManager() {
		ChannelKeyPair pair = new ChannelKeyPair(API_KEY, SECRET_KEY);
		channelClient = new BaiduChannelClient(pair);

		gson = new Gson();
	}

	/**
	 * 发送消息给特定的用户
	 * 
	 * @param bdUserId
	 *            推送用户
	 * @param message
	 *            消息
	 * @return 成功发送数
	 */
	public int sendMessage(String bdUserId, Object message) {
		try {
			PushUnicastMessageRequest request = new PushUnicastMessageRequest();
			request.setUserId(bdUserId);
			request.setMessage(gson.toJson(message));

			PushUnicastMessageResponse response = channelClient.pushUnicastMessage(request);
			return response.getSuccessAmount();
		} catch (ChannelClientException e) {
			// 处理客户端错误异常
			logger.error("系统异常，" + e);
		} catch (ChannelServerException e) {
			// 处理服务端错误异常
			logger.error("系统异常，"
					+ String.format("request_id: %d, error_code: %d, error_message: %s", e.getRequestId(),
							e.getErrorCode(), e.getErrorMsg()));
		}

		return 0;
	}

	/**
	 * 发送通知给特定的用户
	 * 
	 * @param bdUserId
	 *            推送用户
	 * @param title
	 *            标题
	 * @param description
	 *            描述
	 * @return 成功发送数
	 */
	public int sendNotification(String bdUserId, String title, String description) {
		try {
			PushUnicastMessageRequest request = new PushUnicastMessageRequest();
			request.setUserId(bdUserId);
			request.setMessageType(1);
			request.setMessage("{\"title\":\""
					+ title
					+ "\",\"description\":\""
					+ description
					+ "\",\"open_type\":2,\"pkg_content\":\"#Intent;component=com.adlooker/.activity.SplashActivity;end\"}");

			PushUnicastMessageResponse response = channelClient.pushUnicastMessage(request);
			return response.getSuccessAmount();
		} catch (ChannelClientException e) {
			// 处理客户端错误异常
			logger.error("系统异常，" + e);
		} catch (ChannelServerException e) {
			// 处理服务端错误异常
			logger.error("系统异常，"
					+ String.format("request_id: %d, error_code: %d, error_message: %s", e.getRequestId(),
							e.getErrorCode(), e.getErrorMsg()));
		}

		return 0;
	}

	/**
	 * 广播消息
	 * 
	 * @param message
	 *            消息
	 * @return 成功发送数
	 */
	public int broadcast(String message) {
		try {
			PushBroadcastMessageRequest request = new PushBroadcastMessageRequest();
			request.setMessage(message);

			PushBroadcastMessageResponse response = channelClient.pushBroadcastMessage(request);
			return response.getSuccessAmount();
		} catch (ChannelClientException e) {
			// 处理客户端错误异常
			logger.error("系统异常，" + e);
		} catch (ChannelServerException e) {
			// 处理服务端错误异常
			logger.error("系统异常，"
					+ String.format("request_id: %d, error_code: %d, error_message: %s", e.getRequestId(),
							e.getErrorCode(), e.getErrorMsg()));
		}

		return 0;
	}

	/**
	 * 获取绑定列表
	 * 
	 * @param bdUserId
	 *            推送用户
	 * @return 绑定列表
	 */
	public List<BindInfo> getBinds(String bdUserId) {
		try {
			QueryBindListRequest request = new QueryBindListRequest();
			request.setUserId(bdUserId);

			QueryBindListResponse response = channelClient.queryBindList(request);
			return response.getBinds();
		} catch (ChannelClientException e) {
			// 处理客户端错误异常
			logger.error("系统异常，" + e);
		} catch (ChannelServerException e) {
			// 处理服务端错误异常
			logger.error("系统异常，"
					+ String.format("request_id: %d, error_code: %d, error_message: %s", e.getRequestId(),
							e.getErrorCode(), e.getErrorMsg()));
		}

		return null;
	}

}
