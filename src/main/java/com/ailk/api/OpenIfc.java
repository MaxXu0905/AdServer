package com.ailk.api;

import com.ailk.api.impl.BaseRequest;
import com.ailk.api.impl.BaseResponse;

public interface OpenIfc {

	static class BindBdPushUserRequest extends BaseRequest {
		private String devId; // 设备ID
		private String userId; // 百度用户ID

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
	 * 保存设备与百度账号间的映射
	 * 
	 * @param request
	 *            请求
	 * @return 是否成功
	 */
	public BaseResponse bindBdPushUser(BindBdPushUserRequest request);

}
