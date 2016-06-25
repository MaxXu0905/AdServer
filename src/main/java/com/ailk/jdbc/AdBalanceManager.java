package com.ailk.jdbc;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.ailk.jdbc.entity.AdBalance;

/**
 * 广告余额管理
 * 
 * @author xugq
 * 
 */
public class AdBalanceManager implements AlterTableIfc {

	private static final Logger logger = Logger.getLogger(AdBalanceManager.class);
	private static AdBalanceManager instance = new AdBalanceManager();

	/**
	 * 获取操作广告余额表的实例
	 * 
	 * @return 操作广告余额表的实例
	 */
	public static AdBalanceManager getInstance() {
		return instance;
	}

	/**
	 * 构造函数
	 */
	private AdBalanceManager() {
	}

	/**
	 * 获取广告余额
	 * 
	 * @param adId
	 *            广告ID
	 * @return 广告余额
	 */
	public AdBalance get(int adId) {
		int partition = HibernateUtil.getPartition(adId);
		Session session = HibernateUtil.getSessionFactory(partition).openSession();

		try {
			return (AdBalance) session.get(AdBalance.class, adId);
		} finally {
			session.close();
		}
	}

	/**
	 * 保存广告余额到数据库
	 * 
	 * @param entity
	 *            广告余额
	 */
	public void save(AdBalance entity) {
		int partition = HibernateUtil.getPartition(entity.getAdId());
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
	 * 更新广告余额到数据库
	 * 
	 * @param entity
	 *            广告余额
	 */
	public void update(AdBalance entity) {
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
	 * 锁定广告余额
	 * 
	 * @param adId
	 *            广告ID
	 * @param locked
	 *            锁定余额
	 * @return 是否更新成功
	 */
	public boolean lock(int adId, long locked) {
		int partition = HibernateUtil.getPartition(adId);
		Session session = HibernateUtil.getSessionFactory(partition).openSession();
		Transaction t = session.beginTransaction();
		try {
			int rows = session
					.createQuery(
							"UPDATE AdBalance SET remain = remain - ?1, locked = locked + ?1 WHERE adId = ?2 AND remain >= ?1")
					.setLong("1", locked).setInteger("2", adId).executeUpdate();
			t.commit();
			return (rows > 0);
		} finally {
			if (t.isActive())
				t.rollback();
			session.close();
		}
	}

	/**
	 * 解锁广告余额
	 * 
	 * @param adId
	 *            广告ID
	 * @param unlocked
	 *            解锁余额
	 * @return 是否更新成功
	 */
	public boolean unlock(int adId, long unlocked) {
		int partition = HibernateUtil.getPartition(adId);
		Session session = HibernateUtil.getSessionFactory(partition).openSession();
		Transaction t = session.beginTransaction();
		try {
			int rows = session
					.createQuery("UPDATE AdBalance SET remain = remain + ?1, locked = locked - ?1 WHERE adId = ?2")
					.setLong("1", unlocked).setInteger("2", adId).executeUpdate();
			t.commit();
			return (rows > 0);
		} finally {
			if (t.isActive())
				t.rollback();
			session.close();
		}
	}

	/**
	 * 计费
	 * 
	 * @param adId
	 *            广告ID
	 * @param remain
	 *            余额调整
	 * @param locked
	 *            锁定调整
	 * @return 是否更新成功
	 */
	public boolean charge(int adId, long remain, long locked) {
		int partition = HibernateUtil.getPartition(adId);
		Session session = HibernateUtil.getSessionFactory(partition).openSession();
		Transaction t = session.beginTransaction();
		try {
			int rows;

			if (remain == 0) {
				rows = session.createQuery("UPDATE AdBalance SET locked = locked - ?1 WHERE adId = ?2")
						.setLong("1", locked).setInteger("2", adId).executeUpdate();
			} else if (locked == 0) {
				rows = session.createQuery("UPDATE AdBalance SET remain = remain - ?1 WHERE adId = ?2 AND remain > ?1")
						.setLong("1", remain).setInteger("2", adId).executeUpdate();
			} else {
				rows = session
						.createQuery(
								"UPDATE AdBalance SET remain = remain - ?1, locked = locked - ?2 WHERE adId = ?3 AND remain > ?1")
						.setLong("1", remain).setLong("2", locked).setInteger("3", adId).executeUpdate();
			}
			t.commit();
			return (rows > 0);
		} finally {
			if (t.isActive())
				t.rollback();
			session.close();
		}
	}

	/**
	 * 从数据库删除广告余额
	 * 
	 * @param entity
	 *            广告余额
	 */
	public void delete(AdBalance entity) {
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
	 * 修改表结构
	 */
	@Override
	public void alterTable() {
		logger.info("成功修改表：ad_balance");
	}

}
