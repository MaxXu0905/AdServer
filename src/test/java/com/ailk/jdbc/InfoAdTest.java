package com.ailk.jdbc;

import java.sql.Timestamp;

import org.hibernate.Session;
import org.junit.Test;

import com.ailk.common.ConstVariables;
import com.ailk.jdbc.entity.AdBalance;
import com.ailk.jdbc.entity.InfoAd;
import com.ailk.jdbc.entity.InfoQuiz;

public class InfoAdTest {

	@Test
	public void init() {
		for (int partition = 0; partition < HibernateUtil.getPartitions(); partition++) {
			Session session = HibernateUtil.getSessionFactory(partition).openSession();

			try {
				session.createSQLQuery("TRUNCATE TABLE info_ad").executeUpdate();
				session.createSQLQuery("TRUNCATE TABLE info_quiz").executeUpdate();
				session.createSQLQuery("TRUNCATE TABLE ad_balance").executeUpdate();
			} finally {
				session.close();
			}
		}
	}

	@Test
	public void testSaveVideos() {
		GlobalManager globalManager = GlobalManager.getInstance();
		InfoAdManager infoAdManager = InfoAdManager.getInstance(globalManager);
		InfoQuizManager infoQuizManager = InfoQuizManager.getInstance(globalManager);
		AdBalanceManager adBalanceManager = AdBalanceManager.getInstance();
		InfoAd infoAd = new InfoAd();
		InfoQuiz infoQuiz = new InfoQuiz();
		AdBalance adBalance = new AdBalance();

		String[] quizTitles = { "�������ʫ���커������ʲôϵ�еģ�", "����а���ŷ����������ʲô���ʵģ�", "�������ϵ»��ײ����͵���С�治������", "����������Ż�������ʲôϵ�еģ�",
				"����в�����ʲô��Ʒ�ģ�", "����м������뵶��ʲô���ͣ�", "����еĻ�ױƷ�ǳ�Ϊ��", "����в�Ʒ��������Ⱥ��", "����еĲ�Ʒ�ǵڼ�����", "����е����̻���ʲô�Ƶģ�" };
		String[] quizOptions = {
				"�޻�����ϵ��" + ConstVariables.FIELD_SEP + "��ʯ��ϵ��" + ConstVariables.FIELD_SEP + "���Խ�ʵ���ϵ��"
						+ ConstVariables.FIELD_SEP + "����ϵ��",
				"������������ˮ��" + ConstVariables.FIELD_SEP + "��������ˮ��" + ConstVariables.FIELD_SEP + "�������ʾ���˽��ˮ��ϵ��Һ������ˮ��"
						+ ConstVariables.FIELD_SEP + "��ϸ����ˮ��",
				"̰С��" + ConstVariables.FIELD_SEP + "��С��" + ConstVariables.FIELD_SEP + "ЦС��" + ConstVariables.FIELD_SEP
						+ "��С��",
				"�˫��ϵ��" + ConstVariables.FIELD_SEP + "ˮ���������ϵ��" + ConstVariables.FIELD_SEP + "����ˮ��������ϵ��"
						+ ConstVariables.FIELD_SEP + "���׼������ϵ��",
				"vivo X3���������ֻ�" + ConstVariables.FIELD_SEP + "I6�໨�������ֻ�" + ConstVariables.FIELD_SEP + "I4�໨�������ֻ�"
						+ ConstVariables.FIELD_SEP + "������",
				"����һ" + ConstVariables.FIELD_SEP + "���ٶ�" + ConstVariables.FIELD_SEP + "������" + ConstVariables.FIELD_SEP
						+ "������",
				"���ƿ" + ConstVariables.FIELD_SEP + "����ƿ" + ConstVariables.FIELD_SEP + "���ƿ" + ConstVariables.FIELD_SEP
						+ "���ƿ",
				"������" + ConstVariables.FIELD_SEP + "������" + ConstVariables.FIELD_SEP + "Ů����" + ConstVariables.FIELD_SEP
						+ "С����",
				"ؤ��Ŵ�" + ConstVariables.FIELD_SEP + "������" + ConstVariables.FIELD_SEP + "�ڶ���" + ConstVariables.FIELD_SEP
						+ "����ڴ�",
				"BOSS��" + ConstVariables.FIELD_SEP + "�ϰ���" + ConstVariables.FIELD_SEP + "CEO��"
						+ ConstVariables.FIELD_SEP + "����" };
		String[] quizAnswers = { "2", "1", "0", "3", "1", "2", "0", "3", "2", "1" };
		String[] adTitles = { "��ʫ�����¸���Խ�ʵ���ϵ��", "����ŷ������������ˮ��", "�ϵ»����Ŀ���ѡ����ؼ�", "����������ȡӯ��Һ", "�����������ֻ�I6�໨��",
				"�����������������뵶", "��װ����Olay���ƿ", "�ﱦ���ؼ�����", "SK��", "�ϰ���������̻�" };
		short[] adDurations = { (short) 30, (short) 15, (short) 30, (short) 15, (short) 15, (short) 30, (short) 15,
				(short) 30, (short) 15, (short) 15 };

		for (int i = 0; i < 10; i++) {
			String adIdStr = Integer.toString(i + 1);

			infoAd.setAdName(adTitles[i]);
			infoAd.setAdDesc(adTitles[i]);
			infoAd.setAdType((short) 0);
			infoAd.setAdStyle(ConstVariables.AD_STYLE_VIDEO);
			infoAd.setDeliveryType((short) 0);
			infoAd.setOfficialUrl("http://www.baidu.com");
			infoAd.setPricePlan("9,1:6,1:3,1$1;1,0$0");
			infoAd.setBudget(1000000);
			infoAd.setDuration(adDurations[i]);
			infoAd.setQuizList(adIdStr);
			infoAd.setCustId(0L);
			infoAd.setStatus(ConstVariables.STATUS_PASS);
			infoAd.setReason(null);
			infoAd.setEffDate(new Timestamp(0L));
			infoAd.setExpDate(new Timestamp(System.currentTimeMillis() + 86400000 * 365));
			infoAd.setVideo(adIdStr + ".mp4");
			infoAd.setWebView(null);
			infoAd.setImage(adIdStr + ".jpeg");
			infoAd.setCycles(null);
			infoAd.setExpire(null);
			infoAd.setOperId(0L);
			infoAd.setOperDate(new Timestamp(System.currentTimeMillis()));
			infoAdManager.save(infoAd);

			infoQuiz.setTitle(quizTitles[i]);
			infoQuiz.setQuizType(ConstVariables.QUIZ_TYPE_SELECT);
			infoQuiz.setOptions(quizOptions[i]);
			infoQuiz.setAnswers(quizAnswers[i]);
			infoQuiz.setAdId(infoAd.getAdId());
			infoQuizManager.save(infoQuiz);

			adBalance.setAdId(infoAd.getAdId());
			adBalance.setBudget(infoAd.getBudget());
			adBalance.setLastUpdate(new Timestamp(System.currentTimeMillis()));
			adBalance.setLocked(0L);
			adBalance.setRemain(infoAd.getBudget());
			adBalanceManager.save(adBalance);
		}
	}

	@Test
	public void testSavePromotions() {
		GlobalManager globalManager = GlobalManager.getInstance();
		InfoAdManager infoAdManager = InfoAdManager.getInstance(globalManager);
		InfoQuizManager infoQuizManager = InfoQuizManager.getInstance(globalManager);
		AdBalanceManager adBalanceManager = AdBalanceManager.getInstance();
		InfoAd infoAd = new InfoAd();
		InfoQuiz infoQuiz = new InfoQuiz();
		AdBalance adBalance = new AdBalance();

		int[] quizCount = { 3, 4 };
		String[] quizTitles = { "��ƽʱ���㲥��ʱ���ǣ�", "��ϲ����ʲô���͵Ĺ㲥��", "�뽫�Ҫ��Ĵ���д�ڴ˴�", "�������ɻ������", "���Ǽ�����������Ҫ�Ľ���յ��Ҹ���ʿ��",
				"��ϲ��ʲô���Ļ�ɴ��", "�뽫�Ҫ��Ĵ���д�ڴ˴�" };
		short[] quizTypes = { ConstVariables.QUIZ_TYPE_SELECT, ConstVariables.QUIZ_TYPE_SELECT,
				ConstVariables.QUIZ_TYPE_TEXT, ConstVariables.QUIZ_TYPE_SELECT, ConstVariables.QUIZ_TYPE_SELECT,
				ConstVariables.QUIZ_TYPE_SELECT, ConstVariables.QUIZ_TYPE_TEXT };
		String[] quizOptions = {
				"����06:00 ~ 12:00" + ConstVariables.FIELD_SEP + "����12:00 ~ 18:00" + ConstVariables.FIELD_SEP
						+ "����18:00 ~ 00:00" + ConstVariables.FIELD_SEP + "�賿 00:00 ~ 06:00",
				"������" + ConstVariables.FIELD_SEP + "������" + ConstVariables.FIELD_SEP + "������" + ConstVariables.FIELD_SEP
						+ "�����" + ConstVariables.FIELD_SEP + "����" + ConstVariables.FIELD_SEP + "����",
				null,
				"����" + ConstVariables.FIELD_SEP + "����",
				"��" + ConstVariables.FIELD_SEP + "��",
				"ѧԺ��" + ConstVariables.FIELD_SEP + "�߹��" + ConstVariables.FIELD_SEP + "�й���" + ConstVariables.FIELD_SEP
						+ "��Ц��", null };
		String[] quizAnswers = { null, null, null, null, "0", null, null };
		String[] adTitles = { "��HIT ������������Ƶ�� ˵��������", "2013��12��7-8�� ����չ���ݻ鲩��" };
		String[] adDescs = {
				"Hit FM���й����ʹ㲥��̨���µĹ�����������Ƶ�ʣ��ڱ�����������Ƶ��Ϊ88.7�׺գ��ڹ��ݵ�������Ƶ��Ϊ88.5�׺ա�ȫ��24Сʱ���������������������̳��С�ʱ�е����Ž����������ֳ�Ϊ��Ʒ��׷�����������õ��������Ʒ��",
				"2013�����鲩�Ὣ��2013��12��17-8���ڹ��һ��������ٶ�������Ⱦ��ף���ʱ��22�����ҡ�20����ҵ��1800�����ⶥ����Ʒ���꽫Ϊ����������˴���20�������Ʒ����Ʒ������������������2013���������鲩�ậ���˽������Ļ鷿���鳵�����Ρ����졢��ɴ��Ӱ����ɴ���������������ѡ�" };
		String[] prices = { "300,0$0", "20000,0$0" };
		String[] webViews = { "promotion_00001/HITFM/hitfm.html", "promotion_00002/wedding/wedding.html" };
		String[] images = { "promotion_00001/hit%20fm.png", "promotion_00002/wedding/wedding.png" };
		String[] cycles = { "һ��", "һ����" };

		for (int k = 0; k < 10; k++) {
			int quizStart = 0;
			for (int i = 0; i < quizCount.length; i++) {
				infoAd.setAdName(adTitles[i] + k);
				infoAd.setAdDesc(adDescs[i] + k);
				infoAd.setAdType((short) 0);
				infoAd.setAdStyle(ConstVariables.AD_STYLE_PROMOTION);
				infoAd.setDeliveryType((short) 0);
				infoAd.setOfficialUrl("http://www.baidu.com");
				infoAd.setPricePlan(prices[i]);
				infoAd.setBudget(1000000);
				infoAd.setDuration(null);
				infoAd.setCustId(0L);
				infoAd.setStatus(ConstVariables.STATUS_PASS);
				infoAd.setReason(null);
				infoAd.setEffDate(new Timestamp(0L));
				infoAd.setExpDate(new Timestamp(System.currentTimeMillis() + 86400000 * 365));
				infoAd.setVideo(null);
				infoAd.setWebView(webViews[i]);
				infoAd.setImage(images[i]);
				infoAd.setCycles(cycles[i]);
				infoAd.setExpire(3600L);
				infoAd.setOperId(0L);
				infoAd.setOperDate(new Timestamp(System.currentTimeMillis()));
				infoAdManager.save(infoAd);

				String adIdStr = "";
				for (int j = quizStart; j < quizStart + quizCount[i]; j++) {
					infoQuiz.setTitle(quizTitles[j]);
					infoQuiz.setQuizType(quizTypes[j]);
					infoQuiz.setOptions(quizOptions[j]);
					infoQuiz.setAnswers(quizAnswers[j]);
					if (j == quizStart)
						infoQuiz.setSkip(true);
					else
						infoQuiz.setSkip(false);
					infoQuiz.setAdId(infoAd.getAdId());
					infoQuizManager.save(infoQuiz);

					if (!adIdStr.isEmpty())
						adIdStr += ConstVariables.FIELD_SEP;

					adIdStr += infoQuiz.getQuizId();
				}
				quizStart += quizCount[i];

				infoAd.setQuizList(adIdStr);
				infoAdManager.update(infoAd);

				adBalance.setAdId(infoAd.getAdId());
				adBalance.setBudget(infoAd.getBudget());
				adBalance.setLastUpdate(new Timestamp(System.currentTimeMillis()));
				adBalance.setLocked(0L);
				adBalance.setRemain(infoAd.getBudget());
				adBalanceManager.save(adBalance);
			}
		}
	}
	
	@Test
	public void testSaveLocks() {
		GlobalManager globalManager = GlobalManager.getInstance();
		InfoAdManager infoAdManager = InfoAdManager.getInstance(globalManager);
		AdBalanceManager adBalanceManager = AdBalanceManager.getInstance();
		InfoAd infoAd = new InfoAd();
		AdBalance adBalance = new AdBalance();

		String[] adTitles = { "��ʫ�����¸���Խ�ʵ���ϵ��", "����ŷ������������ˮ��", "�ϵ»����Ŀ���ѡ����ؼ�", "����������ȡӯ��Һ", "�����������ֻ�I6�໨��",
				"�����������������뵶", "��װ����Olay���ƿ", "�ﱦ���ؼ�����", "SK��", "�ϰ���������̻�" };

		for (int i = 0; i < 10; i++) {
			String adIdStr = Integer.toString(i + 1);

			infoAd.setAdName(adTitles[i]);
			infoAd.setAdDesc(adTitles[i]);
			infoAd.setAdType((short) 0);
			infoAd.setAdStyle(ConstVariables.AD_STYLE_LOCK);
			infoAd.setDeliveryType((short) 0);
			infoAd.setOfficialUrl("http://www.baidu.com");
			infoAd.setPricePlan("3,0,9$0");
			infoAd.setBudget(1000000);
			infoAd.setDuration(null);
			infoAd.setQuizList(null);
			infoAd.setCustId(0L);
			infoAd.setStatus(ConstVariables.STATUS_PASS);
			infoAd.setReason(null);
			infoAd.setEffDate(new Timestamp(0L));
			infoAd.setExpDate(new Timestamp(System.currentTimeMillis() + 86400000 * 365));
			infoAd.setVideo(null);
			infoAd.setWebView(null);
			infoAd.setImage(adIdStr + ".jpeg");
			infoAd.setCycles(null);
			infoAd.setExpire(null);
			infoAd.setOperId(0L);
			infoAd.setOperDate(new Timestamp(System.currentTimeMillis()));
			infoAdManager.save(infoAd);

			adBalance.setAdId(infoAd.getAdId());
			adBalance.setBudget(infoAd.getBudget());
			adBalance.setLastUpdate(new Timestamp(System.currentTimeMillis()));
			adBalance.setLocked(0L);
			adBalance.setRemain(infoAd.getBudget());
			adBalanceManager.save(adBalance);
		}
	}
	
	@Test
	public void testSaveSplash() {
		GlobalManager globalManager = GlobalManager.getInstance();
		InfoAdManager infoAdManager = InfoAdManager.getInstance(globalManager);
		InfoAd infoAd = new InfoAd();

		String[] adTitles = { "��ʫ�����¸���Խ�ʵ���ϵ��", "����ŷ������������ˮ��", "�ϵ»����Ŀ���ѡ����ؼ�", "����������ȡӯ��Һ", "�����������ֻ�I6�໨��",
				"�����������������뵶", "��װ����Olay���ƿ", "�ﱦ���ؼ�����", "SK��", "�ϰ���������̻�" };

		for (int i = 0; i < 10; i++) {
			String adIdStr = Integer.toString(i + 1);

			infoAd.setAdName(adTitles[i]);
			infoAd.setAdDesc(adTitles[i]);
			infoAd.setAdType((short) 0);
			infoAd.setAdStyle(ConstVariables.AD_STYLE_SPLASH);
			infoAd.setDeliveryType((short) 0);
			infoAd.setOfficialUrl(null);
			infoAd.setPricePlan(null);
			infoAd.setBudget(1000000);
			infoAd.setDuration(null);
			infoAd.setQuizList(null);
			infoAd.setCustId(0L);
			infoAd.setStatus(ConstVariables.STATUS_PASS);
			infoAd.setReason(null);
			infoAd.setEffDate(new Timestamp(0L));
			infoAd.setExpDate(new Timestamp(System.currentTimeMillis() + 86400000 * 365));
			infoAd.setVideo(null);
			infoAd.setWebView(null);
			infoAd.setImage(adIdStr + ".jpeg");
			infoAd.setCycles(null);
			infoAd.setExpire(null);
			infoAd.setOperId(0L);
			infoAd.setOperDate(new Timestamp(System.currentTimeMillis()));
			infoAdManager.save(infoAd);
		}
	}

}
