package com.ailk.common;

public class ConstVariables {

	public static final String FIELD_SEP = "\001"; // �ֶμ�ķָ��
	public static final String RECORD_SEP = "\002"; // ��¼��ķָ���

	// �绰����
	public static final short PHONE_TYPE_UNICOM = 1; // ��ͨ
	public static final short PHONE_TYPE_TELECOM = 2; // ����
	public static final short PHONE_TYPE_MOBILE = 3; // �ƶ�

	// �Ա�
	public static final short GENDER_MALE = 1; // ��
	public static final short GENDER_FEMALE = 2; // Ů

	// ֧������
	public static final short PAY_TYPE_ALIPAY = 1; // ֧����
	public static final short PAY_TYPE_TENPAY = 2; // �Ƹ�ͨ
	public static final short PAY_TYPE_BANK = 3; // ����
	public static final short PAY_TYPE_CARD = 4; // ��ֵ��

	// ��������
	public static final short QUIZ_TYPE_SELECT = 1; // ��ѡ
	public static final short QUIZ_TYPE_TEXT = 2; // �ı�
	public static final short QUIZ_TYPE_MULTI = 3; // ��ѡ
	public static final short QUIZ_TYPE_GPS = 4; // ��ȡGPSλ��
	public static final short QUIZ_TYPE_PHOTO = 5; // �ϴ���Ƭ
	public static final short QUIZ_TYPE_VIDEO = 6; // �ϴ���Ƶ

	// �����ʽ
	public static final short AD_STYLE_VIDEO = 1; // ��Ƶ
	public static final short AD_STYLE_SPLASH = 2; // ����
	public static final short AD_STYLE_LOCK = 3; // ����
	public static final short AD_STYLE_PROMOTION = 4; // �

	// Ͷ������
	public static final short DELIVERY_TYPE_MONEY = 1; // ��Ǯ
	public static final short DELIVERY_TYPE_TIME = 2; // ��ʱ

	// ���״̬
	public static final short STATUS_PENDING = 1; // �����
	public static final short STATUS_PASS = 2; // ���ͨ��
	public static final short STATUS_REJECT = 3; // ��˾ܾ�
	public static final short STATUS_OFFLINE = 4; // ������
	public static final short STATUS_FORBIDDEN = 5; // ϵͳ�ܾ�
	public static final short STATUS_EXCEED = 6; // �������������㣩
	public static final short STATUS_ELIMIT = 7; // ������ɲμӴ���
	
	// �������״̬����Ϊ�ɹ��������Ʒ�
	public static final short STATUS_NO_MONEY = -1; // �޷���

	// ʱ������
	public static final short PERIOD_TYPE_WORKING = 1; // ������
	public static final short PERIOD_TYPE_HOLIDAY = 2; // �ݼ���

	// �ͻ�����
	public static final short CUST_TYPE_NORMAL = 1; // ��ͨ�ͻ�
	public static final short CUST_TYPE_SYSTEM = 2; // ϵͳ�ͻ�

	// �û�����
	public static final int ATTR_ID_CITY = 1; // ����
	public static final int ATTR_ID_PERIOD = 2; // ʱ���

	// ����ϵͳ����
	public static final short OS_TYPE_ANDROID = 1; // Android
	public static final short OS_TYPE_IOS = 2; // IOS
	public static final short OS_TYPE_WEB = 3; // ��ҳ

	// �˺�״̬
	public static final short BALANCE_STATUS_ACTIVE = 1; // ��Ծ
	public static final short BALANCE_STATUS_INACTIVE = 2; // ʧЧ

	public static final int USER_ADS_PER_BATCH = 20; // ÿ���û��Ĺ���б����洢�ļ�¼��
	public static final int PAYS_PER_BATCH = 20; // һ�λ�ȡ֧������ļ�¼��
	public static final int PROMOTIONS_PER_BATCH = 5; // һ�λ�ȡ�����ļ�¼��

	// ��ú���������
	public static final int CHECKINS_PER_LEVEL = 10; // ÿ10��������½�ɶһ�һ������
	public static final int VIDEOS_PER_LEVEL = 30; // ÿ30����Ƶ�ɶһ�һ������
	public static final int PROMOTIONS_PER_LEVEL = 1; // ÿ1����ɶһ�һ������
	public static final int REVIEWS_PER_LEVEL = 50; // ÿ�鿴50�β�ͬ�������飨�����������󻬡���Ƶ�鿴��������ǻ�鿴���飩
	
	public static final int MAX_LEVEL = 1020; // ���ȼ�
	
	// ��Ϊ����
	public static final int BEHAVIOR_REVIEW = 1; // �������

	private static String APPROVAL_STATUS_DESC[] = { "", "�����", "���ͨ��", "����" };

	public static String getStatusDesc(short status) {
		if (status < 0 || status >= APPROVAL_STATUS_DESC.length)
			return "";
		else
			return APPROVAL_STATUS_DESC[status];
	}

}
