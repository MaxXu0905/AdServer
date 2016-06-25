package com.ailk.common;

public class ConstVariables {

	public static final String FIELD_SEP = "\001"; // 字段间的分割符
	public static final String RECORD_SEP = "\002"; // 记录间的分隔符

	// 电话类型
	public static final short PHONE_TYPE_UNICOM = 1; // 联通
	public static final short PHONE_TYPE_TELECOM = 2; // 电信
	public static final short PHONE_TYPE_MOBILE = 3; // 移动

	// 性别
	public static final short GENDER_MALE = 1; // 男
	public static final short GENDER_FEMALE = 2; // 女

	// 支付类型
	public static final short PAY_TYPE_ALIPAY = 1; // 支付宝
	public static final short PAY_TYPE_TENPAY = 2; // 财付通
	public static final short PAY_TYPE_BANK = 3; // 银行
	public static final short PAY_TYPE_CARD = 4; // 充值卡

	// 问题类型
	public static final short QUIZ_TYPE_SELECT = 1; // 单选
	public static final short QUIZ_TYPE_TEXT = 2; // 文本
	public static final short QUIZ_TYPE_MULTI = 3; // 多选
	public static final short QUIZ_TYPE_GPS = 4; // 获取GPS位置
	public static final short QUIZ_TYPE_PHOTO = 5; // 上传照片
	public static final short QUIZ_TYPE_VIDEO = 6; // 上传视频

	// 广告形式
	public static final short AD_STYLE_VIDEO = 1; // 视频
	public static final short AD_STYLE_SPLASH = 2; // 首屏
	public static final short AD_STYLE_LOCK = 3; // 锁屏
	public static final short AD_STYLE_PROMOTION = 4; // 活动

	// 投放类型
	public static final short DELIVERY_TYPE_MONEY = 1; // 限钱
	public static final short DELIVERY_TYPE_TIME = 2; // 限时

	// 审核状态
	public static final short STATUS_PENDING = 1; // 待审核
	public static final short STATUS_PASS = 2; // 审核通过
	public static final short STATUS_REJECT = 3; // 审核拒绝
	public static final short STATUS_OFFLINE = 4; // 已下线
	public static final short STATUS_FORBIDDEN = 5; // 系统拒绝
	public static final short STATUS_EXCEED = 6; // 名额已满（余额不足）
	public static final short STATUS_ELIMIT = 7; // 活动超过可参加次数
	
	// 以下审核状态被认为成功，但不计费
	public static final short STATUS_NO_MONEY = -1; // 无费用

	// 时段类型
	public static final short PERIOD_TYPE_WORKING = 1; // 工作日
	public static final short PERIOD_TYPE_HOLIDAY = 2; // 休假日

	// 客户类型
	public static final short CUST_TYPE_NORMAL = 1; // 普通客户
	public static final short CUST_TYPE_SYSTEM = 2; // 系统客户

	// 用户属性
	public static final int ATTR_ID_CITY = 1; // 城市
	public static final int ATTR_ID_PERIOD = 2; // 时间段

	// 操作系统类型
	public static final short OS_TYPE_ANDROID = 1; // Android
	public static final short OS_TYPE_IOS = 2; // IOS
	public static final short OS_TYPE_WEB = 3; // 网页

	// 账号状态
	public static final short BALANCE_STATUS_ACTIVE = 1; // 活跃
	public static final short BALANCE_STATUS_INACTIVE = 2; // 失效

	public static final int USER_ADS_PER_BATCH = 20; // 每个用户的广告列表最多存储的记录数
	public static final int PAYS_PER_BATCH = 20; // 一次获取支付请求的记录数
	public static final int PROMOTIONS_PER_BATCH = 5; // 一次获取参与活动的记录数

	// 获得海贝的条件
	public static final int CHECKINS_PER_LEVEL = 10; // 每10天连续登陆可兑换一个海贝
	public static final int VIDEOS_PER_LEVEL = 30; // 每30个视频可兑换一个海贝
	public static final int PROMOTIONS_PER_LEVEL = 1; // 每1个活动可兑换一个海贝
	public static final int REVIEWS_PER_LEVEL = 50; // 每查看50次不同广告的详情（不论是锁屏左滑、视频查看详情或者是活动查看详情）
	
	public static final int MAX_LEVEL = 1020; // 最大等级
	
	// 行为类型
	public static final int BEHAVIOR_REVIEW = 1; // 广告详情

	private static String APPROVAL_STATUS_DESC[] = { "", "待审核", "审核通过", "离线" };

	public static String getStatusDesc(short status) {
		if (status < 0 || status >= APPROVAL_STATUS_DESC.length)
			return "";
		else
			return APPROVAL_STATUS_DESC[status];
	}

}
