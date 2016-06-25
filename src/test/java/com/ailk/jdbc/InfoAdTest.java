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

		String[] quizTitles = { "广告中雅诗兰黛护肤属于什么系列的？", "广告中巴黎欧莱雅眼线是什么性质的？", "广告中买肯德基套餐赠送的喵小奇不包括？", "广告中珀莱雅护肤属于什么系列的？",
				"广告中步步高什么产品的？", "广告中吉利剃须刀是什么类型？", "广告中的化妆品昵称为？", "广告中产品适用于人群？", "广告中的产品是第几代？", "广告中的油烟机是什么牌的？" };
		String[] quizOptions = {
				"修护精华系列" + ConstVariables.FIELD_SEP + "红石榴系列" + ConstVariables.FIELD_SEP + "弹性紧实柔肤系列"
						+ ConstVariables.FIELD_SEP + "滋润系列",
				"美眸深邃眼线水笔" + ConstVariables.FIELD_SEP + "流畅眼线水笔" + ConstVariables.FIELD_SEP + "魅力焕彩巨星私藏水晶系列液体眼线水笔"
						+ ConstVariables.FIELD_SEP + "极细眼线水笔",
				"贪小奇" + ConstVariables.FIELD_SEP + "娇小奇" + ConstVariables.FIELD_SEP + "笑小奇" + ConstVariables.FIELD_SEP
						+ "呆小奇",
				"深海双藻系列" + ConstVariables.FIELD_SEP + "水漾肌蜜柔肤系列" + ConstVariables.FIELD_SEP + "海洋水动力护肤系列"
						+ ConstVariables.FIELD_SEP + "靓白肌蜜柔肤系列",
				"vivo X3智能音乐手机" + ConstVariables.FIELD_SEP + "I6青花瓷音乐手机" + ConstVariables.FIELD_SEP + "I4青花瓷音乐手机"
						+ ConstVariables.FIELD_SEP + "复读机",
				"锋速一" + ConstVariables.FIELD_SEP + "锋速二" + ConstVariables.FIELD_SEP + "锋速三" + ConstVariables.FIELD_SEP
						+ "锋速四",
				"大红瓶" + ConstVariables.FIELD_SEP + "大绿瓶" + ConstVariables.FIELD_SEP + "大黄瓶" + ConstVariables.FIELD_SEP
						+ "大黑瓶",
				"老婆婆" + ConstVariables.FIELD_SEP + "怪蜀黍" + ConstVariables.FIELD_SEP + "女汉子" + ConstVariables.FIELD_SEP
						+ "小宝宝",
				"丐帮九袋" + ConstVariables.FIELD_SEP + "富二代" + ConstVariables.FIELD_SEP + "第二代" + ConstVariables.FIELD_SEP
						+ "柔道黑带",
				"BOSS牌" + ConstVariables.FIELD_SEP + "老板牌" + ConstVariables.FIELD_SEP + "CEO牌"
						+ ConstVariables.FIELD_SEP + "雕牌" };
		String[] quizAnswers = { "2", "1", "0", "3", "1", "2", "0", "3", "2", "1" };
		String[] adTitles = { "雅诗兰黛新概念弹性紧实柔肤系列", "巴黎欧莱雅流畅眼线水笔", "肯德基把四款“萌友”带回家", "珀莱雅藻萃取盈养液", "步步高音乐手机I6青花瓷",
				"吉利锋速三敏锐剃须刀", "新装升级Olay大红瓶", "帮宝适特级棉柔", "SKⅡ", "老板大吸力油烟机" };
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
		String[] quizTitles = { "您平时听广播的时间是？", "您喜欢听什么类型的广播？", "请将活动要求的答案填写在此处", "您是新郎或者新娘？", "您是即将结婚或者想要拍结婚照的幸福人士吗？",
				"您喜欢什么风格的婚纱？", "请将活动要求的答案填写在此处" };
		short[] quizTypes = { ConstVariables.QUIZ_TYPE_SELECT, ConstVariables.QUIZ_TYPE_SELECT,
				ConstVariables.QUIZ_TYPE_TEXT, ConstVariables.QUIZ_TYPE_SELECT, ConstVariables.QUIZ_TYPE_SELECT,
				ConstVariables.QUIZ_TYPE_SELECT, ConstVariables.QUIZ_TYPE_TEXT };
		String[] quizOptions = {
				"上午06:00 ~ 12:00" + ConstVariables.FIELD_SEP + "下午12:00 ~ 18:00" + ConstVariables.FIELD_SEP
						+ "晚上18:00 ~ 00:00" + ConstVariables.FIELD_SEP + "凌晨 00:00 ~ 06:00",
				"新闻类" + ConstVariables.FIELD_SEP + "音乐类" + ConstVariables.FIELD_SEP + "故事类" + ConstVariables.FIELD_SEP
						+ "广告类" + ConstVariables.FIELD_SEP + "洁柔" + ConstVariables.FIELD_SEP + "其他",
				null,
				"新郎" + ConstVariables.FIELD_SEP + "新娘",
				"是" + ConstVariables.FIELD_SEP + "否",
				"学院风" + ConstVariables.FIELD_SEP + "高贵风" + ConstVariables.FIELD_SEP + "中国风" + ConstVariables.FIELD_SEP
						+ "搞笑风", null };
		String[] quizAnswers = { null, null, null, null, "0", null, null };
		String[] adTitles = { "听HIT 国际流行音乐频道 说歌曲名称", "2013年12月7-8日 北京展览馆婚博会" };
		String[] adDescs = {
				"Hit FM是中国国际广播电台旗下的国际流行音乐频率，在北京地区播出频率为88.7兆赫，在广州地区播出频率为88.5兆赫。全天24小时滚动播出当今国际流行乐坛最动感、时尚的热门金曲，让音乐成为高品质追求者随身享用的生活必需品。",
				"2013北京婚博会将于2013年12月17-8日在国家会议中心再度上演年度巨献，届时，22个国家、20个行业、1800家中外顶级名品名店将为北京结婚新人带来20万款结婚新品，产品囊括您结婚的所有需求。2013冬季北京婚博会涵盖了结婚所需的婚房、婚车、旅游、婚庆、婚纱摄影、婚纱礼服、婚戒等相关消费。" };
		String[] prices = { "300,0$0", "20000,0$0" };
		String[] webViews = { "promotion_00001/HITFM/hitfm.html", "promotion_00002/wedding/wedding.html" };
		String[] images = { "promotion_00001/hit%20fm.png", "promotion_00002/wedding/wedding.png" };
		String[] cycles = { "一周", "一个月" };

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

		String[] adTitles = { "雅诗兰黛新概念弹性紧实柔肤系列", "巴黎欧莱雅流畅眼线水笔", "肯德基把四款“萌友”带回家", "珀莱雅藻萃取盈养液", "步步高音乐手机I6青花瓷",
				"吉利锋速三敏锐剃须刀", "新装升级Olay大红瓶", "帮宝适特级棉柔", "SKⅡ", "老板大吸力油烟机" };

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

		String[] adTitles = { "雅诗兰黛新概念弹性紧实柔肤系列", "巴黎欧莱雅流畅眼线水笔", "肯德基把四款“萌友”带回家", "珀莱雅藻萃取盈养液", "步步高音乐手机I6青花瓷",
				"吉利锋速三敏锐剃须刀", "新装升级Olay大红瓶", "帮宝适特级棉柔", "SKⅡ", "老板大吸力油烟机" };

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
