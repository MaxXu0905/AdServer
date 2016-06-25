package com.ailk.open;

import java.util.List;

import org.junit.Test;

import com.ailk.common.ConstVariables;
import com.ailk.open.PushManager.StatusChangeResponse;
import com.ailk.open.PushManager.SystemResponse;
import com.baidu.yun.channel.model.BindInfo;

public class PushMessageTest {

	// private static final String USER_ID = "1089179183342560674"; // 9300
	private static final String USER_ID = "1136268354858233112"; // HUAWEI

	@Test
	public void testStatusChange() {
		PushManager pushManager = PushManager.getInstance();

		StatusChangeResponse response = new StatusChangeResponse();
		response.setId(100L);
		response.setUserId(7L);
		response.setAdId(1);
		response.setStatus(ConstVariables.STATUS_FORBIDDEN);
		response.setTitle("测试");
		response.setProfit(100L);
		response.setReason("拒绝");
		response.setLastUpdate(System.currentTimeMillis());

		pushManager.sendMessage(USER_ID, response);
	}

	@Test
	public void testSystemMesage() {
		PushManager pushManager = PushManager.getInstance();

		SystemResponse response = new SystemResponse();
		response.setUserId(7L);
		response.setMessage("系统消息");
		response.setLastUpdate(System.currentTimeMillis());

		pushManager.sendMessage(USER_ID, response);
	}

	@Test
	public void testMessage() {
		PushManager pushManager = PushManager.getInstance();

		pushManager.sendMessage(USER_ID, "Hello Channel");
	}

	@Test
	public void testNotification() {
		PushManager pushManager = PushManager.getInstance();

		pushManager.sendNotification(USER_ID, "Notify_title_danbo", "Notify_description_content");
	}

	@Test
	public void testBroadcast() {
		PushManager pushManager = PushManager.getInstance();

		pushManager.broadcast("Hello Channel");
	}

	@Test
	public void testBind() {
		PushManager pushManager = PushManager.getInstance();

		List<BindInfo> bindInfos = pushManager.getBinds(USER_ID);
		if (bindInfos == null)
			return;

		for (BindInfo bindInfo : bindInfos) {
			long channelId = bindInfo.getChannelId();
			String userId = bindInfo.getUserId();
			int status = bindInfo.getBindStatus();
			System.out.println("channel_id:" + channelId + ", user_id: " + userId + ", status: " + status);

			String bindName = bindInfo.getBindName();
			long bindTime = bindInfo.getBindTime();
			String deviceId = bindInfo.getDeviceId();
			int deviceType = bindInfo.getDeviceType();
			long timestamp = bindInfo.getOnlineTimestamp();
			long expire = bindInfo.getOnlineExpires();

			System.out.println("bind_name:" + bindName + "\t" + "bind_time:" + bindTime);
			System.out.println("device_type:" + deviceType + "\tdeviceId:" + deviceId);
			System.out.println(String.format("timestamp: %d, expire: %d", timestamp, expire));
		}
	}

}
