package com.ailk.api;

import com.ailk.api.impl.BaseRequest;
import com.ailk.api.impl.BaseResponse;

public interface OpenIfc {

	static class BindBdPushUserRequest extends BaseRequest {
		private String devId; // �豸ID
		private String userId; // �ٶ��û�ID

		public String getDevId() {
			return devId;
		}

		public void setDevId(String devId) {
			this.devId = devId;
		}

		public String getUserId() {
			return userId;
		}

		public void setUserId(String userId) {
			this.userId = userId;
		}
	}
	
	/**
	 * �����豸��ٶ��˺ż��ӳ��
	 * 
	 * @param request
	 *            ����
	 * @return �Ƿ�ɹ�
	 */
	public BaseResponse bindBdPushUser(BindBdPushUserRequest request);

}