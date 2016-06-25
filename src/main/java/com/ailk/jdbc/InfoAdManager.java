package com.ailk.jdbc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.ailk.api.UserIfc;
import com.ailk.common.ConstVariables;
import com.ailk.jdbc.cache.InfoAdExtValue;
import com.ailk.jdbc.cache.InfoAdKey;
import com.ailk.jdbc.cache.InfoAdValue;
import com.ailk.jdbc.entity.InfoAd;
import com.ailk.jdbc.entity.InfoAdExt;
import com.ailk.jdbc.entity.InfoAdExtPK;
import com.ailk.jdbc.entity.InfoQuiz;

/**
 * 广告信息管理对象，由于广告信息需要大量访问，而且数据量不大，因此保存到私有内存中
 * 
 * @author xugq
 * 
 */
public class InfoAdManager implements InMemCacheIfc, AlterTableIfc {

	private static final Logger logger = Logger.getLogger(InfoAdManager.class);

	private GlobalManager globalManager;
	private TreeMap<InfoAdKey, InfoAdValue> cache; // 广告缓存对象
	private Map<Short, List<UserIfc.AdItem>> defaultAds; // 默认广告列表

	/**
	 * 获取操作广告信息表的实例
	 * 
	 * @return 操作广告信息表的实例
	 */
	public static InfoAdManager getInstance(GlobalManager globalManager) {
		return globalManager.getInfoAdManager();
	}

	public List<UserIfc.AdItem> getDefaultAds(short adStyle) {
		return defaultAds.get(adStyle);
	}

	/**
	 * 构造函数
	 * 
	 * @param globalManager
	 *             全局管理对象
	 */
	private InfoAdManager(GlobalManager globalManager) {
		this.globalManager = globalManager;
		cache = new TreeMap<InfoAdKey, InfoAdValue>();
		defaultAds = new HashMap<Short, List<UserIfc.AdItem>>();
	}

	/**
	 * 获取广告信息缓存
	 * 
	 * @return 广告信息缓存
	 */
	public TreeMap<InfoAdKey, InfoAdValue> getCache() {
		return cache;
	}

	/**
	 * 从缓存查询广告信息
	 * 
	 * @param adId
	 *            广告ID
	 * @return 广告详情，不存在则返回null
	 */
	public InfoAdValue get(int adId) {
		InfoAdKey key = new InfoAdKey();
		key.setAdId(adId);

		return cache.get(key);
	}

	/**
	 * 检查广告是否存在
	 * 
	 * @param adId
	 *            广告ID
	 * @return 是否存在
	 */
	public boolean contains(int adId) {
		InfoAdKey key = new InfoAdKey();
		key.setAdId(adId);

		return cache.containsKey(key);
	}

	/**
	 * 从数据库查询广告信息
	 * 
	 * @param adId
	 *            广告ID
	 * @return 广告详情
	 */
	public InfoAd byId(int adId) {
		int partition = HibernateUtil.getPartition(adId);
		Session session = HibernateUtil.getSessionFactory(partition).openSession();

		try {
			return (InfoAd) session.byId(InfoAd.class).load(adId);
		} finally {
			session.close();
		}
	}

	/**
	 * 广告概要信息
	 * 
	 * @author xugq
	 * 
	 */
	public static class AdSummary {
		private int adId; // 广告ID
		private String adName; // 广告名称
		private String adDesc; // 广告描述
		private short adStyle; // 广告形式
		private String image; // 图片
		private String desc; // 描述

		public void set(InfoAd infoAd) {
			adId = infoAd.getAdId();
			adName = infoAd.getAdName();
			adDesc = infoAd.getAdDesc();
			adStyle = infoAd.getAdStyle();
			image = infoAd.getImage();
			desc = ConstVariables.getStatusDesc(infoAd.getStatus());
		}

		public int getAdId() {
			return adId;
		}

		public void setAdId(int adId) {
			this.adId = adId;
		}

		public String getAdName() {
			return adName;
		}

		public void setAdName(String adName) {
			this.adName = adName;
		}

		public String getAdDesc() {
			return adDesc;
		}

		public void setAdDesc(String adDesc) {
			this.adDesc = adDesc;
		}

		public short getAdStyle() {
			return adStyle;
		}

		public void setAdStyle(short adStyle) {
			this.adStyle = adStyle;
		}

		public String getImage() {
			return image;
		}

		public void setImage(String image) {
			this.image = image;
		}

		public String getDesc() {
			return desc;
		}

		public void setDesc(String desc) {
			this.desc = desc;
		}
	}

	/**
	 * 保存广告信息到数据库
	 * 
	 * @param entity
	 *            广告信息
	 */
	public void save(InfoAd entity) {
		int partition = HibernateUtil.getPartition(entity.getCustId());
		Session session = HibernateUtil.getSessionFactory(partition).openSession();
		Transaction t = session.beginTransaction();
		try {
			session.save(entity);
			t.commit();
		} finally {
			if (t.isActive())
				t.rollback();
			session.close();
		}
	}

	/**
	 * 更新广告信息到数据库
	 * 
	 * @param entity
	 *            广告信息
	 */
	public void update(InfoAd entity) {
		int partition = HibernateUtil.getPartition(entity.getAdId());
		Session session = HibernateUtil.getSessionFactory(partition).openSession();
		Transaction t = session.beginTransaction();
		try {
			session.update(entity);
			t.commit();
		} finally {
			if (t.isActive())
				t.rollback();
			session.close();
		}
	}

	/**
	 * 从数据库删除广告信息
	 * 
	 * @param entity
	 *            广告信息
	 */
	public void delete(InfoAd entity) {
		int partition = HibernateUtil.getPartition(entity.getAdId());
		Session session = HibernateUtil.getSessionFactory(partition).openSession();
		Transaction t = session.beginTransaction();
		try {
			session.delete(entity);
			t.commit();
		} finally {
			if (t.isActive())
				t.rollback();
			session.close();
		}
	}

	/**
	 * 保存广告扩展信息到数据库
	 * 
	 * @param infoAd
	 *            广告信息
	 * @param quizList
	 *            问题列表
	 * @param extValues
	 *            扩展信息列表
	 */
	public void save(InfoAd infoAd, List<InfoQuiz> quizList, List<InfoAdExtValue> extValues) {
		StringBuilder builder = new StringBuilder();
		boolean first = true;
		int partition = HibernateUtil.getPartition(infoAd.getCustId());
		Session session = HibernateUtil.getSessionFactory(partition).openSession();
		Transaction t = session.beginTransaction();

		try {
			if (infoAd.getAdId() >= 0) {
				session.createQuery("DELETE FROM InfoQuiz WHERE adId = ?1").setLong("1", infoAd.getAdId())
						.executeUpdate();

				session.createQuery("DELETE FROM InfoAdExt WHERE infoAdExtPK.adId = ?1").setLong("1", infoAd.getAdId())
						.executeUpdate();
			} else {
				session.save(infoAd);
			}

			if (quizList != null) {
				for (InfoQuiz quiz : quizList) {
					quiz.setAdId(infoAd.getAdId());
					session.save(quiz);
					if (first)
						first = false;
					else
						builder.append(",");
					builder.append(quiz.getQuizId());
				}
			}

			infoAd.setQuizList(builder.toString());
			session.update(infoAd);

			if (extValues != null) {
				InfoAdExt infoAdExt = new InfoAdExt();
				InfoAdExtPK infoAdExtPK = new InfoAdExtPK();
				infoAdExt.setInfoAdExtPK(infoAdExtPK);
				infoAdExtPK.setAdId(infoAd.getAdId());

				for (InfoAdExtValue extValue : extValues) {
					infoAdExtPK.setAttrId(extValue.getAttrId());
					infoAdExt.setAttrValues(extValue.getAttrValue());

					session.save(infoAdExt);
				}
			}

			t.commit();
		} finally {
			if (t.isActive())
				t.rollback();
			session.close();
		}
	}

	/**
	 * 加载数据到缓存
	 */
	@Override
	public void load() {
		synchronized (InfoAdManager.class) {
			// 加载数据
			for (int partition = 0; partition < HibernateUtil.getPartitions(); partition++) {
				Session session = HibernateUtil.getSessionFactory(partition).openSession();
				Iterator<?> iter = session.createQuery("FROM InfoAd").iterate();

				while (iter.hasNext()) {
					InfoAd entity = (InfoAd) iter.next();
					InfoAdKey key = new InfoAdKey();
					InfoAdValue value = new InfoAdValue();

					key.set(entity);
					value.set(globalManager, entity);
					cache.put(key, value);

					List<UserIfc.AdItem> adItems = defaultAds.get(entity.getAdStyle());
					if (adItems == null) {
						adItems = new ArrayList<UserIfc.AdItem>();
						defaultAds.put(entity.getAdStyle(), adItems);
					}

					UserIfc.AdItem adItem = new UserIfc.AdItem();
					adItem.setAdId(entity.getAdId());
					adItem.setDays(0);
					adItem.setTimes(0);
					adItems.add(adItem);
				}

				session.close();
			}
		}

		logger.info("广告信息表加载完毕，共" + cache.size() + "条记录");
	}

	/**
	 * 加载数据到缓存
	 * 
	 * @param globalManager
	 *            全局管理对象
	 */
	public static void load(GlobalManager globalManager) {
		InfoAdManager infoAdManager = new InfoAdManager(globalManager);

		infoAdManager.load();
		globalManager.setInfoAdManager(infoAdManager);
	}

	/**
	 * 修改表结构
	 */
	@Override
	public void alterTable() {
		for (int partition = 0; partition < HibernateUtil.getPartitions(); partition++) {
			Session session = HibernateUtil.getSessionFactory(partition).openSession();

			try {
				String sql = "alter table info_ad AUTO_INCREMENT=" + (partition << 56);
				session.createSQLQuery(sql).executeUpdate();

				sql = "create index idx_info_ad_cust_id on info_ad(cust_id)";
				session.createSQLQuery(sql).executeUpdate();
			} catch (Exception e) {
				logger.error(e);
			}
		}

		logger.info("成功修改表：info_ad");
	}

}
