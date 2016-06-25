package com.ailk.api.impl;


public class BaseResponse {

	public static final int SUCCESS = 0; // 成功
	public static final int EABORT = 1; // 异常退出
	public static final int EBADDESC = 2; // 描述错误
	public static final int EBLOCK = 3; // 阻塞
	public static final int EINVAL = 4; // 参数无效
	public static final int ELIMIT = 5; // 超过限制
	public static final int ENOENT = 6; // 没有入口
	public static final int EOS = 7; // 操作系统异常
	public static final int EPERM = 8; // 权限错误
	public static final int EPROTO = 9; // 协议错误
	public static final int ESVCERR = 10; // 服务错误
	public static final int ESVCFAIL = 11; // 服务失败
	public static final int ESYSTEM = 12; // 系统错误
	public static final int ETIME = 13; // 超时
	public static final int ERELEASE = 14; // 版本错误
	public static final int EMATCH = 15; // 匹配错误
	public static final int EDIAGNOSTIC = 16; // 诊断错误
	public static final int EDUPENT = 17; // 重复入口
	public static final int EEXPIRE = 18; // 超时无效
	public static final int EDEVID = 19; // 设备错误
	public static final int EVERIFY = 20; // 获取验证码失败

	public static final int ENOSESSION = -1; // 会话失效，需要重新登录
	
	public static final String OSS_BASE_URL = "http://192.168.1.111:8080/OSS/oss";

	public static class SystemProperties {
		private int version; // 版本号
		private int features; // 支持的属性列表

		private String ossBaseUrl; // OSS基础URL
		private String ossAppId; // OSS App ID
		private String ossAppSecret; // OSS App Secret

		private String sinaWeiboAppKey; // 新浪微博APP KEY
		private String sinaWeiboAppSecret; // 新浪微博App SECRET
		private String sinaWeiboOfficialName; // 新浪微博官方

		private String rennAppId; // 人人网APP ID
		private String rennAppKey; // 人人网APP KEY
		private String rennAppSecret; // 人人网App SECRET
		private String rennOfficialName; // 人人网官方

		private String tencentWeiboAppKey; // 腾讯微博APP KEY
		private String tencentWeiboAppSecret; // 腾讯微博App SECRET
		private String tencentWeiboOfficialName; // 腾讯微博官方
		private String tencentWeiboOpenIds; // 腾讯微博官方OPEN IDS

		private String tencentQqAppKey; // 腾讯QQ APP KEY
		private String tencentQqAppSecret; // 腾讯QQ App SECRET
		private String tencentQqOfficialName; // 腾讯QQ官方

		private String tencentWeichatAppId; // 腾讯微信APP ID
		
		private String hotline; // 热线电话

		private String bdPushAppKey; // 百度推送平台APP KEY

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

	private int errorCode; // 错误代码
	private String errorDesc; // 错误描述
	private long timestamp; // 服务端系统时间戳
	private SystemProperties properties; // 系统参数

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
