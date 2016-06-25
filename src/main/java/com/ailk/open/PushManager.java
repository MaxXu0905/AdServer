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
 * ���͹���
 * 
 * @author xugq
 * 
 */
public class PushManager {

	private final static Logger logger = Logger.getLogger(PushManager.class);
	private static final String API_KEY = "jWFWLMvDNh0GaeY3rCqdGKjT";
	private static final String SECRET_KEY = "rnHsR6Gitm8fbFzC8FkjhOCuMdjvY18a";

	public static final int MSG_TYPE_STATUS_CHANGE = 1; // ���״̬���
	public static final int MSG_TYPE_SYSTEM = 2; // ϵͳ��Ϣ
	public static final int MSG_TYPE_KICK_USER = 3; // ���û�

	private static PushManager instance = new PushManager();

	private BaiduChannelClient channelClient;
	private Gson gson;

	/**
	 * ����Ӧ�����
	 * 
	 * @author xugq
	 * 
	 */
	public static class PushResponse {
		private long timestamp; // �����ϵͳʱ���
		private int msgType; // ��Ϣ����
		private long userId; // �û�ID
		
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
		private long id; // ΨһID
		private int adId; // ���ID
		private short status; // ״̬
		private String title; // ����
		private Long profit; // ���
		private String reason; // �ܾ�ԭ��
		private long lastUpdate; // ʱ���
		private UserIfc.Asset asset; // �ʲ�

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
		private String message; // ��Ϣ
		private long lastUpdate; // ʱ���

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
		private long lastUpdate; // ʱ���
		
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
	 * ��ȡ����ʵ��
	 * 
	 * @return
	 */
	public static PushManager getInstance() {
		return instance;
	}

	/**
	 * ���캯��
	 */
	private PushManager() {
		ChannelKeyPair pair = new ChannelKeyPair(API_KEY, SECRET_KEY);
		channelClient = new BaiduChannelClient(pair);

		gson = new Gson();
	}

	/**
	 * ������Ϣ���ض����û�
	 * 
	 * @param bdUserId
	 *            �����û�
	 * @param message
	 *            ��Ϣ
	 * @return �ɹ�������
	 */
	public int sendMessage(String bdUserId, Object message) {
		try {
			PushUnicastMessageRequest request = new PushUnicastMessageRequest();
			request.setUserId(bdUserId);
			request.setMessage(gson.toJson(message));

			PushUnicastMessageResponse response = channelClient.pushUnicastMessage(request);
			return response.getSuccessAmount();
		} catch (ChannelClientException e) {
			// ����ͻ��˴����쳣
			logger.error("ϵͳ�쳣��" + e);
		} catch (ChannelServerException e) {
			// �������˴����쳣
			logger.error("ϵͳ�쳣��"
					+ String.format("request_id: %d, error_code: %d, error_message: %s", e.getRequestId(),
							e.getErrorCode(), e.getErrorMsg()));
		}

		return 0;
	}

	/**
	 * ����֪ͨ���ض����û�
	 * 
	 * @param bdUserId
	 *            �����û�
	 * @param title
	 *            ����
	 * @param description
	 *            ����
	 * @return �ɹ�������
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
			// ����ͻ��˴����쳣
			logger.error("ϵͳ�쳣��" + e);
		} catch (ChannelServerException e) {
			// �������˴����쳣
			logger.error("ϵͳ�쳣��"
					+ String.format("request_id: %d, error_code: %d, error_message: %s", e.getRequestId(),
							e.getErrorCode(), e.getErrorMsg()));
		}

		return 0;
	}

	/**
	 * �㲥��Ϣ
	 * 
	 * @param message
	 *            ��Ϣ
	 * @return �ɹ�������
	 */
	public int broadcast(String message) {
		try {
			PushBroadcastMessageRequest request = new PushBroadcastMessageRequest();
			request.setMessage(message);

			PushBroadcastMessageResponse response = channelClient.pushBroadcastMessage(request);
			return response.getSuccessAmount();
		} catch (ChannelClientException e) {
			// ����ͻ��˴����쳣
			logger.error("ϵͳ�쳣��" + e);
		} catch (ChannelServerException e) {
			// �������˴����쳣
			logger.error("ϵͳ�쳣��"
					+ String.format("request_id: %d, error_code: %d, error_message: %s", e.getRequestId(),
							e.getErrorCode(), e.getErrorMsg()));
		}

		return 0;
	}

	/**
	 * ��ȡ���б�
	 * 
	 * @param bdUserId
	 *            �����û�
	 * @return ���б�
	 */
	public List<BindInfo> getBinds(String bdUserId) {
		try {
			QueryBindListRequest request = new QueryBindListRequest();
			request.setUserId(bdUserId);

			QueryBindListResponse response = channelClient.queryBindList(request);
			return response.getBinds();
		} catch (ChannelClientException e) {
			// ����ͻ��˴����쳣
			logger.error("ϵͳ�쳣��" + e);
		} catch (ChannelServerException e) {
			// �������˴����쳣
			logger.error("ϵͳ�쳣��"
					+ String.format("request_id: %d, error_code: %d, error_message: %s", e.getRequestId(),
							e.getErrorCode(), e.getErrorMsg()));
		}

		return null;
	}

}
