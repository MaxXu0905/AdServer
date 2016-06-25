package com.ailk.api;

import com.ailk.api.impl.BaseRequest;
import com.ailk.api.impl.BaseResponse;

public interface PackageIfc {

	static class GetVersionRequest extends BaseRequest {
	}

	static class GetVersionResponse extends BaseResponse {
		private String versionName; // �汾������
		private String info; // �汾��Ϣ

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
	 * ��ȡ��װ���İ汾
	 * 
	 * @param request
	 *            ����
	 * @return �Ƿ�ɹ�
	 */
	public GetVersionResponse getVersion(GetVersionRequest request);

}
