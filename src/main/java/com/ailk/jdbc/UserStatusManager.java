package com.ailk.jdbc;

import org.apache.log4j.Logger;
import org.hibernate.LockOptions;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.ailk.jdbc.entity.UserStatus;

/**
 * 用户状态管理
 * 
 * @author xugq
 * 
 */
public class UserStatusManager implements AlterTableIfc {

	private static final Logger logger = Logger.getLogger(UserStatusManager.class);
	private static UserStatusManager instance = new UserStatusManager();

	/**
	 * 获取操作用户状态表的实例
	 * 
	 * @return 操作用户状态表的实例
	 */
	public static UserStatusManager getInstance() {
		return instance;
	}

	/**
	 * 构造函数
	 */
	private UserStatusManager() {
	}

	/**
	 * 获取用户状态
	 * 
	 * @param session
	 *            会话
	 * @param userId
	 *            用户ID
	 * @return 返回结果
	 */
	public UserStatus get(Session session, long userId, LockOptions lockOptions) {
		return (UserStatus) session.get(UserStatus.class, userId, lockOptions);
	}

	/**
	 * 获取用户状态
	 * 
	 * @param userId
	 *            用户ID
	 * @return 返回结果
	 */
	public UserStatus get(long userId) {
		int partition = HibernateUtil.getPartition(userId);
		Session session = HibernateUtil.getSessionFactory(partition).openSession();
		try {
			return (UserStatus) session.get(UserStatus.class, userId);
		} finally {
			session.close();
		}
	}

	/**
	 * 保存用户状态
	 * 
	 * @param entity
	 *            用户状态对象
	 */
	public void save(UserStatus entity) {
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
	 * 更新用户状态
	 * 
	 * @param entity
	 *            用户对象
	 */
	public void update(UserStatus entity) {
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
	 * 删除用户状态
	 * 
	 * @param entity
	 *            用户状态对象
	 */
	public void delete(UserStatus entity) {
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
				String sql = "alter table user_status AUTO_INCREMENT=" + (partition << 56);
				session.createSQLQuery(sql).executeUpdate();
			} catch (Exception e) {
				logger.error(e);
			}
		}

		logger.info("成功修改表：user_status");
	}

}
