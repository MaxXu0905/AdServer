package com.ailk.api;

import com.ailk.api.impl.BaseRequest;
import com.ailk.api.impl.BaseResponse;

public interface TLSServerIfc {

	public static final String TLS_SERVER_ATTR = "TLSServerIfc";

	static class HandshakeRequest extends BaseRequest {
		private short osType; // ����ϵͳ����
		private String data; // �ù�Կ���ܵĶԳ���Կ

		public short getOsType() {
			return osType;
		}

		public void setOsType(short osType) {
			this.osType = osType;
		}

		public String getData() {
			return data;
		}

		public void setData(String data) {
			this.data = data;
		}
	}

	static class HandshakeResponse extends BaseResponse {
		private int version; // �汾��
		private int features; // ֧�ֵ������б�

		private String ossBaseUrl; // OSS����URL
		private String ossAppId; // OSS App ID
		private String ossAppSecret; // OSS App Secret

		private String sinaWeiboAppKey; // ����΢��APP KEY
		private String sinaWeiboAppSecret; // ����΢��App SECRET
		private String sinaWeiboOfficialName; // ����΢���ٷ�

		private String rennAppId; // ������APP ID
		private String rennAppKey; // ������APP KEY
		private String rennAppSecret; // ������App SECRET
		private String rennOfficialName; // �������ٷ�

		private String tencentWeiboAppKey; // ��Ѷ΢��APP KEY
		private String tencentWeiboAppSecret; // ��Ѷ΢��App SECRET
		private String tencentWeiboOfficialName; // ��Ѷ΢���ٷ�
		private String tencentWeiboOpenIds; // ��Ѷ΢���ٷ�OPEN IDS

		private String tencentQqAppKey; // ��ѶQQ APP KEY
		private String tencentQqAppSecret; // ��ѶQQ App SECRET
		private String tencentQqOfficialName; // ��ѶQQ�ٷ�

		private String tencentWeichatAppId; // ��Ѷ΢��APP ID

		public HandshakeResponse() {
			version = 1;
			features = 0;

			ossBaseUrl = "http://192.168.1.111:8080/OSS/oss";
			ossAppId = "ZVOyzDe2iVnWPKWZ";
			ossAppSecret = "46ny7hnYR9S2fP9nxDCiFn9X01KZUo";

			sinaWeiboAppKey = "1009564918";
			sinaWeiboAppSecret = "553f94964b8ea79c30b497bde93a2b19";
			sinaWeiboOfficialName = "18612259745";

			rennAppId = "242522";
			rennAppKey = "46bcdbf4271c481e87aea6b7277b0a11";
			rennAppSecret = "d19e5775d36a4902995a131aa38c9882";
			rennOfficialName = "562362374";

			tencentWeiboAppKey = "801425688";
			tencentWeiboAppSecret = "5ddbef1c0b2c357148fc31c7063eed0f";
			tencentWeiboOfficialName = "xugenquan79";
			tencentWeiboOpenIds = "";

			tencentQqAppKey = "801425688";
			tencentQqAppSecret = "5ddbef1c0b2c357148fc31c7063eed0f";
			tencentQqOfficialName = "40124999";

			tencentWeichatAppId = "wxf839b4ec45571dde";
		}

		public int getVersion() {
			return version;
		}

		public void setVersion(int version) {
			this.version = version;
		}

		public int getFeatures() {
			return features;
		}

		public void setFeatures(int features) {
			this.features = features;
		}

		public String getOssBaseUrl() {
			return ossBaseUrl;
		}

		public void setOssBaseUrl(String ossBaseUrl) {
			this.ossBaseUrl = ossBaseUrl;
		}

		public String getOssAppId() {
			return ossAppId;
		}

		public void setOssAppId(String ossAppId) {
			this.ossAppId = ossAppId;
		}

		public String getOssAppSecret() {
			return ossAppSecret;
		}

		public void setOssAppSecret(String ossAppSecret) {
			this.ossAppSecret = ossAppSecret;
		}

		public String getSinaWeiboAppKey() {
			return sinaWeiboAppKey;
		}

		public void setSinaWeiboAppKey(String sinaWeiboAppKey) {
			this.sinaWeiboAppKey = sinaWeiboAppKey;
		}

		public String getSinaWeiboAppSecret() {
			return sinaWeiboAppSecret;
		}

		public void setSinaWeiboAppSecret(String sinaWeiboAppSecret) {
			this.sinaWeiboAppSecret = sinaWeiboAppSecret;
		}

		public String getSinaWeiboOfficialName() {
			return sinaWeiboOfficialName;
		}

		public void setSinaWeiboOfficialName(String sinaWeiboOfficialName) {
			this.sinaWeiboOfficialName = sinaWeiboOfficialName;
		}

		public String getRennAppId() {
			return rennAppId;
		}

		public void setRennAppId(String rennAppId) {
			this.rennAppId = rennAppId;
		}

		public String getRennAppKey() {
			return rennAppKey;
		}

		public void setRennAppKey(String rennAppKey) {
			this.rennAppKey = rennAppKey;
		}

		public String getRennAppSecret() {
			return rennAppSecret;
		}

		public void setRennAppSecret(String rennAppSecret) {
			this.rennAppSecret = rennAppSecret;
		}

		public String getRennOfficialName() {
			return rennOfficialName;
		}

		public void setRennOfficialName(String rennOfficialName) {
			this.rennOfficialName = rennOfficialName;
		}

		public String getTencentWeiboAppKey() {
			return tencentWeiboAppKey;
		}

		public void setTencentWeiboAppKey(String tencentWeiboAppKey) {
			this.tencentWeiboAppKey = tencentWeiboAppKey;
		}

		public String getTencentWeiboAppSecret() {
			return tencentWeiboAppSecret;
		}

		public void setTencentWeiboAppSecret(String tencentWeiboAppSecret) {
			this.tencentWeiboAppSecret = tencentWeiboAppSecret;
		}

		public String getTencentWeiboOfficialName() {
			return tencentWeiboOfficialName;
		}

		public void setTencentWeiboOfficialName(String tencentWeiboOfficialName) {
			this.tencentWeiboOfficialName = tencentWeiboOfficialName;
		}

		public String getTencentWeiboOpenIds() {
			return tencentWeiboOpenIds;
		}

		public void setTencentWeiboOpenIds(String tencentWeiboOpenIds) {
			this.tencentWeiboOpenIds = tencentWeiboOpenIds;
		}

		public String getTencentQqAppKey() {
			return tencentQqAppKey;
		}

		public void setTencentQqAppKey(String tencentQqAppKey) {
			this.tencentQqAppKey = tencentQqAppKey;
		}

		public String getTencentQqAppSecret() {
			return tencentQqAppSecret;
		}

		public void setTencentQqAppSecret(String tencentQqAppSecret) {
			this.tencentQqAppSecret = tencentQqAppSecret;
		}

		public String getTencentQqOfficialName() {
			return tencentQqOfficialName;
		}

		public void setTencentQqOfficialName(String tencentQqOfficialName) {
			this.tencentQqOfficialName = tencentQqOfficialName;
		}

		public String getTencentWeichatAppId() {
			return tencentWeichatAppId;
		}

		public void setTencentWeichatAppId(String tencentWeichatAppId) {
			this.tencentWeichatAppId = tencentWeichatAppId;
		}
	}

	/**
	 * ������Ϣ���ܵĶԳ���Կ������Կ�ù�Կ����
	 * 
	 * @param request
	 *            ����
	 * @return ����Ӧ��
	 */
	public HandshakeResponse handshake(HandshakeRequest request);

	/**
	 * �öԳ���Կ����
	 * 
	 * @param data
	 *            ����ǰ������
	 * @return ���ܺ�����ݣ�����ʧ���򷵻�null
	 */
	public byte[] encrypt(byte[] data);

	/**
	 * �öԳ���Կ����
	 * 
	 * @param data
	 *            ����ǰ������
	 * @return ���ܺ�����ݣ�����ʧ���򷵻�null
	 */
	public byte[] decrypt(byte[] data);

}
