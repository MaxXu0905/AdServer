package com.ailk.api;

import com.ailk.api.impl.BaseRequest;
import com.ailk.api.impl.BaseResponse;

public interface PackageIfc {

	static class GetVersionRequest extends BaseRequest {
	}

	static class GetVersionResponse extends BaseResponse {
		private String versionName; // 版本号名称
		private String info; // 版本信息

		public String getVersionName() {
			return versionName;
		}

		public void setVersionName(String versionName) {
			this.versionName = versionName;
		}

		public String getInfo() {
			return info;
		}

		public void setInfo(String info) {
			this.info = info;
		}
	}

	/**
	 * 获取安装包的版本
	 * 
	 * @param request
	 *            请求
	 * @return 是否成功
	 */
	public GetVersionResponse getVersion(GetVersionRequest request);

}
