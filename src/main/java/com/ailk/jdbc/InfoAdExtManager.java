package com.ailk.jdbc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.ailk.jdbc.cache.InfoAdExtValue;
import com.ailk.jdbc.cache.InfoAdKey;
import com.ailk.jdbc.entity.InfoAdExt;

/**
 * 广告扩展信息管理，该表将加载到私有内存
 * 
 * @author xugq
 * 
 */
public class InfoAdExtManager implements InMemCacheIfc, AlterTableIfc {

	private static final Logger logger = Logger.getLogger(InfoAdExtManager.class);

	private Map<InfoAdKey, List<InfoAdExtValue>> cache;

	/**
	 * 获取操作InfoAdExt表的实例
	 * 
	 * @return 操作InfoAdExt表的实例
	 */
	public static InfoAdExtManager getInstance(GlobalManager globalManager) {
		return globalManager.getInfoAdExtManager();
	}

	/**
	 * 构造函数
	 * 
	 * @param globalManager
	 *            全局管理对象
	 */
	private InfoAdExtManager(GlobalManager globalManager) {
		cache = new HashMap<InfoAdKey, List<InfoAdExtValue>>();
	}

	/**
	 * 获取广告扩展信息缓存
	 * 
	 * @return 广告信扩展息缓存
	 */
	public Map<InfoAdKey, List<InfoAdExtValue>> getCache() {
		return cache;
	}

	/**
	 * 从缓存查询广告信息
	 * 
	 * @param adId
	 *            广告ID
	 * @return 返回查询结果
	 */
	public List<InfoAdExtValue> get(int adId) {
		InfoAdKey key = new InfoAdKey();
		key.setAdId(adId);
		return cache.get(key);
	}

	/**
	 * 保存广告扩展信息到数据库
	 * 
	 * @param entity
	 *            广告扩展信息
	 */
	public void save(InfoAdExt entity) {
		int partition = HibernateUtil.getPartition(entity.getInfoAdExtPK().getAdId());
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
	 * 更新广告扩展信息到数据库
	 * 
	 * @param entity
	 *            广告扩展信息
	 */
	public void update(InfoAdExt entity) {
		int partition = HibernateUtil.getPartition(entity.getInfoAdExtPK().getAdId());
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
	 * 从数据库删除广告扩展信息
	 * 
	 * @param entity
	 *            广告扩展信息
	 */
	public void delete(InfoAdExt entity) {
		int partition = HibernateUtil.getPartition(entity.getInfoAdExtPK().getAdId());
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
	 * 加载数据到缓存
	 */
	@Override
	public void load() {
		synchronized (InfoAdExtManager.class) {
			for (int partition = 0; partition < HibernateUtil.getPartitions(); partition++) {
				Session session = HibernateUtil.getSessionFactory(partition).openSession();
				Iterator<?> iter = session.createQuery("FROM InfoAdExt ORDER BY infoAdExtPK.adId, infoAdExtPK.attrId")
						.iterate();

				while (iter.hasNext()) {
					InfoAdExt entity = (InfoAdExt) iter.next();
					InfoAdKey key = new InfoAdKey();
					key.setAdId(entity.getInfoAdExtPK().getAdId());

					List<InfoAdExtValue> items = cache.get(key);
					if (items == null) {
						items = new ArrayList<InfoAdExtValue>();
						cache.put(key, items);
					}

					InfoAdExtValue value = new InfoAdExtValue();
					value.setAttrId(entity.getInfoAdExtPK().getAttrId());
					value.setAttrValue(entity.getAttrValues());
					items.add(value);
				}

				session.close();
			}
		}

		logger.info("广告扩展信息表加载完毕，共" + cache.size() + "条记录");
	}

	/**
	 * 加载数据到缓存
	 * 
	 * @param globalManager
	 *            全局管理对象
	 */
	public static void load(GlobalManager globalManager) {
		InfoAdExtManager infoAdExtManager = new InfoAdExtManager(globalManager);

		infoAdExtManager.load();
		globalManager.setInfoAdExtManager(infoAdExtManager);
	}

	/**
	 * 修改表结构
	 */
	@Override
	public void alterTable() {
		logger.info("成功修改表：info_ad_ext");
	}

}
