package com.ailk.jdbc;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.ailk.jdbc.entity.InfoUser;

/**
 * 用户信息管理
 * 
 * @author xugq
 * 
 */
public class InfoUserManager implements AlterTableIfc {

	private static final Logger logger = Logger.getLogger(InfoUserManager.class);
	private static InfoUserManager instance = new InfoUserManager();

	/**
	 * 获取操作用户信息表的实例
	 * 
	 * @return 操作用户信息表的实例
	 */
	public static InfoUserManager getInstance() {
		return instance;
	}

	/**
	 * 构造函数
	 */
	private InfoUserManager() {
	}

	/**
	 * 获取用户信息
	 * 
	 * @param userId
	 *            用户ID
	 * @return 返回结果
	 */
	public InfoUser get(long userId) {
		int partition = HibernateUtil.getPartition(userId);
		Session session = HibernateUtil.getSessionFactory(partition).openSession();
		try {
			return (InfoUser) session.get(InfoUser.class, userId);
		} finally {
			session.close();
		}
	}

	/**
	 * 保存用户信息
	 * 
	 * @param entity
	 *            用户信息对象
	 */
	public void save(InfoUser entity) {
		int partition = HibernateUtil.getPartition();
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
	 * 更新用户信息
	 * 
	 * @param entity
	 *            用户对象
	 */
	public void update(InfoUser entity) {
		int partition = HibernateUtil.getPartition(entity.getUserId());
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
	 * 删除用户信息
	 * 
	 * @param entity
	 *            用户信息对象
	 */
	public void delete(InfoUser entity) {
		int partition = HibernateUtil.getPartition(entity.getUserId());
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
		for (int partition = 0; partition < HibernateUtil.getPartitions(); partition++) {
			Session session = HibernateUtil.getSessionFactory(partition).openSession();

			try {
				String sql = "alter table info_user AUTO_INCREMENT=" + (partition << 56);
				session.createSQLQuery(sql).executeUpdate();
			} catch (Exception e) {
				logger.error(e);
			}
		}

		logger.info("成功修改表：info_user");
	}

}
