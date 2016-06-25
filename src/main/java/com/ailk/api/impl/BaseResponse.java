package com.ailk.api.impl;


public class BaseResponse {

	public static final int SUCCESS = 0; // �ɹ�
	public static final int EABORT = 1; // �쳣�˳�
	public static final int EBADDESC = 2; // ��������
	public static final int EBLOCK = 3; // ����
	public static final int EINVAL = 4; // ������Ч
	public static final int ELIMIT = 5; // ��������
	public static final int ENOENT = 6; // û�����
	public static final int EOS = 7; // ����ϵͳ�쳣
	public static final int EPERM = 8; // Ȩ�޴���
	public static final int EPROTO = 9; // Э�����
	public static final int ESVCERR = 10; // �������
	public static final int ESVCFAIL = 11; // ����ʧ��
	public static final int ESYSTEM = 12; // ϵͳ����
	public static final int ETIME = 13; // ��ʱ
	public static final int ERELEASE = 14; // �汾����
	public static final int EMATCH = 15; // ƥ�����
	public static final int EDIAGNOSTIC = 16; // ��ϴ���
	public static final int EDUPENT = 17; // �ظ����
	public static final int EEXPIRE = 18; // ��ʱ��Ч
	public static final int EDEVID = 19; // �豸����
	public static final int EVERIFY = 20; // ��ȡ��֤��ʧ��

	public static final int ENOSESSION = -1; // �ỰʧЧ����Ҫ���µ�¼
	
	public static final String OSS_BASE_URL = "http://192.168.1.111:8080/OSS/oss";

	public static class SystemProperties {
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
		
		private String hotline; // ���ߵ绰

		private String bdPushAppKey; // �ٶ�����ƽ̨APP KEY

		public SystemProperties(int version) {
			this.version = version;
			features = 0;

			ossBaseUrl = OSS_BASE_URL;
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
			
			hotline = "18612259745";

			bdPushAppKey = "jWFWLMvDNh0GaeY3rCqdGKjT";
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

		public String getHotline() {
			return hotline;
		}

		public void setHotline(String hotline) {
			this.hotline = hotline;
		}

		public String getBdPushAppKey() {
			return bdPushAppKey;
		}

		public void setBdPushAppKey(String bdPushAppKey) {
			this.bdPushAppKey = bdPushAppKey;
		}
	}

	private int errorCode; // �������
	private String errorDesc; // ��������
	private long timestamp; // �����ϵͳʱ���
	private SystemProperties properties; // ϵͳ����

	public BaseResponse() {
		errorCode = SUCCESS;
		errorDesc = null;
		timestamp = System.currentTimeMillis();
		properties = null;
	}

	public int getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorDesc() {
		return errorDesc;
	}

	public void setErrorDesc(String errorDesc) {
		this.errorDesc = errorDesc;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public SystemProperties getProperties() {
		return properties;
	}

	public void setProperties(SystemProperties properties) {
		this.properties = properties;
	}

}
