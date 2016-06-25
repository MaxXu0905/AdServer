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
import com.ailk.jdbc.GlobalManager;
import com.ailk.jdbc.InfoAdManager;
import com.ailk.jdbc.cache.InfoAdKey;
import com.ailk.jdbc.cache.InfoAdValue;
import com.ailk.jdbc.entity.UserAd;
import com.ailk.jdbc.entity.UserAdPK;
import com.ailk.jdbc.entity.UserAdStatus;

/**
 * 更新用户第二天可以观看的广告列表，该进程需要在每个节点执行，并且需在每晚11点前完成
 * 
 * @author xugq
 * 
 */
public class UserAdManager {

	private Session session;
	private Timestamp lastUpdate;
	private long sysdate;

	static class Key {
		private int adId;

		public int getAdId() {
			return adId;
		}

		public void setAdId(int adId) {
			this.adId = adId;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + adId;
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Key other = (Key) obj;
			if (adId != other.adId)
				return false;
			return true;
		}
	}

	static class Value {
		private int days;
		private int times;

		public int getDays() {
			return days;
		}

		public void setDays(int days) {
			this.days = days;
		}

		public int getTimes() {
			return times;
		}

		public void setTimes(int times) {
			this.times = times;
		}
	}

	public UserAdManager(Session session, Timestamp lastUpdate, long sysdate) {
		this.session = session;
		this.lastUpdate = lastUpdate;
		this.sysdate = sysdate;
	}

	// 更新广告列表
	public void update(GlobalManager globalManager, long userId) {
		Map<Key, Value> map = new HashMap<Key, Value>();
		InfoAdManager infoAdManager = InfoAdManager.getInstance(globalManager);

		Iterator<?> iter = session.createQuery("FROM UserAdStatus WHERE userAdStatusPK.userId = ?1")
				.setLong("1", userId).iterate();
		while (iter.hasNext()) {
			UserAdStatus userAdStatus = (UserAdStatus) iter.next();

			// 获取广告信息
			InfoAdValue infoAdValue = infoAdManager.get(userAdStatus.getUserAdStatusPK().getAdId());
			if (infoAdValue == null)
				continue;

			if (infoAdValue.getStatus() != ConstVariables.STATUS_PASS)
				continue;

			// 设置键
			Key key = new Key();
			key.setAdId(userAdStatus.getUserAdStatusPK().getAdId());

			// 设置值
			Value value = new Value();
			value.setDays(userAdStatus.getDays());
			if (infoAdValue.getAdStyle() != ConstVariables.AD_STYLE_LOCK)
				value.setTimes(userAdStatus.getTimes());
			else
				value.setTimes(userAdStatus.getTimes2());

			// 加到列表
			map.put(key, value);
		}

		save(globalManager, userId, map);
	}

	// 保存用户的广告列表
	public void save(GlobalManager globalManager, long userId, Map<Key, Value> map) {
		Map<Short, StringBuilder> builders = new HashMap<Short, StringBuilder>();
		Key key = new Key();

		InfoAdManager infoAdManager = InfoAdManager.getInstance(globalManager);
		TreeMap<InfoAdKey, InfoAdValue> cache = infoAdManager.getCache();

		// 先遍历第一次观看的情况
		for (Entry<InfoAdKey, InfoAdValue> entry : cache.entrySet()) {
			InfoAdKey infoAdKey = entry.getKey();
			InfoAdValue infoAdValue = entry.getValue();

			key.setAdId(infoAdKey.getAdId());
			Value value = map.get(key);

			if (value != null)
				continue;

			short adStyle = infoAdValue.getAdStyle();
			StringBuilder builder = builders.get(adStyle);
			if (builder == null) {
				builder = new StringBuilder();
				builders.put(adStyle, builder);
			} else {
				builder.append(ConstVariables.FIELD_SEP);
			}

			builder.append(key.getAdId());
			builder.append(ConstVariables.FIELD_SEP);
			builder.append(0);
			builder.append(ConstVariables.FIELD_SEP);
			builder.append(0);
		}

		// 遍历已观看的情况
		InfoAdKey infoAdKey = new InfoAdKey();
		for (Entry<Key, Value> entry : map.entrySet()) {
			key = entry.getKey();
			Value value = entry.getValue();

			infoAdKey.setAdId(key.getAdId());
			InfoAdValue infoAdValue = cache.get(infoAdKey);

			// 可能已下架
			if (infoAdValue == null)
				continue;

			short adStyle = infoAdValue.getAdStyle();
			StringBuilder builder = builders.get(adStyle);
			if (builder == null) {
				builder = new StringBuilder();
				builders.put(adStyle, builder);
			} else {
				builder.append(ConstVariables.FIELD_SEP);
			}

			builder.append(key.getAdId());
			builder.append(ConstVariables.FIELD_SEP);
			builder.append(value.getDays() + 1);
			builder.append(ConstVariables.FIELD_SEP);
			builder.append(value.getTimes());
		}

		// 执行数据库操作
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
