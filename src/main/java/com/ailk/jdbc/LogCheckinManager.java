package com.ailk.jdbc;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.ailk.jdbc.entity.LogCheckin;

/**
 * 签到管理
 * 
 * @author xugq
 * 
 */
public class LogCheckinManager implements AlterTableIfc {

	private static final Logger logger = Logger.getLogger(LogCheckinManager.class);
	private static LogCheckinManager instance = new LogCheckinManager();

	/**
	 * 获取操作LogCheckin表的实例
	 * 
	 * @return 操作LogCheckin表的实例
	 */
	public static LogCheckinManager getInstance() {
		return instance;
	}

	/**
	 * 构造函数
	 */
	private LogCheckinManager() {
	}

	/**
	 * 保存签到记录到数据库
	 * 
	 * @param entity
	 *            签到记录
	 */
	public void save(LogCheckin entity) {
		int partition = HibernateUtil.getPartition(entity.getUserId());
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
	 * 获取签到记录
	 * 
	 * @param userId
	 *           用户ID
	 * @return 签到记录
	 */
	public LogCheckin get(long userId) {
		int partition = HibernateUtil.getPartition(userId);
		Session session = HibernateUtil.getSessionFactory(partition).openSession();

		return (LogCheckin) session.get(LogCheckin.class, userId);
	}

	/**
	 * 更新签到记录到数据库
	 * 
	 * @param entity
	 *            签到记录
	 */
	public void update(LogCheckin entity) {
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
	 * 修改表结构
	 */
	@Override
	public void alterTable() {
		logger.info("成功修改表：log_checkin");
	}

}
