package com.ailk.jdbc;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.ailk.jdbc.entity.UserPush;

/**
 * 用户推送管理
 * 
 * @author xugq
 * 
 */
public class UserPushManager implements AlterTableIfc {

	private static final Logger logger = Logger.getLogger(UserPushManager.class);
	private static UserPushManager instance = new UserPushManager();

	/**
	 * 获取操作用户推送表的实例
	 * 
	 * @return 操作用户推送表的实例
	 */
	public static UserPushManager getInstance() {
		return instance;
	}

	/**
	 * 构造函数
	 */
	private UserPushManager() {
	}

	/**
	 * 获取用户推送
	 * 
	 * @param devId
	 *            设备ID
	 * @return 返回结果
	 */
	public UserPush get(String devId) {
		int partition = HibernateUtil.getPartition(devId.hashCode());
		Session session = HibernateUtil.getSessionFactory(partition).openSession();
		try {
			return (UserPush) session.get(UserPush.class, devId);
		} finally {
			session.close();
		}
	}

	/**
	 * 保存用户推送
	 * 
	 * @param entity
	 *            用户推送对象
	 */
	public void save(UserPush entity) {
		int partition = HibernateUtil.getPartition(entity.getDevId().hashCode());
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
	 * 保存或更新用户推送
	 * 
	 * @param entity
	 *            用户推送对象
	 */
	public void saveOrUpdate(UserPush entity) {
		int partition = HibernateUtil.getPartition(entity.getDevId().hashCode());
		Session session = HibernateUtil.getSessionFactory(partition).openSession();
		Transaction t = session.beginTransaction();
		try {
			session.saveOrUpdate(entity);
			t.commit();
		} finally {
			if (t.isActive())
				t.rollback();
			session.close();
		}
	}

	/**
	 * 更新用户推送
	 * 
	 * @param entity
	 *            用户对象
	 */
	public void update(UserPush entity) {
		int partition = HibernateUtil.getPartition(entity.getDevId().hashCode());
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
	 * 删除用户推送
	 * 
	 * @param entity
	 *            用户推送对象
	 */
	public void delete(UserPush entity) {
		int partition = HibernateUtil.getPartition(entity.getDevId().hashCode());
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
		logger.info("成功修改表：user_push");
	}

}
