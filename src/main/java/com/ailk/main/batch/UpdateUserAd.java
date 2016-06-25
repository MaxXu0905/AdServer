package com.ailk.main.batch;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.ailk.common.ConstVariables;
import com.ailk.common.GlobalVariables;
import com.ailk.jdbc.GlobalManager;
import com.ailk.jdbc.HibernateUtil;
import com.ailk.jdbc.InfoAdManager;
import com.ailk.jdbc.cache.InfoAdKey;
import com.ailk.jdbc.cache.InfoAdValue;
import com.ailk.jdbc.entity.AdBalance;
import com.ailk.jdbc.entity.InfoUser;
import com.ailk.jdbc.entity.UserAd;
import com.ailk.jdbc.entity.UserAdPK;
import com.ailk.jdbc.entity.UserAdStatus;
import com.ailk.main.batch.UserAdManager.Key;
import com.ailk.main.batch.UserAdManager.Value;

/**
 * �����û��ڶ�����Թۿ��Ĺ���б��ý�����Ҫ��ÿ���ڵ�ִ�У���������ÿ��11��ǰ���
 * 
 * @author xugq
 * 
 */
public class UpdateUserAd {

	public static void main(String[] args) {
		GlobalManager globalManager = GlobalManager.getInstance();
		UpdateUserAd instance = new UpdateUserAd();

		instance.init(globalManager);
		instance.update(globalManager);
		instance.updateNew();
	}

	private Session session;
	private Map<Integer, AdBalance> adBalanceMap;
	private Map<Long, Long> userIdMap;
	private TreeMap<InfoAdKey, InfoAdValue> cache;
	private Timestamp lastUpdate;
	private long sysdate;
	private UserAdManager userAdManager;

	private void init(GlobalManager globalManager) {
		int partition = HibernateUtil.getPartition();
		session = HibernateUtil.getSessionFactory(partition).openSession();

		// �����û��б�
		loadInfoUser();

		// ���ع�����
		loadAdBalance();

		// ���ع����Ϣ��
		InfoAdManager infoAdManager = InfoAdManager.getInstance(globalManager);
		infoAdManager.load();
		cache = infoAdManager.getCache();

		// ���ò���ʱ��
		lastUpdate = new Timestamp(System.currentTimeMillis());
		sysdate = GlobalVariables.sysdate;

		userAdManager = new UserAdManager(session, lastUpdate, sysdate);
	}

	// �����û��б�
	private void loadInfoUser() {
		userIdMap = new HashMap<Long, Long>();

		Iterator<?> iter = session.createQuery("FROM InfoUser").iterate();
		while (iter.hasNext()) {
			InfoUser infoUser = (InfoUser) iter.next();

			userIdMap.put(infoUser.getUserId(), infoUser.getUserId());
		}
	}

	// ���ع�����
	private void loadAdBalance() {
		adBalanceMap = new HashMap<Integer, AdBalance>();

		Iterator<?> iter = session.createQuery("FROM AdBalance").iterate();
		while (iter.hasNext()) {
			AdBalance adBalance = (AdBalance) iter.next();

			adBalanceMap.put(adBalance.getAdId(), adBalance);
		}
	}

	// ���¹���б�
	private void update(GlobalManager globalManager) {
		Map<Key, Value> map = new HashMap<Key, Value>();
		InfoAdManager infoAdManager = InfoAdManager.getInstance(globalManager);

		long lastUserId = -1;
		Iterator<?> iter = session.createQuery("FROM UserAdStatus ORDER BY userAdStatusPK.userId").iterate();
		while (iter.hasNext()) {
			UserAdStatus userAdStatus = (UserAdStatus) iter.next();

			// ��ȡ�����Ϣ
			InfoAdValue infoAdValue = infoAdManager.get(userAdStatus.getUserAdStatusPK().getAdId());
			if (infoAdValue == null)
				continue;

			if (infoAdValue.getStatus() != ConstVariables.STATUS_PASS)
				continue;

			if (lastUserId == -1) {
				// ��һ��
				lastUserId = userAdStatus.getUserAdStatusPK().getUserId();
			} else if (lastUserId != userAdStatus.getUserAdStatusPK().getUserId()) {
				// �л�������û�
				userAdManager.save(globalManager, lastUserId, map);

				// ����
				lastUserId = userAdStatus.getUserAdStatusPK().getUserId();
				map.clear();
			}

			// ���ü�
			Key key = new Key();
			key.setAdId(userAdStatus.getUserAdStatusPK().getAdId());

			// ����ֵ
			Value value = new Value();
			value.setDays(userAdStatus.getDays());
			if (infoAdValue.getAdStyle() != ConstVariables.AD_STYLE_LOCK)
				value.setTimes(userAdStatus.getTimes());
			else
				value.setTimes(userAdStatus.getTimes2());

			// �ӵ��б�
			map.put(key, value);
		}

		if (lastUserId != -1)
			userAdManager.save(globalManager, lastUserId, map);
	}

	// �������û��Ĺ���б�
	private void updateNew() {
		Map<Short, StringBuilder> builders = new HashMap<Short, StringBuilder>();

		for (long userId : userIdMap.values()) {
			for (Entry<InfoAdKey, InfoAdValue> entry : cache.entrySet()) {
				InfoAdKey infoAdKey = entry.getKey();
				InfoAdValue infoAdValue = entry.getValue();

				short adStyle = infoAdValue.getAdStyle();
				StringBuilder builder = builders.get(adStyle);
				if (builder == null) {
					builder = new StringBuilder();
					builders.put(adStyle, builder);
				} else {
					builder.append(ConstVariables.FIELD_SEP);
				}

				builder.append(infoAdKey.getAdId());
				builder.append(ConstVariables.FIELD_SEP);
				builder.append(0);
				builder.append(ConstVariables.FIELD_SEP);
				builder.append(0);
			}

			Transaction t = session.beginTransaction();
			try {
				for (Entry<Short, StringBuilder> entry : builders.entrySet()) {
					UserAdPK userAdPK = new UserAdPK();
					userAdPK.setUserId(userId);
					userAdPK.setAdStyle(entry.getKey());

					String ads = entry.getValue().toString();
					UserAd userAd = (UserAd) session.get(UserAd.class, userAdPK);
					if (userAd == null) {
						userAd = new UserAd();
						userAd.setUserAdPK(userAdPK);
						userAd.setOldAds(null);
						userAd.setAds(ads);
						userAd.setLastUpdate(lastUpdate);
						userAd.setSysdate(sysdate);

						session.save(userAd);
					} else {
						if (sysdate > userAd.getSysdate()) {
							userAd.setOldAds(userAd.getAds());
							userAd.setAds(ads);
							userAd.setLastUpdate(lastUpdate);
							userAd.setSysdate(sysdate);
						} else {
							userAd.setAds(ads);
							userAd.setLastUpdate(lastUpdate);
						}

						session.update(userAd);
					}
				}

				t.commit();
			} finally {
				if (t.isActive())
					t.rollback();
			}
		}
	}

}
